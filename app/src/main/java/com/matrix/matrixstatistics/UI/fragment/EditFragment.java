package com.matrix.matrixstatistics.UI.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.matrix.matrixstatistics.R;

/**
 * @ClassName EditFragment
 * @Author Create By matrix
 * @Date 2024/3/21 0021 21:39
 */
public class EditFragment extends Fragment {
    private static int REQUEST_CODE_FOR_DIR=1;

    private View view;
    private String args = null;
    private TextView mVideo_Text, mAudio_Text;
    private Button mVideo_Btn, mAudio_Btn,mBuild_Btn;
    private String mBtnType=null;
    private String video_name,audio_name,out_name="Out.mp4";

    public static EditFragment newInstance(String param1) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString("args", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public EditFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.editfragment, container, false);

            Bundle bundle = getArguments();
            args = bundle.getString("args");

            InitFragmentData(view);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void InitFragmentData(final View view) {

    }
}