package fr.iti.pokedata.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.iti.pokedata.Models.Pokemon;
import fr.iti.pokedata.R;
import fr.iti.pokedata.Services.GetPokemonListService;
import fr.iti.pokedata.Services.GetPokemonService;
import fr.iti.pokedata.Utils.Utils;

public class PokemonListActivity extends AppCompatActivity {

    private static final String TAG = PokemonListActivity.class.getName();
    public static final String POKEMON_UPDATE = "fr.iti.pokedata.POKEMON_UPDATE";
    public static final String POKEMON_LIST_UPDATE = "fr.iti.pokedata.POKEMON_LIST_UPDATE";

    private RecyclerView rvPokemonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);
        setTitle("All Pokemon");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GetPokemonListService.getAllPokemon(this);
        IntentFilter intentFilter = new IntentFilter(POKEMON_LIST_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new PokemonListUpdate(), intentFilter);

        rvPokemonList = findViewById(R.id.rv_pokemon_list);
        rvPokemonList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvPokemonList.setAdapter(new PokemonListAdapter(getPokemonListFromFile(), this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public JSONArray getPokemonListFromFile(){
        try {
            InputStream is = new FileInputStream(getCacheDir() + "/" + "pokemonList.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            JSONObject json = new JSONObject(new String(buffer, "UTF-8"));
            return json.getJSONArray("results");
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public JSONObject getPokemonFromFile(String name) {
        try {
            InputStream is = new FileInputStream(getCacheDir() + "/" + name + ".json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONObject(new String(buffer, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    private class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.PokemonHolder> {

        private JSONArray pokemonList;
        private Context context;

        public PokemonListAdapter(JSONArray pokemonList, Context context) {
            this.pokemonList = pokemonList;
            this.context = context;
        }

        @Override
        public PokemonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(PokemonListActivity.this);
            View v = layoutInflater.inflate(R.layout.item_pokemon_list, parent, false);
            WindowManager windowManager = (WindowManager)v.getContext().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            v.setLayoutParams(new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new PokemonHolder(v);
        }

        @Override
        public void onBindViewHolder(PokemonHolder holder, int position) {
            try {
                /*String currentPokemonName = pokemonList.getJSONObject(position).getString("name");
                GetPokemonService.getPokemon(context, currentPokemonName);
                IntentFilter intentFilter = new IntentFilter(POKEMON_UPDATE);
                LocalBroadcastManager.getInstance(context).registerReceiver(new PokemonUpdate(), intentFilter);

                Pokemon pokemon = new Pokemon("", "", "","","");
                bindPokemonToHolder(holder, pokemon);*/
                String name = pokemonList.getJSONObject(position).getString("name").trim();
                holder.name1.setText(Utils.getFormattedString(name));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return pokemonList.length();
        }

        public void setNewPokemon(JSONArray pokemonList) {
            this.pokemonList = pokemonList;
            notifyDataSetChanged();
        }

        public void setPokemonData(JSONObject pokemonData) {
            String name1 = "";
            String name2 = "";
            String id = "";
            String type1 = "";
            String type2 = "";
            try {
                name1 = pokemonData.getString("name");
                name2 = pokemonData.getString("name");
                id = pokemonData.getString("id");
                JSONArray types = pokemonData.getJSONArray("types");
                type1 = types.getJSONObject(0).getJSONObject("type").getString("name");
                if(types.length() == 2) {
                    type2 = types.getJSONObject(1).getJSONObject("type").getString("name");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Pokemon pokemon = new Pokemon(name1, name2, id, type1, type2);
            notifyDataSetChanged();
        }

        class PokemonHolder extends RecyclerView.ViewHolder {
            public ImageView sprite;
            public TextView name1;
            public TextView name2;
            public TextView id;
            public TextView type1;
            public TextView type2;

            public PokemonHolder(View itemView) {
                super(itemView);
                sprite = itemView.findViewById(R.id.rv_pokemon_list_item_sprite);
                name1 = itemView.findViewById(R.id.rv_pokemon_list_item_name1);
                name2 = itemView.findViewById(R.id.rv_pokemon_list_item_name2);
                id = itemView.findViewById(R.id.rv_pokemon_list_item_id);
                type1 = itemView.findViewById(R.id.rv_pokemon_list_item_type1);
                type2 = itemView.findViewById(R.id.rv_pokemon_list_item_type2);
            }
        }
    }

    public class PokemonListUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());
            ((PokemonListAdapter)rvPokemonList.getAdapter()).setNewPokemon(getPokemonListFromFile());
        }
    }

    public class PokemonUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());
            String name = intent.getStringExtra("name");
            ((PokemonListAdapter)rvPokemonList.getAdapter()).setPokemonData(getPokemonFromFile(name));
        }
    }

    private static void bindPokemonToHolder(PokemonListAdapter.PokemonHolder holder, Pokemon pokemon) {
        holder.name1.setText(Utils.getFormattedString(pokemon.getName1()));
        holder.name2.setText(Utils.getFormattedString(pokemon.getName2()));
        holder.id.setText(Utils.getFormattedString(pokemon.getId()));
        holder.type1.setText(Utils.getFormattedString(pokemon.getType1()));
        holder.type2.setText(Utils.getFormattedString(pokemon.getType2()));
    }
}
