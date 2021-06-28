package com.example.quote;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class QuoteByQuoteIdLoader extends AsyncTaskLoader<Object> {

    private ArrayList marrayList;
    private String english;
    private String chinese;
    private String quoteid;

    QuoteByQuoteIdLoader(Context context, ArrayList arrayList) {
        super(context);
        marrayList = arrayList;
    }

    public ArrayList getArrayList(){
        return marrayList;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public Object loadInBackground() {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        ArrayList a = new ArrayList();
        Iterator iterator = marrayList.iterator();
        while(iterator.hasNext()){
            quoteid = (String)iterator.next();
            JSONObject jsonObject = DataBaseUtil.getQuoteByQuoteId(quoteid);
            english = jsonObject.getString("english");
            chinese = jsonObject.getString("chinese");
            a.add(english+"\n"+chinese);
        }
        return a;
    }
}

