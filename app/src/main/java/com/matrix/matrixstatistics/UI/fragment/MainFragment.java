package com.matrix.matrixstatistics.UI.fragment;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.matrix.matrixstatistics.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName MainFragment
 * @Author Create By matrix
 * @Date 2024/3/21 0021 21:38
 */
public class MainFragment extends Fragment {
    private View view;
    private String args=null;

    private EditText edit_input;
    private Button statisticBtn;
    //private  TextView view_statistic;

    public static MainFragment newInstance(String param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("args", param1);
        fragment.setArguments(args);
        return fragment;
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
            view = inflater.inflate(R.layout.mainfragment, container, false);

            Bundle bundle = getArguments();
            args = bundle.getString("args");
            //执行Fragment视图数据初始化
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

    private void InitFragmentData(final View view){
        edit_input=view.findViewById(R.id.edit_input);
        statisticBtn=view.findViewById(R.id.statisticBtn);
        //view_statistic=view.findViewById(R.id.view_result);
        //view_statistic.setMovementMethod(ScrollingMovementMethod.getInstance());
        TableLayout tableLayout = view.findViewById(R.id.table_layout);

        statisticBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edit_input.getText().toString().isEmpty()){
                    //view_statistic.setText(getNum(edit_input.getText().toString()));
                    tableLayout.removeAllViews();//创建之前先清空之前的视图
                    // 创建表头行
                    TableRow headerRow = new TableRow(view.getContext());
                    TableRow.LayoutParams headerLayoutParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT, // 宽度匹配父容器
                            TableRow.LayoutParams.WRAP_CONTENT); // 高度根据内容自动调整
                    headerRow.setLayoutParams(headerLayoutParams);
                    // 创建TextView来显示键的列名
                    TextView keyHeaderTextView = new TextView(view.getContext());
                    keyHeaderTextView.setText("数字"); // 设置键的列名
                    keyHeaderTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                    keyHeaderTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28); // 可选：设置字体大小
                    keyHeaderTextView.setLayoutParams(new TableRow.LayoutParams(
                            0, // 宽度为0，以便权重分配生效
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1f)); // 设置权重为1，以便列宽均匀分布
                    headerRow.addView(keyHeaderTextView);
                    // 创建TextView来显示值的列名
                    TextView valueHeaderTextView = new TextView(view.getContext());
                    valueHeaderTextView.setText("频率"); // 设置值的列名
                    valueHeaderTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                    valueHeaderTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28); // 可选：设置字体大小
                    valueHeaderTextView.setLayoutParams(new TableRow.LayoutParams(
                            0, // 宽度为0，以便权重分配生效
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1f)); // 设置权重为1，以便列宽均匀分布
                    headerRow.addView(valueHeaderTextView);
                    // 将表头行添加到TableLayout中
                    tableLayout.addView(headerRow);

                    Map<Integer, Integer> result=getNum(edit_input.getText().toString());
                    for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
                        TableRow row = new TableRow(view.getContext());
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;// 设置居中
                        row.setLayoutParams(layoutParams);
                        // 创建TextView来显示键
                        TextView keyTextView = new TextView(view.getContext());
                        handleTextView(keyTextView,entry,"key");
                        row.addView(keyTextView);
                        // 创建TextView来显示值
                        TextView valueTextView = new TextView(view.getContext());
                        handleTextView(valueTextView,entry,"value");
                        row.addView(valueTextView);
                        // 将TableRow添加到TableLayout中
                        tableLayout.addView(row);
                    }
                }
            }
        });
    }

    /**
     * 显示文本逻辑函数
     * @param textView 文本组件
     * @param entry 数据
     * @param valueType 数据类型参数:key为键;value为值
     */
    private void handleTextView(TextView textView,Map.Entry<Integer, Integer> entry,String valueType){
        switch (valueType){
            case "key":
                textView.setText(String.valueOf(entry.getKey()));
                textView.setPadding(10, 10, 10, 10); // 设置内边距
                textView.setTextSize(28);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            case "value":
                textView.setText(String.valueOf(entry.getValue()));
                textView.setPadding(10, 10, 10, 10);
                textView.setTextSize(28);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
        }
    }

    /**
     * 统计字符串中的数字
     * @param str
     * @return
     */
    private Map<Integer, Integer> getNum(String str){
        //String result="";
        // 使用HashMap来存储数字和它们的计数
        String temStr=str.replace("【", "").replace("】", ",");
        if (temStr.endsWith(",")) {
            temStr = temStr.substring(0, temStr.length() - 1);
        }
        Map<Integer, Integer> numberCounts = new HashMap<>();
        // 去除字符串两端的空格，并用空格替换所有的非数字字符
        String cleanedInput = temStr.trim().replaceAll("[^0-9\\s]", " ");
        // 分割字符串，得到数字字符串数组
        String[] numberStrings = cleanedInput.split("\\s+");
        // 遍历数字字符串数组，统计每个数字出现的次数
        for (String numberStr : numberStrings) {
            // 将数字字符串转换为整数
            int number = Integer.parseInt(numberStr);
            // 在HashMap中增加数字的计数
            numberCounts.put(number, numberCounts.getOrDefault(number, 0) + 1);
        }
        // 使用流（Stream）API按数字出现的次数降序排序
        Map<Integer, Integer> sortedNumberCounts = numberCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
        return sortedNumberCounts;
    }
}
