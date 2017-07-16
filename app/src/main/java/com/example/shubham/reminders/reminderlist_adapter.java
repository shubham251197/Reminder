package com.example.shubham.reminders;

import android.content.Context;
import android.provider.CalendarContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by shubham on 22-06-2017.
 */

public class reminderlist_adapter extends ArrayAdapter {

    ArrayList<reminder> reminderArrayList;
    Context context;
    onListButtonClickedListener listener;

    void setOnListClickButtonListener(onListButtonClickedListener listener){
        this.listener=listener;
    }

    public reminderlist_adapter(@NonNull Context context, ArrayList<reminder> reminderArrayList) {
        super(context, 0);
        this.context=context;
        this.reminderArrayList=reminderArrayList;
    }


    @Override
    public int getCount() {
        return reminderArrayList.size();
    }


    static  class reminderViewholder{

        TextView reminder,date,time;
        Button edit_button;
        reminderViewholder(TextView r,TextView d,TextView t,Button b){
            this.reminder=r;
            this.date=d;
            this.time=t;
            this.edit_button=b;
        }
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

       if(convertView==null) {
           convertView = LayoutInflater.from(context).inflate(R.layout.list_item_layout, null);
           TextView remindertext = (TextView) convertView.findViewById(R.id.reminder_text);
           TextView reminderdate = (TextView) convertView.findViewById(R.id.date_text);
           TextView remindertime = (TextView) convertView.findViewById(R.id.time_text);
           Button edit_button= (Button) convertView.findViewById(R.id.edit_button);
           reminderViewholder remindVH = new reminderViewholder(remindertext, reminderdate, remindertime,edit_button);
           convertView.setTag(remindVH);
       }
        final int pos=position;
        reminder r=reminderArrayList.get(position);
        reminderViewholder remindVH= (reminderViewholder)convertView.getTag();
        remindVH.reminder.setText(r.reminder);
        remindVH.date.setText(r.date);
        remindVH.time.setText(r.time);
        remindVH.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.listButtonClicked(v,pos);
            }
        });
        return convertView;
    }
}



interface onListButtonClickedListener{

    void listButtonClicked(View v,int pos);
}