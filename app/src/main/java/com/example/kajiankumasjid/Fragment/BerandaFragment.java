package com.example.kajiankumasjid.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.ZeeLoader;
import com.example.kajiankumasjid.Adapter.BerandaAdapter;
import com.example.kajiankumasjid.ApiInterface.ApiInterface;
import com.example.kajiankumasjid.MainActivity;
import com.example.kajiankumasjid.Model.PostingModel;
import com.example.kajiankumasjid.PostingActivity;
import com.example.kajiankumasjid.R;
import com.example.kajiankumasjid.RestApi.ApiClient;
import com.example.kajiankumasjid.Utilities.GlobalConfig;
import com.google.android.material.appbar.MaterialToolbar;
import com.schibstedspain.leku.LocationPickerActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;


public class BerandaFragment extends Fragment implements DialogDistanceFragment.OnInputDistanceSelected {
    private static final String TAG = "Beranda Fragment";
    private RecyclerView recyclerView;
    private ArrayList<PostingModel> postingModels;
    private BerandaAdapter berandaAdapter;
    private Button btn_postingKajian;
    private ImageView img_onfailure;
    private LocationManager locationManager;
    private TextView tv_onfailure, tv_Berandalocation, tv_jarak ;
    private int MAP_BUTTON_REQUEST_CODE = 1;
    private String latitude, longitude, mLatitude, mLongitude;
    private List<Address> addresses;
    private String getJarak = "3";
    private DialogDistanceFragment.OnInputDistanceSelected onInputDistanceSelected;
    private ZeeLoader zeeLoader;
    private Geocoder geocoder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        onInputDistanceSelected = this::SendDistance;
        btn_postingKajian = view.findViewById(R.id.btn_posting);
        recyclerView = view.findViewById(R.id.rc_viewBeranda);
        img_onfailure = view.findViewById(R.id.img_kajian_null);
        tv_onfailure = view.findViewById(R.id.tv_onfailure_kajian);
        tv_Berandalocation = view.findViewById(R.id.tv_Berandalocation);
        tv_jarak = view.findViewById(R.id.tv_jarakBeranda);
        zeeLoader = view.findViewById(R.id.zeeLoader_fr_kajian);
        tv_onfailure.setVisibility(View.GONE);
        img_onfailure.setVisibility(View.GONE);
        recyclerView.setNestedScrollingEnabled(false);

        ActivityCompat.requestPermissions(getActivity(), new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, GlobalConfig.REQUEST_CODE_LOCATION_PERMISSION);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        openMap();

        btn_postingKajian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), PostingActivity.class);
                startActivity(intent);
            }
        });

        if (tv_Berandalocation.isPressed()){

        }else{
            tv_jarak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogDistanceFragment dialogDistanceFragment = new DialogDistanceFragment();
                    dialogDistanceFragment.setTargetFragment(BerandaFragment.this,1);
                    dialogDistanceFragment.show(getActivity().getSupportFragmentManager(), "dialog jarak");
                }
            });
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS();
            } else {
                try {
                    getLocation(getJarak);
                } catch (IOException e) {
                    Log.e(TAG, "Error Boss!!! "+e );
                }
            }
        }

        return view;
    }

    public void openMap(){
        tv_Berandalocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new LocationPickerActivity.Builder()
                        .withUnnamedRoadHidden()
                        .withLegacyLayout()
                        .build(getActivity());
                intent.putExtra("test","this is test");
                startActivityForResult(intent, MAP_BUTTON_REQUEST_CODE);
                if (tv_jarak.isPressed()){
                    startActivityForResult(intent, MAP_BUTTON_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null){
            if (requestCode==1){
                double latitude = data.getDoubleExtra(LATITUDE,0.0);
                double longitude = data.getDoubleExtra(LONGITUDE, 0.0);

                this.mLatitude = String.valueOf(latitude);
                this.mLongitude = String.valueOf(longitude);

                String alamat = data.getStringExtra(LOCATION_ADDRESS);

                tv_Berandalocation.setText(alamat);
                getData(String.valueOf(latitude),String.valueOf(longitude),getJarak);

            }
        }
        if (resultCode==Activity.RESULT_CANCELED){
            Log.d("RESULT****", "CANCELLED");
        }
    }

    public void getData(String latitude, String longitude, String distance){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<PostingModel>> postingModelCall = apiInterface.get_kajian_Default(
                latitude,
                longitude,
                distance
        );
        postingModelCall.enqueue(new Callback<ArrayList<PostingModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PostingModel>> call, Response<ArrayList<PostingModel>> response) {
                postingModels = response.body();
                img_onfailure.setVisibility(View.GONE);
                tv_onfailure.setVisibility(View.GONE);
                zeeLoader.setVisibility(View.GONE);
                berandaAdapter = new BerandaAdapter(postingModels, getContext());
                berandaAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(berandaAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<PostingModel>> call, Throwable t) {
                img_onfailure.setVisibility(View.VISIBLE);
                tv_onfailure.setVisibility(View.VISIBLE);
                zeeLoader.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public String SendDistance(String distance) {
        tv_jarak.setText(distance+" KM");
        this.getJarak = distance;
        if (mLatitude == null){
            getData(latitude,longitude,distance);

        }else{
            getData(mLatitude,mLongitude,distance);

        }
        return distance;
    }

    private void getLocation(String radiusJarak) throws IOException {

        //Check Permissions again
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),

                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
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
                Toast.makeText(getContext().getApplicationContext(), "Tidak Mendapatkan Lokasi Anda", Toast.LENGTH_SHORT).show();
            }

            //getData
            try {
                double lat = Double.parseDouble(latitude);
                double lang = Double.parseDouble(longitude);

                geocoder = new Geocoder(getContext(), Locale.getDefault());
                addresses = geocoder.getFromLocation(lat, lang, 1);
                String address = addresses.get(0).getLocality();
                tv_Berandalocation.setText(address);
                getData(latitude, longitude, radiusJarak);
            } catch (Exception e) {
                e.printStackTrace();
            }

            tv_jarak.setText(radiusJarak+" KM");

        }
    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(getContext());

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