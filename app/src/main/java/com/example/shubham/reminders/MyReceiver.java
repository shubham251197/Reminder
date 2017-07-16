package com.example.shubham.reminders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
   // static int i=1;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)){
//            if((i==2)) {
//                Toast.makeText(context, "CALL DISCONNECTED", Toast.LENGTH_SHORT).show();
        if(intent.getAction().equals("android.intent.action.AIRPLANE_MODE")){
                Intent in=new Intent(context,MainActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
        }
//                i =1;
//            }
//        }
//        else if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
//            Toast.makeText(context,"CALL COMING",Toast.LENGTH_SHORT).show();
//            i=2;
//        }
//        else if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
//            Toast.makeText(context,"CALL RECEIVED",Toast.LENGTH_SHORT).show();
//            i=2;
//        }



    }
}
