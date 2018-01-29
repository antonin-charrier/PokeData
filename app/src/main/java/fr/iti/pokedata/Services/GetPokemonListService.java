package fr.iti.pokedata.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import fr.iti.pokedata.Activities.PokemonListActivity;
import fr.iti.pokedata.Utils.ServicesUtils;

public class GetPokemonListService extends IntentService {
    private static final String ACTION_POKEMON_LIST = "fr.iti.pokedata.action.POKEMON_LIST";
    private static final String TAG = GetPokemonListService.class.getName();


    public GetPokemonListService() {
        super("GetPokemonListService");
    }

    public static void getAllPokemon(Context context) {
        Intent intent = new Intent(context, GetPokemonListService.class);
        intent.setAction(ACTION_POKEMON_LIST);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POKEMON_LIST.equals(action)) {
                handleActionPokemonList();
            }
        }
    }

    private void handleActionPokemonList() {
        Log.i(TAG, "Handling action POKEMON_LIST");
        URL url;
        try {
            url = new URL("https://pokeapi.co/api/v2/pokemon/?limit=949");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                ServicesUtils.copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "pokemonList.json"));
                Log.i(TAG, "Completed download of Pokemon list JSON");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(PokemonListActivity.POKEMON_UPDATE));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
