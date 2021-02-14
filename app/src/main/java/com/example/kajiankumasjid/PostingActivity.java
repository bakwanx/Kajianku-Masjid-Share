package com.example.kajiankumasjid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.ZeeLoader;
import com.example.kajiankumasjid.ApiInterface.ApiInterface;
import com.example.kajiankumasjid.Fragment.PostingFragment;
import com.example.kajiankumasjid.Model.GeneralResponse;
import com.example.kajiankumasjid.Model.PostingModel;
import com.example.kajiankumasjid.RestApi.ApiClient;
import com.example.kajiankumasjid.Utilities.GlobalConfig;

import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostingActivity extends AppCompatActivity {

    public static final String kirim_data = "kirim";
    String TAG = "MainActivity";
    private Toolbar toolbar;
    private ImageView img_poster;
    private TextView tv_date, tv_time, tv_endTime;
    private EditText edt_tema, edt_desc;
    private Button btn_posting;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat dateFormat, dateFormatShow;
    private String imagePath;
    private GeneralResponse generalResponse;
    private RequestBody sendDate, sendTime, sendEndTime;
    private static final int REQUEST_IMAGE_GET = 100;
    private static final int PERMISSION_STORAGE = 2;
    private PostingModel postingModel;
    private ZeeLoader zeeLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        toolbar = findViewById(R.id.toolbar_posting);
        img_poster = findViewById(R.id.img_upload);
        edt_tema = findViewById(R.id.editTextTema);
        tv_date = findViewById(R.id.tvDate);
        tv_time = findViewById(R.id.tvTime);
        tv_endTime = findViewById(R.id.tvTimeEnd);
        edt_desc = findViewById(R.id.editTextDescription);
        btn_posting = findViewById(R.id.btn_posting);
        zeeLoader = findViewById(R.id.zeeLoader_activity_posting);
        zeeLoader.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostingActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.kirim_fragment, "PostingFragment");
                startActivity(intent);
            }
        });

        postingModel = getIntent().getParcelableExtra(kirim_data);
        if (postingModel != null){
            Picasso.with(PostingActivity.this)
                    .load(GlobalConfig.img_poster + postingModel.getImage())
                    .into(img_poster);
            edt_tema.setText(postingModel.getTitle());
            tv_date.setText(postingModel.getEvent_date());
            tv_time.setText(postingModel.getEvent_time());
            tv_endTime.setText(postingModel.getEnd_event_time());
            edt_desc.setText(postingModel.getDescription());
            Picasso.with(PostingActivity.this)
                    .load(GlobalConfig.img_poster + postingModel.getImage())
                    .placeholder(R.drawable.bg_imgupload)
                    .into(img_poster);
            btn_posting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateData();
                }
            });

        }else{
            btn_posting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postData();
                }
            });

        }

        img_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(PostingActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(PostingActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PostingActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_STORAGE);
                } else {
                    pickPoster();
                }

            }
        });

        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime();
                timePickerDialog.show();
            }
        });

        tv_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEndTime();
                timePickerDialog.show();
            }
        });

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDate();
                datePickerDialog.show();
            }
        });


    }


    public RequestBody getTime() {
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
    }


    public RequestBody getDate() {
        if (sendDate == null) {
            Calendar calendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(PostingActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    dateFormatShow = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    String stringDate = dateFormat.format(newDate.getTime());
                    sendDate = RequestBody.create(MediaType.parse("text/plain"), stringDate);
                    String getDateShow = dateFormatShow.format(newDate.getTime());
                    tv_date.setText(getDateShow);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        } else {
            Calendar calendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(PostingActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    dateFormatShow = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    String stringDate = dateFormat.format(newDate.getTime());
                    sendDate = RequestBody.create(MediaType.parse("text/plain"), stringDate);
                    String getDateShow = dateFormatShow.format(newDate.getTime());
                    tv_date.setText(getDateShow);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }

        return sendDate;
    }

    public void pickPoster() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_IMAGE_GET);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickPoster();
                }

                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri selectImageUri = data.getData();
            imagePath = getRealPathFromURIPath(selectImageUri, PostingActivity.this);
            img_poster.setImageURI(selectImageUri);
            img_poster.setVisibility(View.VISIBLE);
        }

    }

    public void updateData(){
        zeeLoader.setVisibility(View.VISIBLE);
        postingModel = getIntent().getParcelableExtra(kirim_data);
        String id_mosque = Prefs.getString(GlobalConfig.id_mosque, null);
        String id_post = postingModel.getId_post();
        String date = postingModel.getEvent_date();
        String time = postingModel.getEvent_time();
        String endtime = postingModel.getEnd_event_time();
        String edtTema = edt_tema.getText().toString();
        String edtDesk = edt_desc.getText().toString();
        RequestBody sendId_post, sendId_user, sendTema, sendDeskripsi, sendTime = null, sendEndTime = null, sendDate = null;

        if (edtTema.equals("") || edtDesk.equals("")){
            sendTema = RequestBody.create(MediaType.parse("text/plain"), postingModel.getTitle());
            sendDeskripsi = RequestBody.create(MediaType.parse("text/plain"), postingModel.getDescription());
        }else{
            sendTema = RequestBody.create(MediaType.parse("text/plain"), edtTema);
            sendDeskripsi = RequestBody.create(MediaType.parse("text/plain"), edtDesk);
        }

        //Processing image to upload
        MultipartBody.Part imageRequest = null;
        try {
            File file  = new File(imagePath);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            imageRequest = MultipartBody.Part.createFormData("photo", file.getName(), reqFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendId_post = RequestBody.create(MediaType.parse("text/plain"), id_post);
        sendId_user = RequestBody.create(MediaType.parse("text/plain"), id_mosque);

        if (getDate() == null && getTime() == null && getEndTime() == null){
            sendDate = RequestBody.create(MediaType.parse("text/plain"), date);
            sendTime = RequestBody.create(MediaType.parse("text/plain"), time);
            sendEndTime = RequestBody.create(MediaType.parse("text/plain"), endtime);
        }else if (getDate() == null && getTime() == null){
            Log.d(TAG, "waktu: "+4);
            sendDate = RequestBody.create(MediaType.parse("text/plain"), date);
            sendTime = RequestBody.create(MediaType.parse("text/plain"), time);
            sendEndTime = getEndTime();
        }else if(getTime() == null && getEndTime() == null){
            Log.d(TAG, "waktu: "+5);
            sendDate = getDate();
            sendTime = RequestBody.create(MediaType.parse("text/plain"), time);
            sendEndTime = RequestBody.create(MediaType.parse("text/plain"), endtime);
        }else if (getEndTime() == null && getDate() == null){
            Log.d(TAG, "waktu: "+6);
            sendDate = RequestBody.create(MediaType.parse("text/plain"), date);
            sendTime = getTime();
            sendEndTime = RequestBody.create(MediaType.parse("text/plain"), endtime);
        }else if (getDate() == null){
            Log.d(TAG, "waktu: "+1);
            sendDate = RequestBody.create(MediaType.parse("text/plain"), date);
            sendTime = getTime();
            sendEndTime = getEndTime();
        }else if (getTime() == null){
            Log.d(TAG, "waktu: "+2);
            sendDate = getDate();
            sendTime = RequestBody.create(MediaType.parse("text/plain"), time);
            sendEndTime = getEndTime();
        }else if (getEndTime() == null){
            Log.d(TAG, "waktu: "+3);
            sendDate = getDate();
            sendTime = getTime();
            sendEndTime = RequestBody.create(MediaType.parse("text/plain"), endtime);
        }else{
            sendDate = getDate();
            sendTime = getTime();
            sendEndTime = getEndTime();
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GeneralResponse> postingModelCall = apiInterface.updateKajian(
                sendId_post,
                sendId_user,
                sendTema,
                sendDeskripsi,
                sendDate,
                sendTime,
                sendEndTime,
                imageRequest);
        postingModelCall.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                zeeLoader.setVisibility(View.VISIBLE);
                generalResponse = response.body();
                if (generalResponse.getCode().equals("1") || generalResponse.getCode() == "1") {
                    Toast.makeText(PostingActivity.this, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostingActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.kirim_fragment, "PostingFragment");
                    startActivity(intent);
                    zeeLoader.setVisibility(View.GONE);
                } else {
                    Toast.makeText(PostingActivity.this, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    zeeLoader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Toast.makeText(PostingActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
                zeeLoader.setVisibility(View.GONE);
            }
        });

    }

    public void postData() {

        if (imagePath == null) {
            Toast.makeText(PostingActivity.this, "Silahkan pilih gambar", Toast.LENGTH_SHORT).show();
            return;
        }
        //Processing image to upload
        File file = new File(imagePath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imageRequest = MultipartBody.Part.createFormData("photo", file.getName(), reqFile);
        String id_mosque = Prefs.getString(GlobalConfig.id_mosque, null);
        RequestBody sendId_user = RequestBody.create(MediaType.parse("text/plain"), id_mosque);
        RequestBody sendTema = RequestBody.create(MediaType.parse("text/plain"), edt_tema.getText().toString());
        RequestBody sendDeskripsi = RequestBody.create(MediaType.parse("text/plain"), edt_desc.getText().toString());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        zeeLoader.setVisibility(View.VISIBLE);

        Call<GeneralResponse> generalResponseCall = apiInterface.posting(
                sendId_user,
                sendTema,
                sendDeskripsi,
                getDate(),
                getTime(),
                getEndTime(),
                imageRequest);
        generalResponseCall.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                zeeLoader.setVisibility(View.VISIBLE);
                generalResponse = response.body();
                if (generalResponse.getCode().equals("1") || generalResponse.getCode() == "1") {
                    Toast.makeText(PostingActivity.this, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostingActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.kirim_fragment, "PostingFragment");
                    startActivity(intent);
                    zeeLoader.setVisibility(View.GONE);
                } else {
                    Toast.makeText(PostingActivity.this, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    zeeLoader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Toast.makeText(PostingActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
                zeeLoader.setVisibility(View.GONE);
            }
        });

    }

    private RequestBody getEndTime() {
        if (sendEndTime == null) {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(PostingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(0, 0, 0, hourOfDay, minute);
                    CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
                    sendEndTime = RequestBody.create(MediaType.parse("text/plain"), dateFormat.toString());
                    String stringTime = dateFormat.toString();
                    tv_endTime.setText(stringTime);
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
                    sendEndTime = RequestBody.create(MediaType.parse("text/plain"), dateFormat.toString());
                    String stringTime = dateFormat.toString();
                    tv_endTime.setText(stringTime);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(PostingActivity.this));
        }
        return sendEndTime;
    }
}