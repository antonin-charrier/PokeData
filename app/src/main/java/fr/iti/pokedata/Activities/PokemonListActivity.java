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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.iti.pokedata.R;
import fr.iti.pokedata.Services.GetPokemonListService;

public class PokemonListActivity extends AppCompatActivity {

    private static final String TAG = PokemonListActivity.class.getName();
    public static final String POKEMON_UPDATE = "com.octip.cours.inf4042_11.POKEMON_UPDATE";

    private RecyclerView rvPokemonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        GetPokemonListService.getAllPokemon(this);
        IntentFilter intentFilter = new IntentFilter(POKEMON_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new PokemonUpdate(),intentFilter);

        rvPokemonList = findViewById(R.id.rv_pokemon_list);
        rvPokemonList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPokemonList.setAdapter(new PokemonListAdapter(getPokemonListFromFile()));
    }

    public JSONArray getPokemonListFromFile(){
        try {
            InputStream is = new FileInputStream(getCacheDir() + "/" + "pokemonList.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8")); // construction du tableau
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

        public PokemonListAdapter(JSONArray pokemonList) {
            this.pokemonList = pokemonList;
        }

        @Override
        public PokemonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(PokemonListActivity.this);
            View v = layoutInflater.inflate(R.layout.item_pokemon_list, rvPokemonList, false);
            return new PokemonHolder(v);
        }

        @Override
        public void onBindViewHolder(PokemonHolder holder, int position) {
            try {
                holder.name.setText(pokemonList.getJSONObject(position).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return pokemonList.length();
        }

        public void setNewPokemon(JSONArray pokemonList) {
            notifyDataSetChanged();
        }

        class PokemonHolder extends RecyclerView.ViewHolder {
            public TextView name;

            public PokemonHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.rv_pokemon_list_item);
            }
        }
    }

    public class PokemonUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());
            ((PokemonListAdapter)rvPokemonList.getAdapter()).setNewPokemon(getPokemonListFromFile());
        }
    }
}
