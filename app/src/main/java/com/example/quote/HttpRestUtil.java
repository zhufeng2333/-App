package com.example.quote;


//import com.alibaba.fastjson.JSONObject;



import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;


public class HttpRestUtil {

//    private static HttpRestUtil httpRestUtil = new HttpRestUtil();
//    private HttpRestUtil(){}
//    public static HttpRestUtil getInstance(){
//        return httpRestUtil;
//    }
    /**
     * java调用运程api公共方法
     *
     * @param requestUrl
     * @param params
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String httpRequest(String requestUrl, Map params) throws Exception {
        // buffer用于接受返回的字符
        StringBuffer buffer = new StringBuffer();

        // 建立URL，把请求地址给补全，其中urlencode（）方法用于把params里的参数给取出来
        URL url = new URL(requestUrl + "?" + urlencode(params));
        // 打开http连接
        HttpURLConnection httpUrlConn = (HttpURLConnection) url
                .openConnection();
        httpUrlConn.setDoInput(true);
        httpUrlConn.setRequestMethod("GET");
        httpUrlConn.connect();

        // 获得输入
        InputStream inputStream = httpUrlConn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(
                inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        // 将bufferReader的值给放到buffer里
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        // 关闭bufferReader和输入流
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        inputStream = null;
        // 断开连接
        httpUrlConn.disconnect();

        // 返回字符串
        return buffer.toString();
    }

    public static String sendPost(String url, JSONObject param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 设置连接超时时间
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("Content-type", "application/json;charset=utf-8");
            conn.setRequestProperty("Charset", "UTF-8");

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (ConnectException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

//    HttpResponse response = null;
//    BufferedReader rd = null;
//    StringBuffer result = new StringBuffer();
//    String line = "";
//    HttpClient httpclient = HttpClients.createDefault();
//    HttpPatch httpPatch = new HttpPatch("http://myURL");
//    JsonArrayBuilder Abuilder = Json.createArrayBuilder();
//    JsonObjectBuilder oBuilder = Json.createObjectBuilder();
//    for(int i=0;i<48;i++){
//            Abuilder.add(i+1);
//        }
//    oBuilder.add("date", "2016-09-08");
//    oBuilder.add("values",Abuilder);
//    JsonObject jo = Json.createObjectBuilder().add("puissance", Json.createObjectBuilder().add("curves",Json.createArrayBuilder().add(oBuilder))).build();
//
//    public static String sendPatch(String url, JSONObject param) {
//        HttpClient httpclient = HttpClients.createDefault();
//        HttpPatch httpPatch = new HttpPatch("http://myURL");
//        try {
//            StringEntity params = new StringEntity(param.toString());
//            params.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//            httpPatch.setEntity(params);
//            response = httpclient.execute(httpPatch);
//            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
//            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//            while ((line = rd.readLine()) != null) {
//                System.out.println(line);
//                result.append(line);
//            }
//        } catch (Exception e) {
//
//        }
//    }

    /**
     * 请求参数拼接（组装）
     *
     * @param data
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String urlencode(Map data) {
        // 将map里的参数变成像 showapi_appid=###&showapi_sign=###&的样子
        StringBuilder sb = new StringBuilder();
        for (Object i : data.entrySet()) {
            try {
                sb.append(((Map.Entry)i).getKey()).append("=")
                        .append(URLEncoder.encode(((Map.Entry)i).getValue() + "", "UTF-8"))
                        .append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if(sb.length()>0)return sb.substring(0,sb.length()-1);
        return sb.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPostHttpRequest(String url, JSONObject param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 1.获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 2.中文有乱码的需要将PrintWriter改为如下
            // out=new OutputStreamWriter(conn.getOutputStream(),"UTF-8")
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String getJsonData(JSONObject jsonParam, String urls) {
            StringBuffer sb=new StringBuffer();
            try {
                ;
                // 创建url资源
                URL url = new URL(urls);
                // 建立http连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 设置允许输出
                conn.setDoOutput(true);
                // 设置允许输入
                conn.setDoInput(true);
                // 设置不用缓存
                conn.setUseCaches(false);
                // 设置传递方式
                conn.setRequestMethod("POST");
                // 设置维持长连接
                conn.setRequestProperty("Connection", "Keep-Alive");
                // 设置文件字符集:
                conn.setRequestProperty("Charset", "UTF-8");
                // 转换为字节数组
                byte[] data = (jsonParam.toString()).getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(data.length));
                // 设置文件类型:
                conn.setRequestProperty("contentType", "application/json");
                // 开始连接请求
                conn.connect();
                OutputStream out = new DataOutputStream(conn.getOutputStream()) ;
                // 写入请求的字符串
                out.write((jsonParam.toString()).getBytes());
                out.flush();
                out.close();
                System.out.println(conn.getResponseCode());

                // 请求返回的状态
                if (HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                    System.out.println("连接成功");
                    // 请求返回的数据
                    InputStream in1 = conn.getInputStream();
                    try {
                        String readLine=new String();
                        BufferedReader responseReader=new BufferedReader(new InputStreamReader(in1,"UTF-8"));
                        while((readLine=responseReader.readLine())!=null){
                            sb.append(readLine).append("\n");
                        }
                        responseReader.close();
                        System.out.println(sb.toString());

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    System.out.println("error++");

                }
            } catch (Exception e) {
            }

            return sb.toString();
        }
//        public static void main(String[] args) {
//            //返回的是一个[{}]格式的字符串时:
//            JSONArray jsonArray = new JSONArray(data);
//            //返回的是一个{}格式的字符串时:
//            /*JSONObject obj= new JSONObject(data);*/
//        }

}