package pl.olafwozniak.fuelapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class SecondActivityAdapter extends RecyclerView.Adapter<SecondActivityAdapter.FuelViewHolder> {

    private List<Fuel> fuelList;
    private LayoutInflater inflater;

    public SecondActivityAdapter(SecondActivity context) {
        inflater = LayoutInflater.from(context);
        this.fuelList = fuelList;
    }

    class FuelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView Title;
        private TextView DateText;

        final SecondActivityAdapter adapter;

        public FuelViewHolder(@NonNull View itemView, SecondActivityAdapter adapter) {
            super(itemView);
            Title = itemView.findViewById(R.id.title);
            DateText = itemView.findViewById(R.id.date);
            this.adapter = adapter;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final Intent intent;

            int position = getLayoutPosition();
            Fuel element = fuelList.get(position);
            fuelList.set(position, element);
            adapter.notifyItemChanged(position);

            intent = new Intent(view.getContext(), Fuel.class);
            intent.putExtra("position", getLayoutPosition());
            view.getContext().startActivity(intent);
        }
    }

    @NonNull
    @Override
    public FuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.word_list_item, parent, false);
        return new FuelViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull FuelViewHolder holder, int position) {
        Fuel current = fuelList.get(position);
        holder.Title.setText(current.getTitle());
        holder.DateText.setText(current.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return fuelList.size();
    }


}
