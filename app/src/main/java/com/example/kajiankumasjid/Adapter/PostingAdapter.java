package com.example.kajiankumasjid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.kajiankumasjid.ApiInterface.ApiInterface;
import com.example.kajiankumasjid.MainActivity;
import com.example.kajiankumasjid.Model.GeneralResponse;
import com.example.kajiankumasjid.Model.PostingModel;
import com.example.kajiankumasjid.PostingActivity;
import com.example.kajiankumasjid.R;
import com.example.kajiankumasjid.RestApi.ApiClient;
import com.example.kajiankumasjid.Utilities.GlobalConfig;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostingAdapter extends RecyclerView.Adapter<PostingAdapter.PostingHolder> {
    private Context context;
    private ArrayList<PostingModel> postingModels;

    public PostingAdapter(ArrayList<PostingModel> postingModels, Context context) {
        this.postingModels = postingModels;
        this.context = context;
    }

    @NonNull
    @Override
    public PostingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_posting, parent,false);
        return new PostingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostingHolder holder, int position) {
        holder.bind(postingModels.get(position));
    }

    @Override
    public int getItemCount() {
        return postingModels.size();
    }

    public class PostingHolder extends RecyclerView.ViewHolder {
        ImageView img_poster, img_edit;
        TextView tv_name_mosque, tv_tema, tv_eventdate, tv_eventTime, tv_eventEndTime;
        ReadMoreTextView tv_deskripsi;
        PhotoView photoZoom;
        public PostingHolder(@NonNull View itemView) {
            super(itemView);
            img_poster = itemView.findViewById(R.id.img_cdPosting_poster);
            img_edit = itemView.findViewById(R.id.img_cdPosting_edit);
            tv_name_mosque = itemView.findViewById(R.id.tv_cdPosting_name_mosque);
            tv_tema = itemView.findViewById(R.id.tv_cdPosting_tema);
            tv_eventdate = itemView.findViewById(R.id.tv_cdPosting_evendate);
            tv_eventTime = itemView.findViewById(R.id.tv_cdPosting_eventime);
            tv_eventEndTime = itemView.findViewById(R.id.tv_cdPosting_eventimeEnd);
            tv_deskripsi = itemView.findViewById(R.id.tv_cdPosting_Deskripsi);
            photoZoom = itemView.findViewById(R.id.ImgDetail);
        }

        private void bind(PostingModel postingModel){
            Picasso.with(context)
                    .load(GlobalConfig.img_poster + postingModel.getImage())
                    .placeholder(R.drawable.logo_kajianku)
                    .into(img_poster);

            img_poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url_img = GlobalConfig.img_poster + postingModel.getImage();
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                    LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View mView = li.inflate(R.layout.dialog_zoom_image, null);
                    PhotoView photoView = mView.findViewById(R.id.ImgDetail);

                    Picasso.with(context)
                            .load(url_img)
                            .into(photoView);

                    mBuilder.setView(mView);
                    AlertDialog mDialog = mBuilder.create();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(mDialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    mDialog.show();
                    mDialog.getWindow().setAttributes(lp);
                }
            });

            img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context,img_edit);
                    popupMenu.inflate(R.menu.options_menu);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.options_edit:
                                    Intent intent = new Intent(itemView.getContext(), PostingActivity.class);
                                    intent.putExtra(PostingActivity.kirim_data, postingModel);
                                    itemView.getContext().startActivity(intent);
                                    return true;
                                case R.id.options_delete:
                                    String id_post = postingModel.getId_post();
                                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                                    Call<GeneralResponse> generalResponseCall = apiInterface.deletePosting(id_post);
                                    generalResponseCall.enqueue(new Callback<GeneralResponse>() {
                                        @Override
                                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                            GeneralResponse generalResponse = response.body();
                                            if (generalResponse.getCode().equals("1")){
                                                Toast.makeText(context, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(context, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<GeneralResponse> call, Throwable t) {
                                            Toast.makeText(context, "No Connection", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Intent toHome = new Intent(itemView.getContext(), MainActivity.class);
                                    itemView.getContext().startActivity(toHome);
                                    return true;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();

                }
            });

            tv_name_mosque.setText(postingModel.getName_mosque());
            Log.d("Adapter", "name mosque: "+postingModel.getName_mosque());
            tv_tema.setText(postingModel.getTitle());
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(postingModel.getEvent_date());
                String simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy").format(date);
                tv_eventdate.setText(simpleDateFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String time = postingModel.getEvent_time().substring(0, postingModel.getEvent_time().length() - 3);
            String endTime = postingModel.getEnd_event_time().substring(0, postingModel.getEnd_event_time().length() - 3);
            tv_eventTime.setText(time);
            tv_eventEndTime.setText(endTime);
            tv_deskripsi.setText(postingModel.getDescription());

        }
    }

}
