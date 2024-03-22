package com.matrix.matrixstatistics.UI.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.matrix.matrixstatistics.R;

/**
 * @ClassName ChatFragment
 * @Author Create By matrix
 * @Date 2024/3/21 0021 21:40
 */
public class ChatFragment extends Fragment {
    private View view;
    private String args1=null;

    public static ChatFragment newInstance(String param1) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString("args1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view!=null){
            ViewGroup parent=(ViewGroup) view.getParent();
            if(parent!=null){
                parent.removeView(view);
            }
        }else{
            view = inflater.inflate(R.layout.chatfragment, container, false);

            Bundle bundle = getArguments();
            args1 = bundle.getString("args1");
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

    private void InitFragmentData(final View view){

        TextView tv = view.findViewById(R.id.chatFragment_text);
    }
}