package com.example.kajiankumasjid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.kajiankumasjid.Model.PostingModel;
import com.example.kajiankumasjid.R;
import com.example.kajiankumasjid.Utilities.GlobalConfig;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BerandaAdapter extends RecyclerView.Adapter<BerandaAdapter.BerandaHolder> {
    private ArrayList<PostingModel> postingModels;
    private Context context;

    public BerandaAdapter(ArrayList<PostingModel> postingModels, Context context) {
        this.postingModels = postingModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BerandaAdapter.BerandaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_beranda, parent, false);
        return new BerandaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BerandaAdapter.BerandaHolder holder, int position) {
        holder.bind(postingModels.get(position));
    }

    @Override
    public int getItemCount() {
        if (postingModels != null) {
            return postingModels.size();
        } else {
            return 0;
        }
    }

    public class BerandaHolder extends RecyclerView.ViewHolder {
        ImageView img_poster;
        TextView tv_name_mosque, tv_tema, tv_eventdate, tv_eventTime, tv_eventEndTime, tv_location;
        ReadMoreTextView tv_deskripsi;
        PhotoView photoZoom;

        public BerandaHolder(@NonNull View itemView) {
            super(itemView);
            img_poster = itemView.findViewById(R.id.img_cd_poster);
            tv_name_mosque = itemView.findViewById(R.id.tv_cd_name_mosque);
            tv_tema = itemView.findViewById(R.id.tv_cd_tema);
            tv_eventdate = itemView.findViewById(R.id.tv_cd_evendate);
            tv_eventTime = itemView.findViewById(R.id.tv_cd_eventime);
            tv_eventEndTime = itemView.findViewById(R.id.tv_cd_eventimeEnd);
            tv_deskripsi = itemView.findViewById(R.id.tv_cdDeskripsi);
            tv_location = itemView.findViewById(R.id.tv_cdLocation);
            photoZoom = itemView.findViewById(R.id.ImgDetail);
        }

        public void bind(PostingModel postingModel) {
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
            tv_location.setText(postingModel.getAddress());
            tv_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url_location = postingModel.getLatitude() + "," + postingModel.getLongitude();
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/search/?api=1&query=" + url_location));
                    itemView.getContext().startActivity(intent);
                }
            });

        }

    }
}
