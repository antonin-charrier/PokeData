package fr.iti.pokedata.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.iti.pokedata.R;
import fr.iti.pokedata.Services.GetPokemonService;
import fr.iti.pokedata.Utils.Utils;

public class PokemonActivity extends AppCompatActivity {

    private static final String TAG = PokemonActivity.class.getName();
    public static final String POKEMON_UPDATE = "fr.iti.pokedata.POKEMON_UPDATE";

    private String name = "";
    private int id = 0;
    private int type1;
    private int type2;
    private String abilities = "";
    private int height;
    private int weight;

    TextView tv_name;
    TextView tv_id;
    TextView tv_type1;
    TextView tv_type2;
    TextView tv_abilities;
    TextView tv_height;
    TextView tv_weight;
    ScrollView sv;
    RelativeLayout pbLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);
        Intent intent = getIntent();
        name = intent.getStringExtra("pokemon_name");
        setTitle(Utils.getFormattedString(name));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tv_name = findViewById(R.id.pokemon_name);
        tv_id = findViewById(R.id.pokemon_id);
        tv_type1 = findViewById(R.id.pokemon_type1);
        tv_type2 = findViewById(R.id.pokemon_type2);
        tv_abilities = findViewById(R.id.pokemon_abilities);
        tv_height = findViewById(R.id.pokemon_height);
        tv_weight = findViewById(R.id.pokemon_weight);
        sv = findViewById(R.id.pokemon_sv);
        pbLayout = findViewById(R.id.pokemon_progress_bar_layout);

        sv.setVisibility(View.GONE);
        pbLayout.setVisibility(View.VISIBLE);

        GetPokemonService.getPokemon(this, name);
        IntentFilter intentFilter = new IntentFilter(POKEMON_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new PokemonActivity.PokemonUpdate(), intentFilter);

        type1 = R.string.type_unknown;
        type2 = R.string.type_unknown;
        setupUI();
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

    private void setupUI() {
        tv_name.setText(Utils.getFormattedString(name));
        tv_id.setText(Utils.getFormattedId(id));
        updateTypes();
        tv_abilities.setText(Utils.getFormattedString(abilities));
        tv_height.setText(Double.toString(height * 0.1) + " m");
        tv_weight.setText(Double.toString(weight * 0.1) + " kg");
    }

    private void updateTypes() {
        switch (type1) {
            case R.string.type_normal:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_normal);
                break;
            case R.string.type_fire:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_fire);
                break;
            case R.string.type_water:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_water);
                break;
            case R.string.type_grass:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_grass);
                break;
            case R.string.type_bug:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_bug);
                break;
            case R.string.type_electric:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_electric);
                break;
            case R.string.type_ground:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_ground);
                break;
            case R.string.type_rock:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_rock);
                break;
            case R.string.type_steel:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_steel);
                break;
            case R.string.type_fighting:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_fighting);
                break;
            case R.string.type_psychic:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_psychic);
                break;
            case R.string.type_ghost:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_ghost);
                break;
            case R.string.type_dark:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_dark);
                break;
            case R.string.type_dragon:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_dragon);
                break;
            case R.string.type_fairy:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_fairy);
                break;
            case R.string.type_ice:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_ice);
                break;
            case R.string.type_flying:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_flying);
                break;
            case R.string.type_poison:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_poison);
                break;
            case 0:
                tv_type1.setVisibility(View.GONE);
                break;
            case R.string.type_unknown:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_unknown);
                break;
            default:
                tv_type1.setText(Utils.getFormattedString(getString(type1)).toUpperCase());
                tv_type1.setBackgroundResource(R.drawable.background_type_unknown);
                break;
        }

        switch (type2) {
            case R.string.type_normal:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_normal);
                break;
            case R.string.type_fire:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_fire);
                break;
            case R.string.type_water:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_water);
                break;
            case R.string.type_grass:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_grass);
                break;
            case R.string.type_bug:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_bug);
                break;
            case R.string.type_electric:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_electric);
                break;
            case R.string.type_ground:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_ground);
                break;
            case R.string.type_rock:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_rock);
                break;
            case R.string.type_steel:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_steel);
                break;
            case R.string.type_fighting:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_fighting);
                break;
            case R.string.type_psychic:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_psychic);
                break;
            case R.string.type_ghost:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_ghost);
                break;
            case R.string.type_dark:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_dark);
                break;
            case R.string.type_dragon:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_dragon);
                break;
            case R.string.type_fairy:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_fairy);
                break;
            case R.string.type_ice:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_ice);
                break;
            case R.string.type_flying:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_flying);
                break;
            case R.string.type_poison:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_poison);
                break;
            case 0:
                tv_type2.setVisibility(View.GONE);
                break;
            case R.string.type_unknown:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_unknown);
                break;
            default:
                tv_type2.setText(Utils.getFormattedString(getString(type2)).toUpperCase());
                tv_type2.setBackgroundResource(R.drawable.background_type_unknown);
                break;
        }
    }

    private int checkTypes(String typeValue){
        switch (typeValue) {
            case "normal":
                return R.string.type_normal;
            case "fire":
                return R.string.type_fire;
            case "water":
                return R.string.type_water;
            case "grass":
                return R.string.type_grass;
            case "bug":
                return R.string.type_bug;
            case "electric":
                return R.string.type_electric;
            case "ground":
                return R.string.type_ground;
            case "rock":
                return R.string.type_rock;
            case "steel":
                return R.string.type_steel;
            case "fighting":
                return R.string.type_fighting;
            case "psychic":
                return R.string.type_psychic;
            case "ghost":
                return R.string.type_ghost;
            case "dark":
                return R.string.type_dark;
            case "dragon":
                return R.string.type_dragon;
            case "fairy":
                return R.string.type_fairy;
            case "ice":
                return R.string.type_ice;
            case "flying":
                return R.string.type_flying;
            case "poison":
                return R.string.type_poison;
            case "no_second_type":
                return 0;
            default:
                return R.string.type_unknown;
        }
    }

    public void setPokemon(JSONObject pokemon) {
        try {
            this.name = pokemon.getString("name");
            this.id = Integer.parseInt(pokemon.getString("id"));
            JSONArray typesArray = pokemon.getJSONArray("types");
            JSONObject jsonType0 = typesArray.getJSONObject(0);
            String stringType0 = jsonType0.getJSONObject("type").getString("name");
            String stringType1 = "no_second_type";
            if(jsonType0.getString("slot").equals("2")) {
                stringType1 = pokemon.getJSONArray("types").getJSONObject(1).getJSONObject("type").getString("name");
                this.type2 = checkTypes(stringType0);
                this.type1 = checkTypes(stringType1);
            } else {
                this.type1 = checkTypes(stringType0);
                this.type2 = checkTypes(stringType1);
            }
            JSONArray abilitiesArray = pokemon.getJSONArray("abilities");
            this.abilities = "";
            for (int i = 0; i < abilitiesArray.length(); i++) {
                String abilityName = Utils.getFormattedString(abilitiesArray.getJSONObject(i).getJSONObject("ability").getString("name"));
                if(abilitiesArray.getJSONObject(i).getBoolean("is_hidden")) {
                    abilityName += " " + getString(R.string.isHidden);
                }
                if(this.abilities != "")
                    abilityName += ", ";
                this.abilities = abilityName + this.abilities;
            }
            this.height = pokemon.getInt("height");
            this.weight = pokemon.getInt("weight");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setupUI();
        pbLayout.setVisibility(View.GONE);
        sv.setVisibility(View.VISIBLE);
    }

    public JSONObject getPokemonFromFile(String name){
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

    public class PokemonUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());
            setPokemon(getPokemonFromFile(name));
        }
    }

}
