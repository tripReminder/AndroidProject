package com.tripreminder.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Trip[] data;
    private LayoutInflater inflater;
    Context context;

    HistoryAdapter(Context context, Trip[] data) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int id = data[position].getTrip_id();
        String title = data[position].getTitle();
        boolean status = data[position].getStatus();
        String image = data[position].getImagePath();

        holder.trip_id = id;
        holder.title.setText(title);
        if(status)
            holder.status.setImageResource((Integer)1);
        else
            holder.status.setImageResource((Integer)0);
        //holder.trippic.setImageResource((Integer)1);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        int trip_id;
        View view;
        TextView title;
        ImageView status, trippic;
        Button show, delete;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = itemView.findViewById(R.id.nameTxt);
            status = itemView.findViewById(R.id.statusImg);
            trippic = itemView.findViewById(R.id.tripimg);
            show= itemView.findViewById(R.id.showBtn);
            delete = itemView.findViewById(R.id.deleteBtn);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, title.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
