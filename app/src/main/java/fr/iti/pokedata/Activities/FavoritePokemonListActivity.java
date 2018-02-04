package fr.iti.pokedata.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import fr.iti.pokedata.R;
import fr.iti.pokedata.Utils.Utils;

public class FavoritePokemonListActivity extends AppCompatActivity {

    private static final String TAG = FavoritePokemonListActivity.class.getName();
    private static final String IS_DOWNLOADED = "fr.iti.pokedata.FAVORITE_POKEMON_LIST_DOWNLOADED";
    public static final String FAVORITE_POKEMON_LIST_UPDATE = "fr.iti.pokedata.FAVORITE_POKEMON_LIST_UPDATE";

    private RecyclerView rv_favoritePokemonList;
    RelativeLayout pb_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_pokemon_list);
        setTitle(getString(R.string.favorites_pokemon));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pb_loading = findViewById(R.id.favorite_pokemon_list_progress_bar_layout);
        rv_favoritePokemonList = findViewById(R.id.rv_favorite_pokemon_list);

        rv_favoritePokemonList.setVisibility(View.GONE);
        pb_loading.setVisibility(View.VISIBLE);

        rv_favoritePokemonList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_favoritePokemonList.setAdapter(new FavoritePokemonListAdapter(getFavoritePokemonListFromFile()));
        ((FavoritePokemonListAdapter)rv_favoritePokemonList.getAdapter()).setNewPokemon(getFavoritePokemonListFromFile());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String name = (String) ((FavoritePokemonListAdapter.PokemonHolder)viewHolder).name.getText();
                JSONArray favoritePokemonListArray = getFavoritePokemonListFromFile();
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        favoritePokemonListArray.remove(viewHolder.getAdapterPosition());
                    } else {
                        JSONArray list = new JSONArray();
                        if (favoritePokemonListArray != null) {
                            for (int i=0;i<favoritePokemonListArray.length();i++)
                            {
                                //Excluding the item at position
                                if (i != viewHolder.getAdapterPosition())
                                {
                                    list.put(favoritePokemonListArray.get(i));
                                }
                            }
                        }
                        favoritePokemonListArray = list;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setFavoritePokemonListToFile(favoritePokemonListArray);
                JSONArray rvList = ((FavoritePokemonListAdapter) rv_favoritePokemonList.getAdapter()).getFavoritePokemonList();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    rvList.remove(viewHolder.getAdapterPosition());
                } else {
                    JSONArray list = new JSONArray();
                    if (rvList != null) {
                        for (int i=0;i < rvList.length();i++)
                        {
                            //Excluding the item at position
                            if (i != viewHolder.getAdapterPosition())
                            {
                                try {
                                    list.put(rvList.get(i));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    rvList = list;
                    ((FavoritePokemonListAdapter) rv_favoritePokemonList.getAdapter()).setNewPokemon(rvList);
                }
                rv_favoritePokemonList.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                rv_favoritePokemonList.getAdapter().notifyDataSetChanged();
                Snackbar.make(rv_favoritePokemonList, Utils.getFormattedString(name) + " " + getString(R.string.removedFromFav), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                final Intent emptyIntent = new Intent();
                PendingIntent pendingIntent = PendingIntent.getActivity(FavoritePokemonListActivity.this, 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Utils.initChannels(FavoritePokemonListActivity.this);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(FavoritePokemonListActivity.this, "default")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(getString(R.string.favorites_pokemon))
                        .setContentText(Utils.getFormattedString(name) + " " + getString(R.string.removedFromFav))
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, builder.build());
            }
        });
        itemTouchHelper.attachToRecyclerView(rv_favoritePokemonList);
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

    public JSONArray getFavoritePokemonListFromFile(){
        try {
            InputStream is = new FileInputStream(getCacheDir() + "/" + "favoritePokemonList.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }


    public void setFavoritePokemonListToFile(JSONArray array){
        try {
            Writer output = null;
            File file = new File(getCacheDir(), "favoritePokemonList.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(array.toString());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class FavoritePokemonListAdapter extends RecyclerView.Adapter<FavoritePokemonListAdapter.PokemonHolder> {

        private JSONArray favoritePokemonList;

        public JSONArray getFavoritePokemonList() { return favoritePokemonList; }

        FavoritePokemonListAdapter(JSONArray favoritePokemonList) {
            this.favoritePokemonList = favoritePokemonList;
        }

        @Override
        public PokemonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FavoritePokemonListActivity.this);
            View v = layoutInflater.inflate(R.layout.item_favorite_pokemon_list, parent, false);
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
                final String name = favoritePokemonList.getJSONObject(position).getString("name").trim();
                holder.name.setText(Utils.getFormattedString(name));
                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FavoritePokemonListActivity.this, PokemonActivity.class);
                        intent.putExtra("pokemon_name", name);
                        FavoritePokemonListActivity.this.startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return favoritePokemonList.length();
        }

        void setNewPokemon(JSONArray favoritePokemonList) {
            this.favoritePokemonList = favoritePokemonList;
            pb_loading.setVisibility(View.GONE);
            rv_favoritePokemonList.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
        }

        class PokemonHolder extends RecyclerView.ViewHolder {
            LinearLayout item;
            public TextView name;

            PokemonHolder(View itemView) {
                super(itemView);
                item = itemView.findViewById(R.id.rv_favorite_pokemon_list_item);
                name = itemView.findViewById(R.id.rv_favorite_pokemon_list_item_name);
            }
        }
    }
}
