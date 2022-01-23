package pl.olafwozniak.fuelapp;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.FuelViewHolder> implements Filterable {

    public final List<Fuel> mFuels;
    public LayoutInflater inflater;


    public WordListAdapter(Context context, List<Fuel> fuelList){
        this.inflater = LayoutInflater.from(context);
        this.mFuels = fuelList;
    }

    public static final String EXTRA_MESSAGE = "pl.edu.uwr.pum.recyclerviewwordlistjava.MESSAGE";

    class FuelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView id;
        public TextView fuelText;
        private TextView date;
        private TextView cost;
        private TextView combustion;
        private TextView costperkm;
        final WordListAdapter adapter;
        public final Context context;

        public FuelViewHolder(@NonNull View itemView, WordListAdapter adapter) {
            super(itemView);
            context = itemView.getContext();
            id = itemView.findViewById(R.id.id);
            fuelText = itemView.findViewById(R.id.word);
            date = itemView.findViewById(R.id.date);
            cost = itemView.findViewById(R.id.cost);
            combustion = itemView.findViewById(R.id.combustion);
            costperkm = itemView.findViewById(R.id.costperkm);
            itemView.setOnClickListener(this);
            this.adapter = adapter;
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Fuel currentFuel = mFuels.get(position);
            Intent intent = new Intent(context, SecondActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("id", currentFuel.getId());
            context.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public WordListAdapter.FuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.word_list_item, parent, false);
        return new FuelViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter.FuelViewHolder holder, int position) {
        Fuel currentFuel = mFuels.get(position);
        holder.id.setText("#"+String.valueOf(currentFuel.getId()));
        holder.fuelText.setText(currentFuel.getTitle());
        holder.date.setText(currentFuel.getDate().toString());
        holder.cost.setText(String.valueOf(currentFuel.getTotalCost())+"zł");
        holder.combustion.setText(String.valueOf(currentFuel.getLperkm())+"L/Km");
        holder.costperkm.setText(String.valueOf(currentFuel.getCostperkm())+"zł/1km");
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Fuel> filter = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (constraint.toString().isEmpty()) {
                filter.addAll(mFuels);
            }
            else {
                for (Fuel f : mFuels) {
                    if (f.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filter.add(f);
                    }
                }
            }
            results.values = filter;
            return results;
        }

        @Override
        protected void publishResults(final CharSequence constraint, FilterResults results) {
            mFuels.clear();
            mFuels.addAll((Collection<? extends Fuel>) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return mFuels.size();
    }
}
