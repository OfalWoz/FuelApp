package pl.olafwozniak.fuelapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FuelLab{

    private static FuelLab sFuelLab;

    public static List<Fuel> mFuels;
    private DBHandler mDbHandler;

    public int getSize() { return mFuels.size(); }

    public static FuelLab get(Context context) {
        if(sFuelLab == null) {
            sFuelLab = new FuelLab(context);
        }
        return sFuelLab;
    }

    private FuelLab(Context context) {
        mFuels = new ArrayList<>();
    }

    public Fuel getFuel(int id) {
        for (Fuel crime : mFuels) {
            if (crime.getId() == id) {
                return crime;
            }
        }
        return null;
    }

    public List<Fuel> getFuels() {
        return mFuels;
    }

    public void updateFuel(int id, String title, Date date){
        for(Fuel fuel : mFuels){
            if(fuel.getId() == id){
                fuel.setTitle(title);
                fuel.setDate(date);
            }
        }
    }

    public void deleteFuel(int id){
        int i = 0;
        for(Fuel fuel: mFuels){
            if(fuel.getId() == id) {
                mFuels.remove(fuel);
                break;
            }
        }
    }

    public void newFuel(Fuel fuel){
        fuel.setId(mFuels.size());
        fuel.setTitle("Click to set");
        fuel.setDate(new Date());
        mFuels.add(fuel);
    }
}