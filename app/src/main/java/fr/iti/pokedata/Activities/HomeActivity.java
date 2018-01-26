package fr.iti.pokedata.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.iti.pokedata.R;
import fr.iti.pokedata.Services.GetPokemonListService;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetPokemonListService.getAllPokemon(this);
        setContentView(R.layout.activity_menu_region);
    }
}
