package com.example.kajiankumasjid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

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
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.ZeeLoader;
import com.example.kajiankumasjid.ApiInterface.ApiInterface;
import com.example.kajiankumasjid.Model.GeneralResponse;
import com.example.kajiankumasjid.Model.ResponsePrayerTime;
import com.example.kajiankumasjid.Model.SholatModel;
import com.example.kajiankumasjid.Model.Times;
import com.example.kajiankumasjid.RestApi.ApiClient;
import com.example.kajiankumasjid.RestApi.ApiIslamicPrayerTimes;
import com.example.kajiankumasjid.Utilities.GlobalConfig;
import com.example.kajiankumasjid.Utilities.WaktuSholat;
import com.pixplicity.easyprefs.library.Prefs;
import com.schibstedspain.leku.LocationPickerActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class WaktuSholatActivity extends AppCompatActivity {
    private TextView tv_location, tv_subuh, tv_dhuhur, tv_ashar, tv_maghrib, tv_isya;
    private String TAG = "Sholat Activity";
    private TextView tv_adzanSubuh, tv_iqomahSubuh, tv_adzanDhuhur, tv_iqomahDhuhur, tv_adzanAshar, tv_iqomahAshar, tv_adzanMaghrib, tv_iqomahMaghrib, tv_adzanIsya, tv_iqomahIsya;
    private LocationManager locationManager;
    private String latitude, longitude, mlatitude, mlongitude;
    private String AdzanSubuh, IqomahSubuh, AdzanDhuhur, IqomahDhuhur, AdzanAshar, IqomahAshar, AdzanMaghrib, IqomahMaghrib, AdzanIsya, IqomahIsya;
    private Geocoder geocoder;
    private List<Address> addresses;
    private int MAP_BUTTON_REQUEST_CODE = 1;
    private ZeeLoader zeeLoader;
    private Toolbar toolbar;
    private Button btn_sholat;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waktu_sholat);
        tv_location = findViewById(R.id.tv_fr_Sholat_Location);
        tv_subuh = findViewById(R.id.tv_subuh);
        tv_dhuhur = findViewById(R.id.tv_dhuhur);
        tv_ashar = findViewById(R.id.tv_ashar);
        tv_maghrib = findViewById(R.id.tv_maghrib);
        tv_isya = findViewById(R.id.tv_isya);
        zeeLoader = findViewById(R.id.zeeLoader_sholat);
        tv_adzanSubuh = findViewById(R.id.tv_adzanSubuh);
        tv_iqomahSubuh = findViewById(R.id.tv_iqomahSubuh);
        tv_adzanDhuhur = findViewById(R.id.tv_adzanDhuhur);
        tv_iqomahDhuhur = findViewById(R.id.tv_iqomahDhuhur);
        tv_adzanAshar = findViewById(R.id.tv_adzanAshar);
        tv_iqomahAshar = findViewById(R.id.tv_iqomahAshar);
        tv_adzanMaghrib = findViewById(R.id.tv_adzanMaghrib);
        tv_iqomahMaghrib = findViewById(R.id.tv_iqomahMaghrib);
        tv_adzanIsya = findViewById(R.id.tv_adzanIsya);
        tv_iqomahIsya = findViewById(R.id.tv_iqomahIsya);
        toolbar = findViewById(R.id.toolbar_waktu_sholat);
        btn_sholat = findViewById(R.id.btn_waktuSholat);
        zeeLoader.setVisibility(View.VISIBLE);

        ActivityCompat.requestPermissions(WaktuSholatActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, GlobalConfig.REQUEST_CODE_LOCATION_PERMISSION);

        locationManager = (LocationManager) WaktuSholatActivity.this.getSystemService(Context.LOCATION_SERVICE);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WaktuSholatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        openMaps();

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            try {
                getLocation();
            } catch (IOException e) {
                Log.e(TAG, "Error Boss!!! "+e );
            }
        }
        getTimes();

        btn_sholat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postSholat();
            }
        });

        btnWaktuSholat();
    }

    public void btnWaktuSholat(){

        tv_adzanSubuh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdzanSubuh();
                timePickerDialog.show();
            }
        });
        tv_iqomahSubuh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IqomahSubuh();
                timePickerDialog.show();
            }
        });
        tv_adzanDhuhur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdzanDhuhur();
                timePickerDialog.show();
            }
        });
        tv_iqomahDhuhur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IqomahDhuhur();
                timePickerDialog.show();
            }
        });
        tv_adzanAshar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdzanAshar();
                timePickerDialog.show();
            }
        });
        tv_iqomahAshar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IqomahAshar();
                timePickerDialog.show();
            }
        });
        tv_adzanMaghrib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdzanMaghrib();
                timePickerDialog.show();
            }
        });
        tv_iqomahMaghrib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IqomahMaghrib();
                timePickerDialog.show();
            }
        });
        tv_adzanIsya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdzanIsya();
                timePickerDialog.show();
            }
        });
        tv_iqomahIsya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IqomahIsya();
                timePickerDialog.show();
            }
        });
    }

    public void postSholat(){
        String id_mosque = Prefs.getString(GlobalConfig.id_mosque, null);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (AdzanSubuh()==null || IqomahSubuh()==null || AdzanDhuhur()==null || IqomahDhuhur()==null || AdzanAshar()==null || IqomahAshar()==null
            || AdzanMaghrib()==null || IqomahMaghrib()==null || AdzanIsya()==null || IqomahIsya()==null){
            Toast.makeText(WaktuSholatActivity.this, "Harap Isi Semua Waktu Sholat Masjid Anda", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Call<GeneralResponse> generalResponseCall = apiInterface.postSholat(
                id_mosque,
                AdzanSubuh(),
                IqomahSubuh(),
                AdzanDhuhur(),
                IqomahDhuhur(),
                AdzanAshar(),
                IqomahAshar(),
                AdzanMaghrib(),
                IqomahMaghrib(),
                AdzanIsya(),
                IqomahIsya()
        );
        generalResponseCall.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                GeneralResponse generalResponse = response.body();
                if (generalResponse.getCode().equals("1") || generalResponse.getCode() == "1"){
                    Intent intent = new Intent(WaktuSholatActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(WaktuSholatActivity.this, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(WaktuSholatActivity.this, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Toast.makeText(WaktuSholatActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void openMaps(){
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new LocationPickerActivity.Builder()
                        .withUnnamedRoadHidden()
                        .withLegacyLayout()
                        .build(WaktuSholatActivity.this);
                intent.putExtra("test","this is test");
                startActivityForResult(intent, MAP_BUTTON_REQUEST_CODE);
            }
        });
    }

    public String AdzanSubuh(){
        if (AdzanSubuh == null) {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    AdzanSubuh = dateFormat.toString();
                    String stringTime = dateFormat.toString();
                    tv_adzanSubuh.setText(stringTime);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));

        } else {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    AdzanSubuh = dateFormat.toString();
                    tv_adzanSubuh.setText(AdzanSubuh);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));
        }
        return AdzanSubuh;
    }

    public String IqomahSubuh(){
        if (IqomahSubuh == null) {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    IqomahSubuh = dateFormat.toString();
                    tv_iqomahSubuh.setText(IqomahSubuh);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));

        } else {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    IqomahSubuh = dateFormat.toString();
                    tv_iqomahSubuh.setText(IqomahSubuh);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));
        }
        return IqomahSubuh;
    }

    public String AdzanDhuhur(){
        if (AdzanDhuhur == null) {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    AdzanDhuhur = dateFormat.toString();
                    tv_adzanDhuhur.setText(AdzanDhuhur);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));

        } else {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    AdzanDhuhur = dateFormat.toString();
                    tv_adzanDhuhur.setText(AdzanDhuhur);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));
        }
        return AdzanDhuhur;
    }

    public String IqomahDhuhur(){
        if (IqomahDhuhur == null) {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    IqomahDhuhur = dateFormat.toString();
                    tv_iqomahDhuhur.setText(IqomahDhuhur);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));

        } else {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    IqomahDhuhur = dateFormat.toString();
                    tv_iqomahDhuhur.setText(IqomahDhuhur);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));
        }
        return IqomahDhuhur;
    }

    public String AdzanAshar(){
        if (AdzanAshar == null) {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    AdzanAshar = dateFormat.toString();
                    tv_adzanAshar.setText(AdzanAshar);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));

        } else {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    AdzanAshar = dateFormat.toString();
                    tv_adzanAshar.setText(AdzanAshar);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));
        }
        return AdzanAshar;
    }

    public String IqomahAshar(){
        if (IqomahAshar == null) {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    IqomahAshar = dateFormat.toString();
                    tv_iqomahAshar.setText(IqomahAshar);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));

        } else {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    IqomahAshar = dateFormat.toString();
                    tv_iqomahAshar.setText(IqomahAshar);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));
        }
        return IqomahAshar;
    }

    public String AdzanMaghrib(){
        if (AdzanMaghrib == null) {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    AdzanMaghrib = dateFormat.toString();
                    tv_adzanMaghrib.setText(AdzanMaghrib);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));

        } else {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    AdzanMaghrib = dateFormat.toString();
                    tv_adzanMaghrib.setText(AdzanMaghrib);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));
        }
        return AdzanMaghrib;
    }

    public String IqomahMaghrib(){
        if (IqomahMaghrib == null) {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    IqomahMaghrib = dateFormat.toString();
                    tv_iqomahMaghrib.setText(IqomahMaghrib);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));

        } else {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    IqomahMaghrib = dateFormat.toString();
                    tv_iqomahMaghrib.setText(IqomahMaghrib);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));
        }
        return IqomahMaghrib;
    }

    public String AdzanIsya(){
        if (AdzanIsya == null) {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    AdzanIsya = dateFormat.toString();
                    tv_adzanIsya.setText(AdzanIsya);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));

        } else {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    AdzanIsya = dateFormat.toString();
                    tv_adzanIsya.setText(AdzanIsya);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));
        }
        return AdzanIsya;
    }

    public String IqomahIsya(){
        if (IqomahIsya == null) {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    IqomahIsya = dateFormat.toString();
                    tv_iqomahIsya.setText(IqomahIsya);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));

        } else {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(WaktuSholatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    IqomahIsya = dateFormat.toString();
                    tv_iqomahIsya.setText(IqomahIsya);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(WaktuSholatActivity.this));
        }
        return IqomahIsya;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null){
            if (requestCode==1){
                double latitude = data.getDoubleExtra(LATITUDE,0.0);
                double longitude = data.getDoubleExtra(LONGITUDE, 0.0);

                this.mlatitude = String.valueOf(latitude);
                this.mlongitude = String.valueOf(longitude);

                String alamat = data.getStringExtra(LOCATION_ADDRESS);
                tv_location.setText(alamat);
                getTimes();
            }
        }
        if (resultCode==Activity.RESULT_CANCELED){
            Log.d("RESULT****", "CANCELLED");
        }
    }

    public void getTimes(){
        ApiInterface apiInterface = ApiIslamicPrayerTimes.getClient().create(ApiInterface.class);
        //get current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateNow = formatter.format(calendar.getTime());
        Log.d("Coba", "latitude: "+mlatitude+" longtude : "+mlongitude+" date Now : "+dateNow);
        //get data
        Call<ResponsePrayerTime> responsePrayerTimeCall = apiInterface.getTimeFromFatimah(mlatitude, mlongitude, dateNow);
        responsePrayerTimeCall.enqueue(new Callback<ResponsePrayerTime>() {
            @Override
            public void onResponse(Call<ResponsePrayerTime> call, Response<ResponsePrayerTime> response) {

                Times times = response.body().getResults().getDatetime().get(0).getTimes();
                if (times==null){
                    zeeLoader.setVisibility(View.VISIBLE);
                }
                zeeLoader.setVisibility(View.GONE);
                try {

                    time(times);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponsePrayerTime> call, Throwable t) {
                zeeLoader.setVisibility(View.GONE);
            }
        });
    }

    public void time(Times times){
        zeeLoader.setVisibility(View.GONE);
        tv_subuh.setText(times.getFajr());
        tv_dhuhur.setText(times.getDhuhr());
        tv_ashar.setText(times.getAsr());
        tv_maghrib.setText(times.getMaghrib());
        tv_isya.setText(times.getIsha());
    }

    private void getLocation() throws IOException {

        //Check Permissions again
        if (ActivityCompat.checkSelfPermission(WaktuSholatActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WaktuSholatActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WaktuSholatActivity.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, GlobalConfig.REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps != null) {
                double lat = LocationGps.getLatitude();
                double longi = LocationGps.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);


            } else if (LocationNetwork != null) {
                double lat = LocationNetwork.getLatitude();
                double longi = LocationNetwork.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);


            } else if (LocationPassive != null) {
                double lat = LocationPassive.getLatitude();
                double longi = LocationPassive.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);


            } else {
                Toast.makeText(WaktuSholatActivity.this.getApplicationContext(), "Tidak Mendapatkan Lokasi Anda", Toast.LENGTH_SHORT).show();
            }

            this.mlatitude = latitude;
            this.mlongitude = longitude;

            //getData
            try {
                double lat = Double.parseDouble(latitude);
                double lang = Double.parseDouble(longitude);

                geocoder = new Geocoder(WaktuSholatActivity.this, Locale.getDefault());
                addresses = geocoder.getFromLocation(lat, lang, 1);
                String address = addresses.get(0).getLocality();
                tv_location.setText(address);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(WaktuSholatActivity.this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

}