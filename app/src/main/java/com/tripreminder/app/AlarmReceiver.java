package com.tripreminder.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.LocaleData;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    UpcomingTrip upcomingTrip = new UpcomingTrip();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!Settings.canDrawOverlays(context)) {
                askPermission(context);
            }

            
            String localTime = LocalTime.now().getHour() + ":" + LocalTime.now().getMinute();
            String localDate = LocalDate.now().getDayOfMonth() + "/" + (LocalDate.now().getMonthValue()) + "/" + LocalDate.now().getYear();

            Trip[] trips = UpcomingTrip.data;
            if(trips.length == 0){
                SharedPreferences sharedPreferences = context.getSharedPreferences("receiver", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("data", "");
                Log.i("receiver", json);
                Type type = new TypeToken<Trip[]>(){}.getType();
                trips = gson.fromJson(json, type);
            }

            for (int i = 0; i < trips.length; i++) {
                String date = trips[i].getDate();
                String time = trips[i].getTime();
                Log.i("tag",localTime +"&"+time);
                Log.i("tag",localDate +"&"+date);

                if(localDate.equals(date) && localTime.equals(time)){
                    if (!Settings.canDrawOverlays(context)) {
                        upcomingTrip.chickOverlayPermission();
                    } else{
                        showAlert(context, trips[i].getTitle(),
                                trips[i].getTitle() + " trip now is its time. Do you want to start it now ?",
                                "Start", "Cancel", trips[i]);
                        Log.i("receiver", "alert");
                    }

                    break;
                }
            }
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
                manager.removeView(view);

                displayMap(trip.getFrom(),trip.getTo(), context);
                startFloatingWidgetService(trip.getNote(), context);

                if( trip.getType().equals("One Way Trip")&& trip.getRepetition().equals("No Repeat")){
                    trip.setStatus(true);
                }

                if(trip.getType().equals("Round Trip")){
                    String from = trip.getFrom();
                    String to = trip.getTo();
                    trip.setFrom(to);
                    trip.setTo(from);

                    String time = trip.getRoundTime();
                    String date = trip.getRoundDate();
                    trip.setTime(time);
                    trip.setDate(date);

                    trip.setType("One Way Trip");
                }else if(! trip.getRepetition().equals("No Repeat")) {
                    String dateStr = trip.getDate();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar calendar = Calendar.getInstance();

                    switch (trip.getRepetition()){
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

                    trip.setDate(dateStr);
                }

                ((UpcomingTrip)context).updateStatus(trip);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                manager.removeView(view);
            }
        });
        manager.addView(view, layoutParams);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(context, notification);
        ringtone.play();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }

    private void displayMap(String source, String destention, Context context) {
        Uri uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin="+source+"&destination=" + destention);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
        context.startActivity(intent);
    }
}

    private void startFloatingWidgetService(String Notes, Context context) {
        Intent i = new Intent(context, bubbleService.class);
        i.setAction(bubbleService.ACTION_START);
        i.putExtra("Intent",Notes);
        context.startService(i);

    }

    public void askPermission(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Permission")
                .setMessage("You must let the App to display content overlay the other Apps to can use all its function")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
                        ((Activity) context).startActivityForResult(intent, 0);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
