package org.wmii.endorfit.Adapters;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import org.wmii.endorfit.Activities.MainActivity;
import org.wmii.endorfit.Activities.MainWindowActivity;
import org.wmii.endorfit.R;


public class NotificationPublisher extends BroadcastReceiver {

    private  String CHANNEL_ID = "my_channel_01";
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[] { 500, 200, 500, 200, 500 });
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

            Intent repeating_intent = new Intent(context, MainWindowActivity.class);
            repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, MainActivity.not_id, repeating_intent, PendingIntent
                    .FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_fitness_fit_white_24dp);
                    if(!intent.getStringExtra("trainingName").equals(context.getString(R.string.SpinnerTrainingToDate)) && intent.getStringExtra("trainingName".toLowerCase()).contains("training"))
                    {
                        //TODO training date to Firebase database
                        builder.setContentTitle("Training " + intent.getStringExtra("trainingName") + " today!");

                    }
                    else if(!intent.getStringExtra("trainingName").equals(context.getString(R.string.SpinnerTrainingToDate)))
                    {
                        //TODO training date to Firebase database
                        builder.setContentTitle( intent.getStringExtra("trainingName").substring(0,1).toUpperCase() + intent.getStringExtra("trainingName").substring(1) + " today!");
                    }
                else
                {
                    builder.setContentTitle("Training today!");
                }
                    builder.setContentText("Get ready, remember about towel and water!")
                    .setAutoCancel(true)
                    .setVibrate(new long[] { 500, 200, 500, 200, 500 });

        if(intent.getAction().equals("MY_NOTIFICATION_MESSAGE"))
        {
            notificationManager.notify(MainActivity.not_id, builder.build());
        }
        MainActivity.not_id++;
//        Notification notification = intent.getParcelableExtra(NOTIFICATION);
//        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
//        notificationManager.notify(id, notification);

    }

}