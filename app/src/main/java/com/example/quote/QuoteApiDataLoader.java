package com.example.quote;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class QuoteApiDataLoader extends AsyncTaskLoader<Object> {

    QuoteApiDataLoader(Context context) {
        super(context);
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
        ApiDataUtil.getApiData(ApiDataUtil.quoteApiUrl);
        return ApiDataUtil.curEnglish+"\n"+ApiDataUtil.curChinese;
    }
}
