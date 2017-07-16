package com.example.shubham.reminders;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Calendar;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements onListButtonClickedListener {



    ListView reminderList;
    ArrayList<reminder> remind_arr;
    reminderlist_adapter remind_adap;
    boolean undone =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        reminderList = (ListView) findViewById(R.id.Reminderlist);
        remind_arr = new ArrayList<>();
//       for(int i=0;i<10;i++) {
//        //   remind_arr.add(" Reminder " + (i + 1));
//
//      reminder r= new reminder();
//           r.reminder="REMINDER " + (i+1);
//           r.date= (i+1)+"-06-2017";
//           r.time=(i+1)+":30 PM";
//
//            remind_arr.add(r);
//       }

        remind_adap = new reminderlist_adapter(this, remind_arr);
        remind_adap.setOnListClickButtonListener(this);

        reminderList.setAdapter(remind_adap);
        reminderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //    Toast.makeText(MainActivity.this,remind_arr.get(position).reminder+ " clicked",Toast.LENGTH_SHORT).show();

//                Intent i=new Intent(MainActivity.this,reminder_editor_activity.class);
//                i.putExtra(intent_constants.REMINDER_TEXT,remind_arr.get(position).reminder+";"+remind_arr.get(position).date+";"+remind_arr.get(position).time);
//                startActivityForResult(i,1);

                Intent i=new Intent(MainActivity.this,display_reminder.class);
                i.putExtra(intent_constants.DISPLAY_INTENT,remind_arr.get(position).reminder+";"+remind_arr.get(position).details+";"+remind_arr.get(position).date+";"+remind_arr.get(position).time);
                startActivity(i);

            }
        });

        reminderList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("DELETE");
                builder.setMessage("Do want to delete this reminder ? ");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        remind_arr.remove(position);
//                        remind_adap.notifyDataSetChanged();

                        final String date,time,title,details;
                        title=remind_arr.get(position).reminder.toString();
                        date=remind_arr.get(position).date;
                        time=remind_arr.get(position).time;
                        details=remind_arr.get(position).details;


                        reminderOpenHelper ROH = new reminderOpenHelper(MainActivity.this);
                        final SQLiteDatabase database = ROH.getWritableDatabase();
                        String id = String.valueOf(remind_arr.get(position).id);
                        final String str[] = {id};
                        final int cancel_id=remind_arr.get(position).id;
                        remind_arr.remove(position);
                        remind_adap.notifyDataSetChanged();
                        undone=false;

                        Snackbar snack= Snackbar.make(view,"REMINDER DELETED",Snackbar.LENGTH_SHORT).setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reminderUpdateList();
                                undone=true;
                            }
                        });
                           snack.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                               @Override
                               public void onViewAttachedToWindow(View view) {

                               }

                               @Override
                               public void onViewDetachedFromWindow(View view) {
                                   if(!undone) {
                                       database.delete(reminderOpenHelper.TABLE_NAME, reminderOpenHelper.COLUMN_ID + " = ?", str);
                                       AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                                       Intent i = new Intent(MainActivity.this, AlarmReceiver.class);
                                       PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, cancel_id, i, PendingIntent.FLAG_UPDATE_CURRENT);
                                       alarm.cancel(pi);
                                   }
                               }
                           });
                        snack.show();

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


                return true;
            }
        });


        reminderUpdateList();

        final  FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(MainActivity.this,reminder_editor_activity.class);
                i.putExtra(intent_constants.REMINDER_TEXT," ; ; ; ;1;"+remind_arr.size());
                startActivityForResult(i,1);
            }
        });
    }


    void reminderUpdateList() {



        reminderOpenHelper ROH = new reminderOpenHelper(this);
        remind_arr.clear();
        SQLiteDatabase database = ROH.getReadableDatabase();
        Cursor cursor = database.query(reminderOpenHelper.TABLE_NAME, null, null, null, null, null,null);
        while (cursor.moveToNext()) {

            reminder r = new reminder();
            r.reminder = cursor.getString(cursor.getColumnIndex(reminderOpenHelper.COLUMN_TITLE));
            r.date = cursor.getString(cursor.getColumnIndex(reminderOpenHelper.COLUMN_DATE));
            r.time = cursor.getString(cursor.getColumnIndex(reminderOpenHelper.COLUMN_TIME));
            r.id = cursor.getInt(cursor.getColumnIndex(reminderOpenHelper.COLUMN_ID));
            r.details=cursor.getString(cursor.getColumnIndex(reminderOpenHelper.COLUMN_DETAILS));

            remind_arr.add(r);
        }
        remind_adap.notifyDataSetChanged();


//
//        if(remind_arr.isEmpty()) {
//            View view = this.getWindow().getDecorView();
//            view.setBackgroundResource(R.drawable.no_reminder);
//        }
//        else
//        {
//            View view = this.getWindow().getDecorView();
//            view.setBackgroundResource(R.color.beige);
//        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    EditText t1, t2, t3, t4;
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//        if (id == R.id.add) {
//
//                    Intent i=new Intent(MainActivity.this,reminder_editor_activity.class);
//            i.putExtra(intent_constants.REMINDER_TEXT," ; ; ;1");
//            startActivityForResult(i,1);


//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            builder.setTitle("ADD");
//            builder.setCancelable(false);
//            final View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
//            builder.setView(view);


//            builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//
//                    t1 = (EditText) view.findViewById(R.id.newreminder);
//                    // t2=(EditText) view.findViewById(R.id.newindex);
//                    t3 = (EditText) view.findViewById(R.id.newdate);
//                    t4 = (EditText) view.findViewById(R.id.newtime);
//
//                    t4.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Calendar c= Calendar.getInstance();
//                            int hour=c.get(Calendar.HOUR_OF_DAY);
//                            int min=c.get(Calendar.MINUTE);
//
//
//                            TimePickerDialog timePickerDialog= new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                                @Override
//                                public void onTimeSet(TimePicker timePicker, int i, int i1) {
//
//                                    t4.setText(i+":"+i1);
//
//                                }
//                            },hour,min,true);
//                            timePickerDialog.show();
//                        }
//                    });
//
//                    t3.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            Calendar c = Calendar.getInstance();
//                            int month = c.get(Calendar.MONTH);
//                            int year = c.get(Calendar.YEAR);
//                            showdatepicker(MainActivity.this, year, month, 1);
//                        }
//                    });


//                    reminderOpenHelper ROH = new reminderOpenHelper(MainActivity.this);
//                    SQLiteDatabase database = ROH.getWritableDatabase();
//
//                    ContentValues cv = new ContentValues();
//
//                    cv.put(reminderOpenHelper.COLUMN_TITLE, t1.getText().toString());
//                    cv.put(reminderOpenHelper.COLUMN_DATE, t3.getText().toString());
//                    cv.put(reminderOpenHelper.COLUMN_TIME, t4.getText().toString());
//
//                    database.insert(reminderOpenHelper.TABLE_NAME, null, cv);
//
//                    reminderUpdateList();
//
//                }
//            });
//            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//
//
//            AlertDialog dialog = builder.create();
//            dialog.show();

//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
//                String newTitle = data.getStringExtra(intent_constants.REMINDER_TEXT);
//                String str[]=newTitle.split(";");
//                int pos=Integer.parseInt(str[1]);
//                reminder r= new reminder();
//                r.reminder=str[0];
//                r.date=remind_arr.get(pos).date;
//                r.time=remind_arr.get(pos).time;
//                remind_arr.remove(pos);
//                remind_arr.add(pos,r);
//                remind_adap.notifyDataSetChanged();


                int position=data.getIntExtra("position",0);

                reminderUpdateList();

                AlarmManager am= (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
                Intent alarm_i= new Intent(MainActivity.this,AlarmReceiver.class);
                alarm_i.putExtra(intent_constants.ALARM_INTENT,remind_arr.get(position).reminder+";"+ remind_arr.get(position).details+";"+remind_arr.get(position).id+";"+remind_arr.get(position).date+";"+remind_arr.get(position).time);
                PendingIntent pendingIntent= PendingIntent.getBroadcast(MainActivity.this,remind_arr.get(position).id,alarm_i,PendingIntent.FLAG_UPDATE_CURRENT);


                Calendar c= Calendar.getInstance();

                String str1[],str2[];
                str1=remind_arr.get(position).date.split("-");
                str2=remind_arr.get(position).time.split(":");

                c.set(Integer.parseInt(str1[2]),Integer.parseInt(str1[1]),Integer.parseInt(str1[0]),Integer.parseInt(str2[0]),Integer.parseInt(str2[1]));
                Long epoc_time=c.getTime().getTime();

                am.set(AlarmManager.RTC_WAKEUP,epoc_time,pendingIntent);

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void listButtonClicked(View v, int pos) {

        //Toast.makeText(this," BUTTON " +pos +" CLICKED ",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MainActivity.this, reminder_editor_activity.class);
        i.putExtra(intent_constants.REMINDER_TEXT, remind_arr.get(pos).reminder + ";" + remind_arr.get(pos).date + ";" + remind_arr.get(pos).time+";"+remind_arr.get(pos).details+";2;"+pos);
        startActivityForResult(i, 1);

    }




}