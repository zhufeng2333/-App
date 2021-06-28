package com.example.quote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {
//    private volatile static boolean flag = false;
    public static String username;
    public static String password;
    public static String userid;
    public static SharedPreferences mPreferences;
    public static String sharedPrefFile = "com.example.quote";
    private String curQuote = "";
    private String curQuoteId = "";
    private ImageView favoriteImageView;
    private ImageView pictureImageView;
    private TextView quoteTextView;
    private TextView yearMonthTextView;
    private TextView dayTextView;
    private TextView weekTextView;
    String today;
    String week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if((getIntent().getFlags()&Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)!=0){
//            //结束你的activity
//            finish();
//            return;
//        }
//
//        if (!this.isTaskRoot()) {
//            Intent mainIntent = getIntent();
//            String action = mainIntent.getAction();
//            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
//                finish();
//                return;
//            }
//        }
//        if(flag==false) {
            System.out.println("进入oncreate的false");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            if (!hasRegisteredLocal()) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
            init();
            if (getSupportLoaderManager().getLoader(0) != null) {
                getSupportLoaderManager().initLoader(0, null, this);
            }
            if (getSupportLoaderManager().getLoader(1) != null) {
                getSupportLoaderManager().initLoader(1, null, this);
            }
            showQuoteApiData();
            showImageApiData();
            quoteTextView.setText("加载中。。。");
            pictureImageView.setImageBitmap(ApiDataUtil.bitmap);
//        Toast.makeText(this,"加载中。。。",Toast.LENGTH_LONG).show();
//            flag = true;
//        }
    }

    public boolean hasRegisteredLocal(){//返回是否注册过
        mPreferences = getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        username = mPreferences.getString("username",null);
        password = mPreferences.getString("password",null);
        userid = mPreferences.getString("userid",null);
        if(username==null) return false;
        else return true;
    }

    public void init(){
        pictureImageView = (ImageView)findViewById(R.id.pictureImageView);
        favoriteImageView = (ImageView)findViewById(R.id.favoriteImageView);
        quoteTextView = (TextView)findViewById(R.id.quoteTextView);
        yearMonthTextView = (TextView)findViewById(R.id.yearMonthTextView);
        dayTextView = (TextView)findViewById(R.id.dayTextView);
        weekTextView = (TextView)findViewById(R.id.weekTextView);
        quoteTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        favoriteImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        today = formatter.format(date);
        String yearMonth = today.substring(6,10)+"年"+today.substring(3,5)+"月";
        String day = today.substring(0,2);
        week = getWeekOfDate(date);
        weekTextView.setText(week);
        yearMonthTextView.setText(yearMonth);
        dayTextView.setText(day);
    }
    public void showImageApiData() {
        System.out.println("进入showImageApiData");
        Bundle queryBundle = new Bundle();
        getSupportLoaderManager().restartLoader(1, queryBundle, this);
        System.out.println("完成图片");
    }

    public void showQuoteApiData() {
        System.out.println("进入showQuoteApiData");
        Bundle queryBundle = new Bundle();
        getSupportLoaderManager().restartLoader(0, queryBundle, this);
    }

    public void randomBtn(View view) {
        showQuoteApiData();
        favoriteImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
    }

    public void favoriteBtn(View view) {
        Intent intent = new Intent(MainActivity.this,FavoriteActivity.class);
        startActivity(intent);
    }

    public void aboutBtn(View view) {
        Intent intent = new Intent(MainActivity.this,AboutActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void favorite(View view) {
        System.out.println("进入favorite方法");

        if(username==null||username.equals("")){
            Toast.makeText(this,"请先登录！",Toast.LENGTH_LONG);
        }else {
            Drawable.ConstantState borderState = getDrawable(R.drawable.ic_baseline_favorite_border_24).getConstantState();
            if (favoriteImageView.getDrawable().getConstantState() == borderState) {//未收藏，执行收藏
                favoriteImageView.setImageResource(R.drawable.ic_baseline_favorite_24);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (DataBaseUtil.getQuoteByEnglish(ApiDataUtil.curEnglish) == null) {
                            System.out.println("没有该记录");
                            DataBaseUtil.addQuote(ApiDataUtil.curChinese, ApiDataUtil.curEnglish);
                            DataBaseUtil.addFavorite(DataBaseUtil.curQuoteId, username);
//                            Toast.makeText(this,"收藏成功！",Toast.LENGTH_SHORT).show();
                        } else {
                            //更新该数据，favorite++
                            System.out.println("有该记录");
                            //更新尚未实现
                            //待实现判断有没有收藏过
                            DataBaseUtil.updateFavoriteById(DataBaseUtil.id);
                            DataBaseUtil.addFavorite(DataBaseUtil.curQuoteId, username);
                        }
                    }
                });
                thread.start();
            } else {//已收藏，取消收藏
//            favoriteImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                //待实现：调用数据库方法
            }
        }
    }

    public static String getWeekOfDate(Date date) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    @NonNull
    @Override
    public Loader<Object> onCreateLoader(int id, @Nullable Bundle args) {
        if(id==0) return new QuoteApiDataLoader(this);
        else return new ImageApiDataLoader(this);
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Object> loader, Object data) {
        if(loader.getId()==0) quoteTextView.setText((String)data);
        else pictureImageView.setImageBitmap((Bitmap)data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Object> loader) {

    }

    @Override
    public boolean moveTaskToBack(boolean nonRoot) {
        return super.moveTaskToBack(false);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        favoriteImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
    }
}