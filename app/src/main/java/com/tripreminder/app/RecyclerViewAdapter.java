package com.tripreminder.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.tripreminder.app.UpcomingTrip.noteView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Trip[] data;
    private LayoutInflater inflater;
    Context context;
    TripViewModel tripViewModel;
    UpcomingTrip upcomingTrip ;


    RecyclerViewAdapter(Context context, UpcomingTrip upcomingTrip, Trip[] data) {
        this.upcomingTrip = upcomingTrip;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.upcomingtrip_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         int id = data[position].getTrip_id();
        String title = data[position].getTitle();
        String time = data[position].getTime();
        String type = data[position].getType();
        String from = data[position].getFrom();
        String to = data[position].getTo();

        holder.trip_id = id;
        holder.titleLbl.setText(title);
        holder.timeLbl.setText(time);
        holder.typeLbl.setText(type);
        holder.fromLbl.setText(from);
        holder.toLbl.setText(to);

        holder.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.inflate(R.menu.option_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.updateBtn:
                                Intent intent = new Intent(context , NewTrip.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                intent.putExtra("trip", data[position]);
                                intent.putExtra("type", "update");
                                context.startActivity(intent);
                                break;

                            case  R.id.deleteBtn:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !Settings.canDrawOverlays(context)) {
                                    upcomingTrip.chickOverlayPermission();
                                } else{
                                    showAlert(context, "Delete Trip", "Are you sure you want to delete this Trip?",
                                            "Yes", "No", data[position]);
                                }
                                break;
                        }
                        return false;

                    }
                });
                popup.show();

            }
        });

        holder.notesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpcomingTrip.noteView.setVisibility(View.VISIBLE);
                UpcomingTrip.noteLbl.setText(data[position].getNote());
            }
        });

        // for map
        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                    upcomingTrip.chickOverlayPermission();
                } else
                    displayMap(data[position].getFrom(),data[position].getTo());
                    startFloatingWidgetService(data[position].getNote());

                    if( data[position].getType().equals("One Way Trip")&& data[position].getRepetition().equals("No Repeat")){
                        data[position].setStatus(true);
                    }

                    if(data[position].getType().equals("Round Trip")){
                        String from = data[position].getFrom();
                        String to = data[position].getTo();
                        data[position].setFrom(to);
                        data[position].setTo(from);

                        String time = data[position].getRoundTime();
                        String date = data[position].getRoundDate();
                        data[position].setTime(time);
                        data[position].setDate(date);

                        data[position].setType("One Way Trip");
                    }else if(! data[position].getRepetition().equals("No Repeat")) {
                        String dateStr = data[position].getDate();
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar calendar = Calendar.getInstance();

                        switch (data[position].getRepetition()){
                            case "Repeat Daily":
                                try {
                                    Date date = format.parse(dateStr);
                                    calendar.setTime(date);
                                    calendar.add(Calendar.DATE, 1);
                                    date = calendar.getTime();
                                    dateStr = format.format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "Repeat Weekly":
                                try {
                                    Date date = format.parse(dateStr);
                                    calendar.setTime(date);
                                    calendar.add(Calendar.DATE, 7);
                                    date = calendar.getTime();
                                    dateStr = format.format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "Repeat Monthly":
                                try {
                                    Date date = format.parse(dateStr);
                                    calendar.setTime(date);
                                    calendar.add(Calendar.DATE, 30);
                                    date = calendar.getTime();
                                    dateStr = format.format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }

                        data[position].setDate(dateStr);
                    }



                    upcomingTrip.updateStatus(data[position]);

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
        TextView titleLbl, timeLbl, typeLbl, fromLbl, toLbl;
        Button menuBtn, startBtn, notesBtn;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            titleLbl = itemView.findViewById(R.id.titleLbl);
            timeLbl = itemView.findViewById(R.id.timeLbl);
            typeLbl = itemView.findViewById(R.id.typeLbl);
            fromLbl = itemView.findViewById(R.id.fromLbl);
            toLbl = itemView.findViewById(R.id.toLbl);
            menuBtn = itemView.findViewById(R.id.menuBtn);
            startBtn = itemView.findViewById(R.id.startBtn);
            notesBtn = itemView.findViewById(R.id.notesBtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, titleLbl.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
    //map
    private void displayMap(String source, String destention) {
        Uri uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin="+source+"&destination=" + destention);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    private void startFloatingWidgetService(String Notes) {
        Intent i = new Intent(context, bubbleService.class);
        i.setAction(bubbleService.ACTION_START);
        i.putExtra("Intent",Notes);
        context.startService(i);

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
                upcomingTrip.delete(trip);
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


