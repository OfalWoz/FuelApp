package pl.olafwozniak.fuelapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "pl.edu.uwr.pum.recyclerviewwordlistjava.MESSAGE";
    private RecyclerView recyclerView;
    private pl.olafwozniak.fuelapp.WordListAdapter WordListAdapter;
    public List<Fuel> fuelList = FuelLab.get(this).getFuels();

    private FuelLab mFuelLab;
    private DBHandler DbHandler;
    private SearchView svFuelSearcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHandler = new DBHandler(this);
        mFuelLab.get(this);
        initialize();

        recyclerView = findViewById(R.id.recyclerView);
        WordListAdapter = new WordListAdapter(this, fuelList);
        recyclerView.setAdapter(WordListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        svFuelSearcher = findViewById(R.id.search);

        svFuelSearcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                WordListAdapter.getFilter().filter(newText);
                return false;
            }
        });

        svFuelSearcher.setOnCloseListener(() -> {
            initialize();
            WordListAdapter.notifyDataSetChanged();
            return false;
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        WordListAdapter.notifyDataSetChanged();
    }

    public void addFuel(View view) {
        Fuel fuel = new Fuel();
        DbHandler.addFuel(fuel);
        WordListAdapter.notifyDataSetChanged();
    }

    private void initialize() {
        Cursor c = DbHandler.getFuels();
        fuelList.clear();

        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                Fuel fuel = new Fuel();
                fuel.setId(Integer.parseInt(c.getString(1)));
                fuel.setTitle(c.getString(2));
                fuel.setDate(new Date(c.getString(3)));
                fuel.setPriceLiters(Float.parseFloat(c.getString(4)));
                fuel.setTotalCost(Float.parseFloat(c.getString(5)));
                fuel.setLiters(Float.parseFloat(c.getString(6)));
                fuel.setKm(Float.parseFloat(c.getString(7)));
                fuel.setLperkm(Float.parseFloat(c.getString(8)));
                fuel.setCostperkm(Float.parseFloat(c.getString(9)));
                fuelList.add(fuel);
            }
        }
    }
}