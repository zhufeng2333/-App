package com.example.quote;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class ImageApiDataLoader extends AsyncTaskLoader<Object> {

    ImageApiDataLoader(Context context) {
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
        ApiDataUtil.getApiData(ApiDataUtil.imageApiUrl);
        return ApiDataUtil.bitmap;
    }

}
