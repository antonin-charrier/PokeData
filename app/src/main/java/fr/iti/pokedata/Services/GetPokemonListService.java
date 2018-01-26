package fr.iti.pokedata.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetPokemonListService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_POKEMON_LIST = "fr.iti.pokedata.action.POKEMON_LIST";

    public GetPokemonListService() {
        super("GetPokemonListService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
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

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPokemonList() {
        Log.i("POKEMON_LIST", "Handling action PokemonList");
    }
}
