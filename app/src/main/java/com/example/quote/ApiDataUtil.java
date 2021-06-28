package com.example.quote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ApiDataUtil {
    public static final String quoteApiUrl = "https://route.showapi.com/1211-1?showapi_appid=140441&showapi_sign=8cde7e3f75c141f99bc512acdc7e1ec0&count=1";
    public static final String imageApiUrl = "https://route.showapi.com/1287-1?showapi_appid=140441&showapi_sign=8cde7e3f75c141f99bc512acdc7e1ec0";
    public static String curChinese = "";
    public static String curEnglish = "";
    public static String date = "";
    public static String copyright = "";
    public static String imageUrl = "";
    public static Bitmap bitmap;
    private static Thread thread;
//    private static ApiDataUtil apiDataUtil = new ApiDataUtil();
//    private ApiDataUtil(){}
//    public static ApiDataUtil getInstance(){
//        return apiDataUtil;
//    }
    public static void getApiData(String apiUrl){
//        System.out.println("进入getApiData");
//        thread = new Thread(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void run() {
//                try {
//                    toGetApi(apiUrl);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
//        System.out.println("到了start后面");
//        try {
//            thread.join();
//            System.out.println("到了join后面");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        try {
            toGetApi(apiUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void toGetApi(String url) throws Exception {
        System.out.println("进入函数了！！");
        URL u = new URL(url);
        InputStream in = u.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte buf[] = new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        byte b[] = out.toByteArray();
        String str = new String(b, "utf-8");
        if(url == quoteApiUrl)splitQuoteApiData(str);
        else if(url == imageApiUrl)splitImageApiData(str);
    }

    private static void splitImageApiData(String str) {
        System.out.println("解析图片");
        System.out.println(str);
        JSONObject json = JSON.parseObject(str);
        str = json.getString("showapi_res_body");
        json = JSON.parseObject(str);
        str = json.getString("data");
        json = JSON.parseObject(str);
        date = json.getString("date");
        copyright = json.getString("copyright");
        imageUrl = json.getString("img_1920");
        bitmap = getHttpBitmap(imageUrl);
        System.out.println(date);
        System.out.println(copyright);
        System.out.println(imageUrl);
    }

    public static void splitQuoteApiData(String str){
        JSONObject json = JSON.parseObject(str);
        str = json.getString("showapi_res_body");
        json = JSON.parseObject(str);
        str = json.getString("data");
        System.out.println(str);
        int len = str.length();
        str = str.substring(1,len);
        ArrayList<JSONObject> a = new ArrayList<>();
        int index = 0;
        int index2 = 0;
        String s = "";
        while(str.indexOf("{",index)!=-1){
            index = str.indexOf("{",index);
            index2 = str.indexOf("}",index);
            s = str.substring(index,index2+1);
            JSONObject json2 = JSON.parseObject(s);
            a.add(json2);
            index = index2+1;
        }
        curEnglish = a.get(0).getString("english");
        curChinese = a.get(0).getString("chinese");
        System.out.println(curChinese);
        System.out.println(curEnglish);
    }

    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
