package com.example.kajiankumasjid.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.kajiankumasjid.R;


public class DialogDistanceFragment extends DialogFragment {
    private SeekBar seekBar;
    private TextView tv_jarak, tv_cls, tv_ok;
    public interface OnInputDistanceSelected{
        String SendDistance(String distance);
    }

    public OnInputDistanceSelected mOnInputDistanceSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_distance_dialog_box, container,false);

        seekBar = view.findViewById(R.id.distance_seekbar);
        tv_jarak = view.findViewById(R.id.cd_tv_distance);
        tv_ok = view.findViewById(R.id.tv_ok_dialog_box);
        tv_cls = view.findViewById(R.id.tv_close_dialog_box);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBar.setProgress(i);
                Integer progress = i;
                tv_jarak.setText(""+i+" KM");

                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnInputDistanceSelected.SendDistance(String.valueOf(i));

                        getDialog().dismiss();
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tv_cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnInputDistanceSelected = (OnInputDistanceSelected) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e("DialogDistanceFragment : ", e.getMessage());
        }
    }
}
