package com.example.quote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataBaseUtil {
//    static ApiDataUtil apiDataUtil = ApiDataUtil.getInstance();
//    static HttpRestUtil httpRestUtil = HttpRestUtil.getInstance();
    public static final String quoteUrl = "https://caoliang-env-6g42pz2708081b24-1305327712.ap-shanghai.service.tcloudbase.com/api/v1.0/quote";
    public static final String userUrl = "https://caoliang-env-6g42pz2708081b24-1305327712.ap-shanghai.service.tcloudbase.com/api/v1.0/user";
    public static final String favoriteUrl = "https://caoliang-env-6g42pz2708081b24-1305327712.ap-shanghai.service.tcloudbase.com/api/v1.0/favorite";
    public static String data = "";//查询到quote的一条数据
    public static JSONObject dataJsonObject;//查询到quote的一条数据中的data字段
    public static String id = "";//查询到quote的data id

    public static String datas = "";//查询到quote的多条数据
    public static JSONObject dataJsonObjects;//datas转JSONObject
    public static String ids = "";//查询到quote的data id

    public static String curQuote = "";//当前添加quote返回字符串（辅助，无实用）
    public static JSONObject curQuoteJsonObject;//curQuote转成的JSONObject（辅助，无实用）
    public static String curQuoteId = "";//当前添加的quoteid

    public static String userData = "";//查询到的user的数据（辅助，保证new Thread中也能使用）
    public static JSONObject userDataJsonObject;//查询到的user的数据中的data字段（辅助，保证new Thread中也能使用）
    public static String userid = "";//查询到的userid（无用）
    public static int favoriteCount = 0;

    //用户相关
    //用户注册
    public static void register(String username, String password){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //待完成
                JSONObject postParam = generateAddUserData(username,password);
                String data = HttpRestUtil.sendPost(userUrl,postParam);
                System.out.println("register--data:"+data);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //通过username查询user数据（为了查看是否存在该用户，以判断能否注册）
    public static JSONObject getUserByUserName(String username){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject postParam = generateGetUserByUserName(username);
                userData = HttpRestUtil.sendPost(userUrl+"/find", postParam);
                System.out.println(data);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        userDataJsonObject = splitUserData(userData);
        System.out.println("getUserByUserName--userDataJsonObject："+userDataJsonObject);
        return userDataJsonObject;
    }
    //通过username和password查询user数据（为了检查用户名和密码是否对应，以登录）
    public static JSONObject getUserByNameAndPwd(String username,String password){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject postParam = generateGetUserByUserNameAndPwd(username,password);
                userData = HttpRestUtil.sendPost(userUrl+"/find", postParam);
                System.out.println(data);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("getUserByNameAndPwd--userData："+userData);
        userDataJsonObject = splitUserData(userData);
        System.out.println("getUserByNameAndPwd--userDataJsonObject："+userDataJsonObject);
        return userDataJsonObject;
    }

    //生成通过username查询user数据的语句-->getUserByUserName(String username)
    public static JSONObject generateGetUserByUserName(String username) {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonParam2 = new JSONObject();
        JSONObject jsonParam3 = new JSONObject();
        jsonParam.put("$eq",username);
        jsonParam2.put("username",jsonParam);
        jsonParam3.put("query",jsonParam2);
        System.out.println("generateGetUserByUserName--jsonparam3:"+jsonParam3);
        return jsonParam3;
    }
    //生成通过username和password查询user数据的语句-->getUserByNameAndPwd(String username,String password)
    public static JSONObject generateGetUserByUserNameAndPwd(String username,String password) {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonParam2 = new JSONObject();
        JSONObject jsonParam3 = new JSONObject();
        JSONObject jsonParam4 = new JSONObject();
        JSONObject jsonParam5 = new JSONObject();
        JSONObject jsonParam6 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonParam.put("$eq",username);
        jsonParam2.put("username",jsonParam);
        jsonParam3.put("$eq",password);
        jsonParam4.put("password",jsonParam3);
        Object[] objects = new Object[2];
        objects[0] = jsonParam2;
        objects[1] = jsonParam4;
        jsonParam5.put("$and",objects);
        jsonParam6.put("query",jsonParam5);
        System.out.println("generateGetUserByUserNameAndPwd--jsonparam66:"+jsonParam6);
        return jsonParam6;
    }
    //生成register的语句
    public static JSONObject generateAddUserData(String username, String password){
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonParam2 = new JSONObject();
        try {
            jsonParam.put("username", username);
            jsonParam.put("password", password);
            Object[] a = new Object[1];
            a[0] = jsonParam;
            jsonParam2.put("data",a);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("generateAddUserData--jsonParam2:"+jsonParam2);
        return jsonParam2;
    }



    //数据相关
    //添加quote
    public static void addQuote(String chinese, String english) {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //待完成
                JSONObject postParam = generateQuoteData(chinese,english);
                curQuote = HttpRestUtil.sendPost(quoteUrl,postParam);
                System.out.println(curQuote);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        curQuoteJsonObject = JSONObject.parseObject(curQuote);
        String temp = curQuoteJsonObject.getString("ids");
        curQuoteId = temp.substring(2,temp.length()-2);
    }
    //未实现：更新某个quoteid的favorite数量
    public static void updateFavoriteById(String id){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                JSONObject postParam = generateUpdateFavorite(favoriteCount);
//                String temp = HttpRestUtil.sendPatch(HttpRestUtil.quoteUrl, postParam);
//                System.out.println("temp为" + temp);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    //通过quote的英文查询quote
    public static JSONObject getQuoteByEnglish(String english){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                JSONObject postParam = generateGetQuoteByEnglish(english);
                data = HttpRestUtil.sendPost(quoteUrl + "/find", postParam);
                System.out.println("getQuoteByEnglish--data:" + data);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dataJsonObject = splitQuoteData(data);
        System.out.print("getQuoteByEnglish--dataJsonObject:");
        System.out.println(dataJsonObject);
        if(dataJsonObject!=null){
            id = dataJsonObject.getString("_id");
            System.out.println("getQuoteByEnglish--id:"+id);
        }
        return dataJsonObject;
    }

    //生成添加quote得到语句
    public static JSONObject generateQuoteData(String chinese,String english){
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonParam2 = new JSONObject();
        jsonParam.put("chinese", chinese);
        jsonParam.put("english", english);
        jsonParam.put("favorite", 1);
        Object[] a = new Object[1];
        a[0] = jsonParam;
        jsonParam2.put("data",a);
        System.out.println("generateQuoteData--jsonParam2:"+jsonParam2);
        return jsonParam2;
    }
    //生成更新favorite数量的语句-->updateFavoriteById(String id)
    public static JSONObject generateUpdateFavorite(int favorite){
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        json.put("favorite",favorite+1);
        json2.put("data",json);
        System.out.println("generateUpdateFavorite--json2:"+json2);
        return json2;
    }
    //生成按英语查询quote的语句-->getQuoteByEnglish(String english)
    public static JSONObject generateGetQuoteByEnglish(String english){
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonParam2 = new JSONObject();
        JSONObject jsonParam3 = new JSONObject();
        jsonParam.put("$eq",english);
        jsonParam2.put("english",jsonParam);
        jsonParam3.put("query",jsonParam2);
        System.out.println("generateGetQuoteByEnglish--jsonParam3:"+jsonParam3);
        return jsonParam3;
    }
    //将按照getQuoteByEnglish查找到的json数据分割，得到data字段内的JSON数据
    public static JSONObject splitQuoteData(String data){
        JSONObject json = JSON.parseObject(data);
        data = json.getString("data");
        if(data.equals("[]")){
            return null;
        }
        else{
            data = data.substring(1,data.length()-1);
            json = JSON.parseObject(data);
            System.out.println("splitQuoteData--json:"+json);
            return json;
        }
    }
    //将查询到的当前用户数据分割，得到data字段内的JSON数据
    public static JSONObject splitUserData(String data){
        JSONObject json = JSON.parseObject(data);
        data = json.getString("data");
        if(data.equals("[]")){
            return null;
        }
        else{
            data = data.substring(1,data.length()-1);
            json = JSON.parseObject(data);
            System.out.println("splitUserData--json:"+json);
            return json;
        }
    }



    //收藏相关
    //添加收藏
    public static void addFavorite(String quoteid,String username) {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //待完成
                System.out.println("addFavorite--quoteid:"+quoteid);
                System.out.println("addFavorite--username:"+username);
                JSONObject postParam = generateFavoriteData(quoteid,username);
                String data = HttpRestUtil.sendPost(favoriteUrl,postParam);
                System.out.println("addFavorite--data:"+data);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    //生成添加收藏的语句
    public static JSONObject generateFavoriteData(String quoteid,String username){
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonParam2 = new JSONObject();
        jsonParam.put("quoteid", quoteid);
        jsonParam.put("username", username);
        Object[] a = new Object[1];
        a[0] = jsonParam;
        jsonParam2.put("data",a);
        System.out.println("generateFavoriteData--jsonParam2:"+jsonParam2);
        return jsonParam2;
    }
    //通过username查询收藏quote列表
    public static ArrayList<String> getQuoteIdsByUserName(String username){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                JSONObject postParam = generateQuoteIdByUserName(username);
                datas = HttpRestUtil.sendPost(favoriteUrl + "/find", postParam);
                System.out.println("getQuoteIdByUserName--data:" + datas);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dataJsonObjects = JSONObject.parseObject(datas);
        String str = dataJsonObjects.getString("data");
        return splitQuoteDatas(str);
    }

    public static ArrayList<String> splitQuoteDatas(String str){
        int len = str.length();
        str = str.substring(1,len);
        ArrayList<String> a = new ArrayList<>();
        int index = 0;
        int index2 = 0;
        String s = "";
        while(str.indexOf("{",index)!=-1){
            index = str.indexOf("{",index);
            index2 = str.indexOf("}",index);
            s = str.substring(index,index2+1);
            JSONObject json2 = JSON.parseObject(s);
            a.add(json2.getString("quoteid"));
            index = index2+1;
        }
        return a;
    }


    //通过quoteid查询quote
    public static JSONObject getQuoteByQuoteId(String quoteid){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                JSONObject postParam = generateQuoteByQuoteId(quoteid);
                data = HttpRestUtil.sendPost(quoteUrl + "/find", postParam);
                System.out.println("getQuoteByQuoteId--data:" + data);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dataJsonObject = splitQuoteData(data);
        System.out.print("getQuoteByQuoteId--dataJsonObject:");
        System.out.println(dataJsonObject);
        if(dataJsonObject!=null){
            id = dataJsonObject.getString("_id");
            System.out.println("getQuoteByQuoteId--id:"+id);
        }
        return dataJsonObject;
    }
    //生成通过quoteid查询quote的语句

    public static JSONObject generateQuoteByQuoteId(String quoteid){
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonParam2 = new JSONObject();
        JSONObject jsonParam3 = new JSONObject();
        jsonParam.put("$eq",quoteid);
        jsonParam2.put("_id",jsonParam);
        jsonParam3.put("query",jsonParam2);
        System.out.println("generateQuoteByQuoteId--jsonParam3:"+jsonParam3);
        return jsonParam3;
    }
    //生成通过username查询收藏quoteidid列表
    public static JSONObject generateQuoteIdByUserName(String username){
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonParam2 = new JSONObject();
        JSONObject jsonParam3 = new JSONObject();
        jsonParam.put("$eq",username);
        jsonParam2.put("username",jsonParam);
        jsonParam3.put("query",jsonParam2);
        System.out.println("generateQuoteIdByUserName--jsonParam3:"+jsonParam3);
        return jsonParam3;
    }
}
