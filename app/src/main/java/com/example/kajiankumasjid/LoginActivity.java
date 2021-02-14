package com.example.kajiankumasjid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.ZeeLoader;
import com.example.kajiankumasjid.ApiInterface.ApiInterface;
import com.example.kajiankumasjid.Model.LoginModel;
import com.example.kajiankumasjid.RestApi.ApiClient;
import com.example.kajiankumasjid.Utilities.GlobalConfig;
import com.facebook.stetho.Stetho;
import com.pixplicity.easyprefs.library.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private ZeeLoader zeeLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Stetho.initializeWithDefaults(this);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btn_Login);
        zeeLoader = findViewById(R.id.zeeLoader_login);
        zeeLoader.setVisibility(View.GONE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doLogin();
            }
        });
    }

    public void doLogin(){
        zeeLoader.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String email = edtEmail.getText().toString().trim();
        String password =  edtPassword.getText().toString().trim();

        Call<LoginModel> loginModelCall = apiInterface.login_request(
                "fatimah@gmail.com",
                "fatimah"
        );

        loginModelCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                zeeLoader.setVisibility(View.VISIBLE);
                if (response.isSuccessful()){
                    String error_code = response.body().getCode();
                    String message = response.body().getMessage();
                    if (error_code.equals("1")){

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        Prefs.putString(GlobalConfig.id_mosque, response.body().getUserModel().getId_mosque());
                        Prefs.putString(GlobalConfig.email, response.body().getUserModel().getEmail());
                        Prefs.putString(GlobalConfig.password, response.body().getUserModel().getPassword());
                        Prefs.putString(GlobalConfig.name_mosque, response.body().getUserModel().getName_mosque());
                        Prefs.putString(GlobalConfig.phone_number, response.body().getUserModel().getPhone_number());
                        Prefs.putString(GlobalConfig.address, response.body().getUserModel().getAddress());
                        Prefs.putString(GlobalConfig.latitude, response.body().getUserModel().getLatitude());
                        Prefs.putString(GlobalConfig.longitude, response.body().getUserModel().getLongitude());
                        Prefs.putString(GlobalConfig.img_profile, response.body().getUserModel().getImg_profile());
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }else if(error_code.equals("2")){
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                    }else if (error_code.equals("0")){
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Tidak Ada Koneksi", Toast.LENGTH_SHORT).show();
                zeeLoader.setVisibility(View.GONE);
            }
        });

    }
}