package pl.olafwozniak.fuelapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    public List<Fuel> fuels =  FuelLab.get(this).getFuels();
    public int Id;
    public Date newDate = new Date();
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Button timeButton;
    int hour, minute;
    public String date = "Set Date";
    public String time = "Set Time";
    private DBHandler mDbHandler;

    private ViewPager2 viewPager2;
    private int fuelPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_view_pager2);
        mDbHandler = new DBHandler(this);
        viewPager2 = findViewById(R.id.detail_view_pager);
        FuelActivityAdapter adapter = new FuelActivityAdapter();
        viewPager2.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            Id = getIntent().getIntExtra("id",0);
            fuelPos = getIntent().getIntExtra("position",0);
        }
        viewPager2.setCurrentItem(fuelPos, false);
    }

    @SuppressLint("WrongViewCast")
    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                //date = makeDateString(day, month, year);
                newDate.setDate(day);
                newDate.setMonth(month - 1);
                newDate.setYear(year - 1900);
            }
        };
        Calendar cel = Calendar.getInstance();
        int year = cel.get(Calendar.YEAR);
        int month = cel.get(Calendar.MONTH);
        int day = cel.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    public void time(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                minute = minute;
                newDate.setHours(hour);
                newDate.setMinutes(minute);
                //time = String.format(Locale.getDefault(),"%02d:%02d",hour,minute);
            }
        };
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, minute);

        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,style, onTimeSetListener,hour,minute,true);
        timePickerDialog.show();
    }

    public class FuelActivityAdapter extends RecyclerView.Adapter<FuelActivityAdapter.FuelViewHolder> {

        public class FuelViewHolder extends RecyclerView.ViewHolder {
            public Date newDate;
            private EditText Title;
            private TextView viewId;
            private Button Date;
            private Button Time;

            private EditText priceLiters;
            private EditText totalCost;
            private TextView Liters;
            private EditText km;

            private Button First;
            private Button Last;

            FuelActivityAdapter adapter;

            public FuelViewHolder(@NonNull View itemView, FuelActivityAdapter adapter){
                super(itemView);
                Title = itemView.findViewById(R.id.title);
                Date = itemView.findViewById(R.id.date);
                Time = itemView.findViewById(R.id.time);
                First = itemView.findViewById(R.id.first);
                Last = itemView.findViewById(R.id.last);
                priceLiters = itemView.findViewById(R.id.priceLiters);
                totalCost = itemView.findViewById(R.id.totalCost);
                Liters = itemView.findViewById(R.id.liters);
                km = itemView.findViewById(R.id.km);
                this.adapter = adapter;

                Date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initDatePicker();
                        Date.setText("Date seted!"); //niestety pokazywanie wybranej daty i godziny dzialalo z opoznieniem dlatego ustawilem tylko zmiane napisu
                    }
                });

                Time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        time(view);
                        Time.setText("Time seted!");
                    }
                });

            }
        }

        @NonNull
        @Override
        public FuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FuelViewHolder(
                    (LayoutInflater.from(SecondActivity.this).inflate(
                            R.layout.activity_second,
                            parent,
                            false
                    )),
                    this
            );
        }

        @Override
        public void onBindViewHolder(@NonNull FuelViewHolder holder, int position) {
            Fuel current = fuels.get(position);
            //holder.viewId.setText(current.getId());
            holder.Title.setText(current.getTitle());
            holder.totalCost.setText(String.valueOf(current.getTotalCost()));
            holder.priceLiters.setText(String.valueOf(current.getPriceLiters()));
            holder.Liters.setText(String.valueOf(current.getLiters()) + "L");
            holder.km.setText(String.valueOf(current.getKm()));
            holder.Date.setText(date);
            holder.Time.setText(time);
            newDate = new Date();
            Id = fuels.get(position).getId();
            fuelPos = viewPager2.getCurrentItem();
            holder.Title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    current.setTitle(holder.Title.getText().toString());
                    mDbHandler.updateFuel(current.getId(), current.getTitle(), current.getDate(), current.getPriceLiters(), current.getTotalCost(), current.getLiters(), current.getKm(), current.getLperkm(), current.getCostperkm());
                }
            });
            holder.Date.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    holder.newDate = newDate;
                }
                @Override
                public void afterTextChanged(Editable s) {
                    current.setDate(holder.newDate);
                    mDbHandler.updateFuel(current.getId(), current.getTitle(), current.getDate(), current.getPriceLiters(), current.getTotalCost(), current.getLiters(), current.getKm(), current.getLperkm(), current.getCostperkm());
                }
            });
            holder.Time.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    holder.newDate = newDate;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    holder.newDate = newDate;
                    current.setDate(holder.newDate);
                    mDbHandler.updateFuel(current.getId(), current.getTitle(), current.getDate(), current.getPriceLiters(), current.getTotalCost(), current.getLiters(), current.getKm(), current.getLperkm(), current.getCostperkm());
                }
            });
            holder.priceLiters.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (holder.priceLiters.getText().toString().isEmpty() || holder.totalCost.getText().toString().isEmpty() || holder.priceLiters.getText().toString().endsWith(".") || holder.totalCost.getText().toString().endsWith(".")) {
                    } else {
                        holder.Liters.setText(String.valueOf(Float.parseFloat(holder.totalCost.getText().toString()) / Float.parseFloat(holder.priceLiters.getText().toString())) + "L");
                        current.setLiters(Float.parseFloat(holder.totalCost.getText().toString()) / Float.parseFloat(holder.priceLiters.getText().toString()));
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if (holder.priceLiters.getText().toString().isEmpty() || holder.totalCost.getText().toString().isEmpty() || holder.priceLiters.getText().toString().endsWith(".") || holder.totalCost.getText().toString().endsWith(".")){
                    } else {
                        current.setPriceLiters(Float.parseFloat(holder.priceLiters.getText().toString()));
                        mDbHandler.updateFuel(current.getId(), current.getTitle(), current.getDate(), current.getPriceLiters(), current.getTotalCost(), current.getLiters(), current.getKm(), current.getLperkm(), current.getCostperkm());
                    }
                }
            });
            holder.totalCost.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (holder.priceLiters.getText().toString().isEmpty() || holder.totalCost.getText().toString().isEmpty() || holder.priceLiters.getText().toString().endsWith(".") || holder.totalCost.getText().toString().endsWith(".")){
                    } else {
                        holder.Liters.setText(String.valueOf(Float.parseFloat(holder.totalCost.getText().toString()) / Float.parseFloat(holder.priceLiters.getText().toString())) + "L");
                        current.setLiters(Float.parseFloat(holder.totalCost.getText().toString()) / Float.parseFloat(holder.priceLiters.getText().toString()));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (holder.priceLiters.getText().toString().isEmpty() || holder.totalCost.getText().toString().isEmpty() || holder.priceLiters.getText().toString().endsWith(".") || holder.totalCost.getText().toString().endsWith(".")){
                    } else {
                        current.setTotalCost(Float.parseFloat(holder.totalCost.getText().toString()));
                        mDbHandler.updateFuel(current.getId(), current.getTitle(), current.getDate(), current.getPriceLiters(), current.getTotalCost(), current.getLiters(), current.getKm(), current.getLperkm(), current.getCostperkm());
                    }
                }
            });
            holder.km.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (holder.priceLiters.getText().toString().isEmpty() || holder.totalCost.getText().toString().isEmpty() || holder.priceLiters.getText().toString().endsWith(".") || holder.totalCost.getText().toString().endsWith(".")|| holder.km.getText().toString().isEmpty() || holder.km.getText().toString().endsWith(".")){
                    } else {
                        int id = current.getId();
                        if(id == 0){
                            current.setCostperkm(0);
                            current.setLperkm(0);
                        }else {
                            Fuel previous = fuels.get(id-1);
                            float l = Float.parseFloat(holder.totalCost.getText().toString()) / Float.parseFloat(holder.priceLiters.getText().toString());
                            float road = Float.parseFloat(holder.km.getText().toString()) - previous.getKm();
                            current.setLperkm((l/road)*100);
                            current.setCostperkm((Float.parseFloat(holder.totalCost.getText().toString())/road));

                        }
                        current.setKm(Float.parseFloat(holder.km.getText().toString()));
                        mDbHandler.updateFuel(current.getId(), current.getTitle(), current.getDate(), current.getPriceLiters(), current.getTotalCost(), current.getLiters(), current.getKm(), current.getLperkm(), current.getCostperkm());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return fuels.size();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void First(View view){
        viewPager2.setCurrentItem(0);
    }

    public void Last(View view){
        viewPager2.setCurrentItem(fuels.size());
    }

    public void delCrime(View view) {
        mDbHandler.deleteFuel(Id);
        finish();
    }
}
