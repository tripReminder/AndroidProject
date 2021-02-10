package com.tripreminder.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

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
                                trips[i].getTitle() + "trip now is its time. Do you want to start it now ?",
                                "Start", "Cancel");
                        Log.i("receiver", "alert");
                    }

                    break;
                }
            }
        }


    }

    void showAlert(Context context, String title, String body, String yes, String no) {
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
