package com.example.kajiankumasjid.Utilities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class WaktuSholat {
    public String sendTimeAdzan = null, sendTimeIqomah = null;

     /*
    public RequestBody Subuh() {
        if (sendTime == null) {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(PostingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    sendTime = RequestBody.create(MediaType.parse("text/plain"), dateFormat.toString());
                    String stringTime = dateFormat.toString();
                    tv_time.setText(stringTime);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(PostingActivity.this));

        } else {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(PostingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    sendTime = RequestBody.create(MediaType.parse("text/plain"), dateFormat.toString());
                    String stringTime = dateFormat.toString();
                    tv_time.setText(stringTime);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(PostingActivity.this));
        }

        return sendTime;
    }*/

    public String Adzan(Calendar calendar, int hourOfDay, int minute){
        calendar.set(0, 0, 0, hourOfDay, minute);
        CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
        sendTimeAdzan = dateFormat.toString();

        return sendTimeAdzan;
    }

    public String Iqomah(Context context){

        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(0, 0, 0, hourOfDay, minute);
                CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                sendTimeIqomah = dateFormat.toString();

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                android.text.format.DateFormat.is24HourFormat(context));

        return sendTimeIqomah;
    }

}
