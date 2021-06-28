package com.example.quote;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private String username;
    private String password;
    private String userid;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    public void init(){
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
    }

    public void registerBtn(View view) {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if(username==null||username.equals("")||password==null||password.equals("")) {
            Toast.makeText(this, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
        }else {
            JSONObject userJson = DataBaseUtil.getUserByUserName(username);
            if (userJson == null) {//数据库没有该用户名
                System.out.println(DataBaseUtil.getUserByUserName(username));
                System.out.println("为空");
                DataBaseUtil.register(username, password);
                userJson = DataBaseUtil.getUserByUserName(username);
                userid = userJson.getString("_id");
                MainActivity.username = username;
                MainActivity.password = password;
                MainActivity.userid = userid;
                writeToLocal();
                finish();
            } else {//数据库有改用户名
                //Toast 提示已注册
                Toast.makeText(getApplicationContext(), "该用户名已注册", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loginBtn(View view) {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if(username==null||username.equals("")||password==null||password.equals("")) {
            Toast.makeText(this, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
        }else {
            //数据库查找数据
            JSONObject userJson = DataBaseUtil.getUserByNameAndPwd(username, password);
            if (userJson == null) {
                //提示用户名不存在或密码错误
                Toast.makeText(getApplicationContext(), "用户名不存在或密码错误", Toast.LENGTH_SHORT).show();
            } else {
                MainActivity.username = userJson.getString("username");
                MainActivity.password = userJson.getString("password");
                MainActivity.userid = userJson.getString("_id");
                writeToLocal();
                finish();
            }
        }
    }



    public void writeToLocal(){
        SharedPreferences.Editor preferencesEditor = MainActivity.mPreferences.edit();
        preferencesEditor.putString("username",MainActivity.username);
        preferencesEditor.putString("password",MainActivity.password);
        preferencesEditor.putString("userid",MainActivity.userid);
        preferencesEditor.apply();
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }//屏蔽返回键
}