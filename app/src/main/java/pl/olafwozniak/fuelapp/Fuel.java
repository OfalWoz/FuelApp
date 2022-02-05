package pl.olafwozniak.fuelapp;

import java.util.Date;

public class Fuel {
    private int mId;
    private String mTitle;
    private Date mDate;
    private float priceLiters;
    private float totalCost;
    private float Liters;
    private float km;
    private float lperkm;
    private float costperkm;
    private String Type;

    public void setTitle(String s) { this.mTitle = s; }

    public String getTitle() { return mTitle; }

    public int getId() { return mId; }

    public void setPriceLiters(float p) { this.priceLiters = p; }

    public float getPriceLiters() { return priceLiters; }

    public void setTotalCost(float c) { this.totalCost = c; }

    public float getTotalCost() { return totalCost; }

    public void setLiters(float l) { this.Liters = l; }

    public float getLiters() { return Liters; }

    public void setKm(float km) { this.km = km; }

    public float getKm() { return km; }

    public void setLperkm(float lperkm) { this.lperkm = lperkm; }

    public float getLperkm() { return lperkm; }

    public void setCostperkm(float costperkm) { this.costperkm = costperkm; }

    public float getCostperkm() { return costperkm; }

    public void setId(int mId) { this.mId = mId; }

    public void setDate(Date date) { this.mDate = date; }

    public Date getDate() { return mDate; }

    public void setType(String type) { this.Type = type; }

    public String getType() { return Type; }
}
