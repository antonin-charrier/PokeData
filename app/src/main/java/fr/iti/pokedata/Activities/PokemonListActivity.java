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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.iti.pokedata.R;
import fr.iti.pokedata.Services.GetPokemonListService;
import fr.iti.pokedata.Utils.Utils;

public class PokemonListActivity extends AppCompatActivity {

    private static final String TAG = PokemonListActivity.class.getName();
    public static final String POKEMON_LIST_UPDATE = "fr.iti.pokedata.POKEMON_LIST_UPDATE";

    private RecyclerView rv_pokemonList;
    RelativeLayout pb_loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);
        setTitle(getString(R.string.all_pokemon));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GetPokemonListService.getAllPokemon(this);
        IntentFilter intentFilter = new IntentFilter(POKEMON_LIST_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new PokemonListUpdate(), intentFilter);

        pb_loading = findViewById(R.id.pokemon_list_progress_bar_layout);
        rv_pokemonList = findViewById(R.id.rv_pokemon_list);

        rv_pokemonList.setVisibility(View.GONE);
        pb_loading.setVisibility(View.VISIBLE);

        rv_pokemonList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_pokemonList.setAdapter(new PokemonListAdapter(getPokemonListFromFile()));
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

    private class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.PokemonHolder> {

        private JSONArray pokemonList;

        PokemonListAdapter(JSONArray pokemonList) {
            this.pokemonList = pokemonList;
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
                final String name = pokemonList.getJSONObject(position).getString("name").trim();
                holder.name.setText(Utils.getFormattedString(name));
                holder.id.setText(Utils.getFormattedId(position + 1));
                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PokemonListActivity.this, PokemonActivity.class);
                        intent.putExtra("pokemon_name", name);
                        PokemonListActivity.this.startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return pokemonList.length();
        }

        void setNewPokemon(JSONArray pokemonList) {
            this.pokemonList = pokemonList;
            pb_loading.setVisibility(View.GONE);
            rv_pokemonList.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
        }

        class PokemonHolder extends RecyclerView.ViewHolder {
            LinearLayout item;
            public TextView id;
            public TextView name;

            PokemonHolder(View itemView) {
                super(itemView);
                id = itemView.findViewById(R.id.rv_pokemon_list_item_id);
                item = itemView.findViewById(R.id.rv_pokemon_list_item);
                name = itemView.findViewById(R.id.rv_pokemon_list_item_name);
            }
        }
    }

    public class PokemonListUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());
            ((PokemonListAdapter) rv_pokemonList.getAdapter()).setNewPokemon(getPokemonListFromFile());
        }
    }
}
