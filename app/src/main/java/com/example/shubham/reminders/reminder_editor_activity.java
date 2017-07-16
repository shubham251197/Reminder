package com.example.shubham.reminders;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class reminder_editor_activity extends AppCompatActivity {

    static int j=0;
    String str;
    EditText reminder_edit, date_edit, time_edit,detail_edit;
    Button submit,title_voice,detail_voice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_editor_activity);
        reminder_edit = (EditText) findViewById(R.id.reminder_edit_text);
        date_edit = (EditText) findViewById(R.id.reminder_edit_date);
        time_edit = (EditText) findViewById(R.id.reminder_edit_time);
        detail_edit= (EditText) findViewById(R.id.reminder_edit_desccription);
        submit = (Button) findViewById(R.id.submit_button);
        title_voice=(Button) findViewById(R.id.title_voice);
        detail_voice=(Button) findViewById(R.id.details_voice);
        Intent i = getIntent();

        str = i.getStringExtra(intent_constants.REMINDER_TEXT);
        String obj[] = str.split(";");

        final int choice = Integer.parseInt(obj[4]);
        if (choice == 2) {
            reminder_edit.setText(obj[0]);
            date_edit.setText(obj[1]);
            time_edit.setText(obj[2]);
            detail_edit.setText(obj[3]);
            submit.setText("UPDATE");
        } else if (choice == 1)
            submit.setText("ADD");



        final int position=Integer.parseInt(obj[5]);


        time_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(reminder_editor_activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        time_edit.setText(i + ":" + i1);

                    }
                }, hour, min, true);
                timePickerDialog.show();
            }
        });


        date_edit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                int date=c.get(Calendar.DAY_OF_MONTH);
                showdatepicker(reminder_editor_activity.this, year, month, date);

            }
        });



        title_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRANCE);
                try{
                    startActivityForResult(i,90);
                }catch (ActivityNotFoundException a){
                    //
                }
            }
        });

        detail_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRANCE);
                try{
                    startActivityForResult(i,80);
                }catch (ActivityNotFoundException a){
                    //
                }
            }
        });

        final String[] oldtime = {obj[2]};
        // final String finalStr = str[1];
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //       String edited_reminder= reminder_edit.getText().toString();
                //edited_reminder=edited_reminder+";"+ finalStr;

                if (reminder_edit.getText().toString().isEmpty() || date_edit.getText().toString().isEmpty() || time_edit.getText().toString().isEmpty()) {
                    Snackbar.make(v, "TITLE,DATE AND TIME NOT SPECIFIED !!", Snackbar.LENGTH_SHORT).show();
                } else {
                    String str1[], str2[];
                    str1 = date_edit.getText().toString().split("-");
                    str2 = time_edit.getText().toString().split(":");
                    Calendar c = Calendar.getInstance();
                    c.set(Integer.parseInt(str1[2]), Integer.parseInt(str1[1]), Integer.parseInt(str1[0]), Integer.parseInt(str2[0]), Integer.parseInt(str2[1]));
                    Long epoc_time = c.getTime().getTime();

                    if (epoc_time <= System.currentTimeMillis())
                        Snackbar.make(v, "DATE AND TIME INVALID !! ", Snackbar.LENGTH_SHORT).show();
                    else {

                        reminderOpenHelper ROH = new reminderOpenHelper(reminder_editor_activity.this);
                        SQLiteDatabase database = ROH.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put(reminderOpenHelper.COLUMN_TITLE, reminder_edit.getText().toString());
                        cv.put(reminderOpenHelper.COLUMN_DATE, date_edit.getText().toString());
                        cv.put(reminderOpenHelper.COLUMN_TIME, time_edit.getText().toString());
                        cv.put(reminderOpenHelper.COLUMN_DETAILS, detail_edit.getText().toString());

                        if (choice == 2)
                            database.update(reminderOpenHelper.TABLE_NAME, cv, reminderOpenHelper.COLUMN_TIME + " = ?", oldtime);
                        else
                            database.insert(reminderOpenHelper.TABLE_NAME, null, cv);


                        Intent i = new Intent();
                        i.putExtra("position", position);
                        //     i.putExtra(intent_constants.REMINDER_TEXT,edited_reminder);
                        setResult(RESULT_OK, i);
                        finish();


                    }
                }
            }
        });

    }
    public void showdatepicker(Context context, int initialYear, int initialMonth, int initialDay ){


        DatePickerDialog datePickerDialog= new DatePickerDialog(context,( new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar c= Calendar.getInstance();
                c.set(year,month,dayOfMonth);
                date_edit.setText(dayOfMonth+"-"+month+"-"+year);
            }
        }),initialYear,initialMonth,initialDay);

        datePickerDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==90)
        {
            if(resultCode==RESULT_OK && data !=null){
                ArrayList<String> str=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                reminder_edit.setText(str.get(0));
            }

        }
        if(requestCode==80)
        {
            if(resultCode==RESULT_OK && data !=null){
                ArrayList<String> str=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                detail_edit.setText(str.get(0));
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
