package com.example.kajiankumasjid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.kajiankumasjid.Fragment.BerandaFragment;
import com.example.kajiankumasjid.Fragment.PostingFragment;
import com.example.kajiankumasjid.Fragment.SholatFragment;
import com.example.kajiankumasjid.Utilities.GlobalConfig;
import com.google.android.material.navigation.NavigationView;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String kirim_fragment = "fragment";
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView tv_email;
    private CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //isi data navigation drawer
        ViewNavDrawerCard(navigationView);

       if (savedInstanceState == null){
            ActionBar actionBar = getSupportActionBar();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new BerandaFragment()).commit();
            actionBar.setTitle("Beranda");
        }
    }

    public void ViewNavDrawerCard(NavigationView navigationView){
        View ViewNav = navigationView.getHeaderView(0);
        tv_email = ViewNav.findViewById(R.id.tv_email);
        circleImageView = ViewNav.findViewById(R.id.img_profile);
        tv_email.setText(Prefs.getString(GlobalConfig.email, null));

        String urlImg = GlobalConfig.profileIMG_url + Prefs.getString(GlobalConfig.img_profile,null);
        Picasso.with(this)
                .load(urlImg)
                .into(circleImageView);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ActionBar actionBar = getSupportActionBar();
        switch (item.getItemId()){
            case R.id.nav_beranda:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BerandaFragment()).commit();
                actionBar.setTitle("Beranda");
                break;
            case R.id.nav_posting:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PostingFragment()).commit();
                actionBar.setTitle("Postinganku");
                break;
            case R.id.nav_sholat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SholatFragment()).commit();
                actionBar.setTitle("Waktu Sholat");
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else{
            super.onBackPressed();
        }
    }
}