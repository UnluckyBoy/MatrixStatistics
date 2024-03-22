package com.matrix.matrixstatistics.UI.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.matrix.matrixstatistics.R;
import com.matrix.matrixstatistics.Tools.FileUtil;
import com.matrix.matrixstatistics.UI.fragment.ChatFragment;
import com.matrix.matrixstatistics.UI.fragment.EditFragment;
import com.matrix.matrixstatistics.UI.fragment.MainFragment;
import com.matrix.matrixstatistics.UI.view.MatrixDialog;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{
    private final MainActivity TAG = MainActivity.this;
    private Intent intent_MainActivity;
    private static int REQUEST_CODE_FOR_DIR=1;

    private BottomNavigationBar bottomNavigationBar;
    private int lastSelectedPosition = 0;

    private MainFragment mMainFragment;
    private ChatFragment mChatFragment;
    private EditFragment mEditFragment;

    private static boolean mBackKeyPressed = false;//记录是否有首次按键

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AppFirstRun();
        //GetPermission();
        InitRun();
    }

    private void InitRun(){
        InitBar();
    }
    public void InitBar(){
        HideMainBar();
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bar_main);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home, "主页").setActiveColorResource(R.color.green))
                .addItem(new BottomNavigationItem(R.drawable.chat, "论坛").setActiveColorResource(R.color.green))
                .addItem(new BottomNavigationItem(R.drawable.video, "玩乐").setActiveColorResource(R.color.green))
                .setFirstSelectedPosition(lastSelectedPosition )
                .setBarBackgroundColor(R.color.blue)//导航栏背景色
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();
    }
    private void setDefaultFragment() {
        Intent intent=getIntent();
        String id=intent.getStringExtra("U_id");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(id==null){
            mMainFragment = MainFragment.newInstance("");
        }else{
            mMainFragment = MainFragment.newInstance(id);
        }
        transaction.replace(R.id.tb,mMainFragment);
        transaction.commit();
    }
    @Override
    public void onTabSelected(int position) {
        Intent intent=getIntent();
        String id=intent.getStringExtra("U_id");
        //开启事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                if (mMainFragment == null) {
                    mMainFragment = MainFragment.newInstance(id);
                }
                transaction.replace(R.id.tb, mMainFragment);
                //this.finish();
                break;
            case 1:
                if (mChatFragment == null) {
                    mChatFragment = ChatFragment.newInstance(id);
                }
                transaction.replace(R.id.tb, mChatFragment);
                //this.finish();
                break;
            case 2:
                if (mEditFragment == null) {
                    mEditFragment = EditFragment.newInstance(id);
                }
                transaction.replace(R.id.tb, mEditFragment);
                //this.finish();
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }
    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
    /**
     * 主界面隐藏标题栏
     */
    private void HideMainBar(){
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.hide(); //隐藏标题栏
        }
    }
    public void ShowDialog(final Context mContext) {
        String[] names = {mContext.getString(R.string.SystemTitle),
                mContext.getString(R.string.loginTitle),
                mContext.getString(R.string.Confirm),mContext.getString(R.string.Cancel) };
        /**MatrixDialog中最后两个按钮的顺序与names的文本顺序相反**/
        MatrixDialog mDialog = new MatrixDialog(mContext, names, true);
        mDialog.setOnClickListener2LastTwoItems(new MatrixDialog.OnClickListener2LastTwoItem() {
            /**取消按钮**/
            @Override
            public void onClickListener2LastItem() {
                intent_MainActivity.putExtra("U_account","");
                mDialog.dismiss();
            }
            /**确定按钮**/
            @Override
            public void onClickListener2SecondLastItem() {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    /**判断是否首次运行**/
    private void AppFirstRun(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun)
        {
            /**首次运行**/
            //Toast.makeText(this,"首次运行",Toast.LENGTH_SHORT).show();
            FileUtil.createDir(this);//创建文件夹
            GetPermission_data();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        } else
        {
            /**首非次运行**/
            //Toast.makeText(this,"非首次运行",Toast.LENGTH_SHORT).show();
        }
    }
    /**获取data权限**/
    public void GetPermission_data() {
        Uri uri = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
        DocumentFile documentFile = DocumentFile.fromTreeUri(this, uri);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, documentFile.getUri());
        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
        startActivityForResult(intent, REQUEST_CODE_FOR_DIR);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        if (requestCode == REQUEST_CODE_FOR_DIR && (uri = data.getData()) != null) {
            getContentResolver().takePersistableUriPermission(uri, data.getFlags()&
                    (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));
        }
    }

    //退出应用提示
    @Override
    public void onBackPressed() {
        if(!mBackKeyPressed){
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        }
        else{//退出程序
            this.finish();
            System.exit(0);
        }
    }
}