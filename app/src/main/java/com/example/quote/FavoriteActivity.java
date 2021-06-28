package com.example.quote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;


public class FavoriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {
    private static ArrayList arrayList;
    private LinearLayout ll;
    private ScrollView scrollView;
    private LinearLayout mlinearLayout;
    private TextView mUserNameTextView;
    public static volatile boolean flag = false;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("执行ONCREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        arrayList = DataBaseUtil.getQuoteIdsByUserName(MainActivity.username);
        scrollView = (ScrollView)findViewById(R.id.mscrollView);
        mUserNameTextView = (TextView)findViewById(R.id.userNameTextView);
        mUserNameTextView.setText(MainActivity.username+"的收藏：\n\t\t\t\t加载中(时间较长请等待)...");
        mlinearLayout = (LinearLayout)findViewById(R.id.mLinearLayout);

        if (getSupportLoaderManager().getLoader(3) != null) {
            getSupportLoaderManager().initLoader(3, null, this);
        }
        traverseArrayList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("执行ONPAUSE");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("执行ONRESTART");
    }

    @SuppressLint("ResourceAsColor")
    private void traverseArrayList() {
        Iterator iterator = arrayList.iterator();
        String quoteid;
        String english;
        String chinese;
        int index = 0;
        Thread thread;
        Bundle queryBundle = new Bundle();
        queryBundle.putStringArrayList("arraylist",arrayList);
//        queryBundle.putInt("index",index);

//        while(iterator.hasNext()){
//            flag = false;
//            TextView textView = new TextView(this);
//            textView.setText("加载中。。。");
//            textView.setId(index);
//            index++;
//            textView.setTextColor(R.color.black);
//            textView.setPadding(20,40,20,40);
//            mlinearLayout.addView(textView);

//            jsonObject = DataBaseUtil.getQuoteByQuoteId(id);
//            english = jsonObject.getString("english");
//            chinese = jsonObject.getString("chinese");
//            createText(english+"\n"+chinese);
//        }
        getSupportLoaderManager().restartLoader(3, queryBundle, this);
    }

//    @SuppressLint("ResourceAsColor")
//    private void createText(String str){
//        TextView textView = new TextView(this);
//        textView.setText(str);
//        textView.setTextColor(R.color.black);
////        textView.setGravity(Gravity.CENTER);
//        textView.setPadding(20,40,20,40);
//        mlinearLayout.addView(textView);
//    }


    @NonNull
    @Override
    public Loader<Object> onCreateLoader(int id, @Nullable Bundle args) {
        return new QuoteByQuoteIdLoader(this,args.getStringArrayList("arraylist"));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onLoadFinished(@NonNull Loader<Object> loader, Object data) {
        ArrayList a = (ArrayList)data;
        mUserNameTextView.setText(MainActivity.username+"的收藏：");
        for(int i=0;i<a.size();i++){
            TextView textView = new TextView(this);
            textView.setText((String)a.get(i));
            textView.setTextColor(R.color.black);
            textView.setPadding(20,40,20,40);
            mlinearLayout.addView(textView);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Object> loader) {

    }
}