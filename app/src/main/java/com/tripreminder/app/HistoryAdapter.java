package com.tripreminder.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Trip[] details;
    private LayoutInflater inflater;
    Context context;

    public HistoryAdapter(Context context, Trip[] details) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.details = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_trip, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.TxtTripName.setText(details[position].getTitle());
        holder.check.setImageResource(details[position].getStatusImg());
        holder.placeimg.setImageResource(details[position].getTripPic());
    }

    @Override
    public int getItemCount() {
        return details.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public View Layout;
        public TextView TxtTripName;
        public Button btnDetails, btnCancel;
        public ImageView check, placeimg;
        public ConstraintLayout constraintLayout;

        ViewHolder(View itemView) {
            super(itemView);
            Layout = itemView;
            TxtTripName = itemView.findViewById(R.id.nameTxt);
            check = itemView.findViewById(R.id.statusImg);
            placeimg = itemView.findViewById(R.id.tripimg);
            btnDetails = itemView.findViewById(R.id.showBtn);
            btnCancel = itemView.findViewById(R.id.deleteBtn);

        }
    }

}
