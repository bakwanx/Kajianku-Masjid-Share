package com.example.kajiankumasjid.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.agrawalsuneet.dotsloader.loaders.ZeeLoader;
import com.example.kajiankumasjid.Adapter.BerandaAdapter;
import com.example.kajiankumasjid.Adapter.PostingAdapter;
import com.example.kajiankumasjid.ApiInterface.ApiInterface;
import com.example.kajiankumasjid.Model.PostingModel;
import com.example.kajiankumasjid.PostingActivity;
import com.example.kajiankumasjid.R;
import com.example.kajiankumasjid.RestApi.ApiClient;
import com.example.kajiankumasjid.Utilities.GlobalConfig;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostingFragment extends Fragment {
    private Button btn_posting;
    private RecyclerView recyclerView;
    private PostingAdapter postingAdapter;
    private ArrayList<PostingModel> postingModels;
    private ImageView img_onfailure;
    private TextView tv_onfailure;
    private ZeeLoader zeeLoader;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting, container, false);
        btn_posting = view.findViewById(R.id.btn_frposting);
        recyclerView = view.findViewById(R.id.rc_viewPosting);
        img_onfailure = view.findViewById(R.id.img_kajian_frPosting_onfailure);
        tv_onfailure = view.findViewById(R.id.tv_fr_Posting_onfailure);
        zeeLoader = view.findViewById(R.id.zeeLoader_fr_posting);
        tv_onfailure.setVisibility(View.GONE);
        img_onfailure.setVisibility(View.GONE);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        getData();

        btn_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), PostingActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void getData(){
        zeeLoader.setVisibility(View.VISIBLE);
        String id_mosque = Prefs.getString(GlobalConfig.id_mosque, null);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<PostingModel>> postingCall = apiInterface.getKajianById(
                id_mosque
        );
        postingCall.enqueue(new Callback<ArrayList<PostingModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PostingModel>> call, Response<ArrayList<PostingModel>> response) {
                postingModels = response.body();
                img_onfailure.setVisibility(View.GONE);
                tv_onfailure.setVisibility(View.GONE);
                zeeLoader.setVisibility(View.GONE);
                postingAdapter = new PostingAdapter(postingModels, getContext());
                postingAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(postingAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<PostingModel>> call, Throwable t) {
                img_onfailure.setVisibility(View.VISIBLE);
                tv_onfailure.setVisibility(View.VISIBLE);
                zeeLoader.setVisibility(View.GONE);
            }
        });
    }
}