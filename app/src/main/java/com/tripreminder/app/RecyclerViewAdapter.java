package com.tripreminder.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
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

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Trip[] data;
    private LayoutInflater inflater;
    Context context;
    TripViewModel tripViewModel;


    RecyclerViewAdapter(Context context, TripViewModel tripViewModel, Trip[] data) {
        this.tripViewModel = tripViewModel;
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
                                Trip trip = data[position];
                                Intent intent = new Intent(context , NewTrip.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                intent.putExtra("trip", trip);
                                intent.putExtra("type", "update");
                                context.startActivity(intent);
                                break;

                            case  R.id.deleteBtn:
                                tripViewModel.delete(data[position]);

//                                new AlertDialog.Builder(context)
//                                        .setTitle("Delete Trip")
//                                        .setMessage("Are you sure you want to delete this Trip?")
//                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                tripViewModel.delete(data[position]);
//                                            }
//                                        })
//                                        .setNegativeButton(android.R.string.no, null)
//                                        .setIcon(android.R.drawable.ic_dialog_alert)
//                                        .show();
                                break;
                        }
                        return false;

                    }
                });
                popup.show();

            }
        });


        // for map
        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMap(30.0179962,31.4174193);
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
    private void displayMap(double EndLatitute,double EndLongtitue)
    {
        Uri uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=&destination"+ EndLatitute + ","+ EndLongtitue);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }



}
