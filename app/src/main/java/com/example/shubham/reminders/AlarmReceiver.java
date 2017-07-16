package com.example.shubham.reminders;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.




        String str[]=intent.getStringExtra(intent_constants.ALARM_INTENT).split(";");
        NotificationCompat.Builder nbuilder= new NotificationCompat.Builder(context);

        nbuilder.setContentTitle(str[0]);
        nbuilder.setContentText(str[1]);
        nbuilder.setAutoCancel(true);
        nbuilder.setSmallIcon(R.mipmap.reminder_icon);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Intent resultIntent=new Intent(context,display_reminder.class);
        resultIntent.putExtra(intent_constants.DISPLAY_INTENT,str[0]+";"+str[1]+";"+str[3]+";"+str[4]+";"+str[2]);
        PendingIntent resultPending= PendingIntent.getActivity(context,Integer.parseInt(str[2]),resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        nbuilder.setContentIntent(resultPending);

        notificationManager.notify(Integer.parseInt(str[2]),nbuilder.build());



        reminderOpenHelper ROH = new reminderOpenHelper(context);
        final SQLiteDatabase database = ROH.getWritableDatabase();
        String obj[] = {str[2]};
        database.delete(reminderOpenHelper.TABLE_NAME, reminderOpenHelper.COLUMN_ID + " = ?", obj);

        Uri ringtone= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        final Ringtone r=RingtoneManager.getRingtone(context,ringtone);
        r.play();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                r.stop();
            }
        },6000);

    }
}
