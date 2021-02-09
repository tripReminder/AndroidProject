package com.tripreminder.app;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
    Button showBtn;
    TripsHistory tripsHistory;

    HistoryAdapter(Context context, TripsHistory tripsHistory, Trip[] data) {
        this.tripsHistory = tripsHistory;
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

        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trip trip = data[position];
                Intent intent = new Intent(context , TripDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("trip", trip);
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !Settings.canDrawOverlays(context)) {
                    tripsHistory.chickOverlayPermission();
                } else{
                    showAlert(context, "Delete Trip", "Are you sure you want to delete this Trip?",
                            "Yes", "No", data[position]);
                }
            }
        });
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

    void showAlert(Context context, String title, String body, String yes, String no, Trip trip) {
        final WindowManager manager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.alpha = 1.0f;
        layoutParams.packageName = context.getPackageName();
        layoutParams.buttonBrightness = 1f;
        layoutParams.windowAnimations = android.R.style.Animation_Dialog;

        final View view = View.inflate(context.getApplicationContext(),R.layout.alert_view, null);
        TextView titleLbl = (TextView) view.findViewById(R.id.alertTitle);
        TextView bodyLbl = (TextView) view.findViewById(R.id.alertBody);
        Button yesButton = (Button) view.findViewById(R.id.yesBtn);
        Button noButton = (Button) view.findViewById(R.id.noBtn);

        titleLbl.setText(title);
        bodyLbl.setText(body);
        yesButton.setText(yes);
        noButton.setText(no);
        yesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tripsHistory.delete(trip);
                manager.removeView(view);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                manager.removeView(view);
            }
        });
        manager.addView(view, layoutParams);
    }
}
