package fr.iti.pokedata.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import fr.iti.pokedata.Activities.PokemonListActivity;
import fr.iti.pokedata.Utils.ServicesUtils;

public class GetPokemonService extends IntentService {
    private static final String ACTION_POKEMON = "fr.iti.pokedata.Services.action.POKEMON";
    private static final String TAG = GetPokemonService.class.getName();

    private static final String POKEMON_NAME = "fr.iti.pokedata.Services.extra.POKEMON_NAME";

    public GetPokemonService() {
        super("GetPokemonService");
    }

    public static void getPokemon(Context context, String name) {
        Intent intent = new Intent(context, GetPokemonService.class);
        intent.setAction(ACTION_POKEMON);
        intent.putExtra(POKEMON_NAME, name);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POKEMON.equals(action)) {
                final String name = intent.getStringExtra(POKEMON_NAME);
                handleActionPokemon(name);
            }
        }
    }

    private void handleActionPokemon(String name) {
        Log.i(TAG, "Handling action POKEMON");
        URL url;
        try {
            url = new URL("https://pokeapi.co/api/v2/pokemon/"+name);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                ServicesUtils.copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), name+".json"));
                Log.i(TAG, "Completed download of Pokemon list JSON");
                Intent intent = new Intent(PokemonListActivity.POKEMON_UPDATE);
                intent.putExtra("name", name);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
