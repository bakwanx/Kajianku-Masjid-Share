package com.example.kajiankumasjid.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.ZeeLoader;
import com.example.kajiankumasjid.ApiInterface.ApiInterface;
import com.example.kajiankumasjid.Model.ResponsePrayerTime;
import com.example.kajiankumasjid.Model.Times;
import com.example.kajiankumasjid.PostingActivity;
import com.example.kajiankumasjid.R;
import com.example.kajiankumasjid.RestApi.ApiIslamicPrayerTimes;
import com.example.kajiankumasjid.Utilities.GlobalConfig;
import com.example.kajiankumasjid.WaktuSholatActivity;
import com.pixplicity.easyprefs.library.Prefs;
import com.schibstedspain.leku.LocationPickerActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

public class SholatFragment extends Fragment {
    private TextView tv_masjid;
    private Button btn_sholat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sholat, container, false);
        btn_sholat = view.findViewById(R.id.btn_sholat);
        tv_masjid = view.findViewById(R.id.tv_Fr_masjid);

        tv_masjid.setText(" "+Prefs.getString(GlobalConfig.name_mosque, null));
        btn_sholat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), WaktuSholatActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }




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


}