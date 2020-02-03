package com.scitmaster.miniproject.waterdietaryapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


//Login Activity
public class MainActivity extends AppCompatActivity {



    String ipAddress = "192.168.0.2:8888";
    EditText loginId;
    EditText loginPassword;

    TextView loginResult;

    JSONArray jarray = null;
    JSONObject item = null;
    URL url;
    HttpURLConnection con;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        Main Thread에서 네트워크 접속 가능하도록 설정
        */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        loginId = findViewById(R.id.loginId);
        StrictMode.setThreadPolicy(policy);
        loginPassword = findViewById(R.id.loginPassword);
        loginResult = findViewById(R.id.loginResult);

    }


    //Sign-Up 버튼 클릭시, 회원가입 페이지로 이동.
    public void joinPage(View v) {
        startActivity(new Intent(MainActivity.this, JoinActivity.class));
    }



    //Login 성공시, 메인메뉴로 이동.
    public void MainMenuPage(View v){

        String id = loginId.getText().toString();
        String password = loginPassword.getText().toString();

        HashMap<String,String> params = new HashMap<>();
        params.put("loginId", id);
        params.put("password", password);

        //쿼리스트림으로 변환
        String param  = makeParams(params);

        //Toast.makeText(MainActivity.this, param, Toast.LENGTH_LONG).show();
        System.out.println(param);

        try{
            //서버의 IP주소, PORT번호, Context root, Request Mapping경로
            //www(project명)뒤에 메서드 처리명 바꿔주기.
            url = new URL("http://"+ipAddress+"/www/selectMember"+"?id="+id+"&password="+password);
            //url = new URL("http://192.168.0.2:8888/www/selectMember");
        } catch (MalformedURLException e){
            Toast.makeText(this,"잘못된 URL입니다.", Toast.LENGTH_SHORT).show();
        }


        try{
            con = (HttpURLConnection) url.openConnection();

            if(con != null){
                con.setConnectTimeout(10000);	//연결제한시간. 0은 무한대기.
                con.setUseCaches(false);		//캐쉬 사용여부
                con.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

                if(con.getResponseCode() == HttpURLConnection.HTTP_OK){

                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                    String line;
                    String page = "";

                    while ((line = reader.readLine()) != null){
                        page += line;
                    }
                    jsonParse(page);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if(con != null){
                con.disconnect();
            }
        }

        //Toast.makeText(MainActivity.this,item.toString(),Toast.LENGTH_SHORT).show();

        if(item != null) {
            //login test 부분
            try {
                if (id.equals(item.getString("id")) && password.equals(item.getString("password"))) {

                    //로그인한 아이디 정보를 MainMenuActivity로 넘겨준다.
                    Toast.makeText(MainActivity.this,id,Toast.LENGTH_SHORT).show();

                    //MainMenuActivity로 loginId return값 보내기.
                    Intent intent = new Intent(this,MainMenuActivity.class);
                    intent.putExtra("returnValue",id);
                    startActivityForResult(intent,1);

                    Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{
            loginResult.setText("Please check your ID and Password");
            loginResult.setTextColor(Color.RED);
            //Toast.makeText(MainActivity.this,"Please check your ID and Password",Toast.LENGTH_SHORT).show();
        }

    }

    public void reset(View view){
        loginId.setText("");
        loginPassword.setText("");
    }



    //서버로 보낼 데이터를 쿼리 스트링으로 변환 ("?이름=값&이름2=값2" 형식)
    public String makeParams(HashMap<String,String> params){
        StringBuffer sbParam = new StringBuffer();
        String key = "";
        String value = "";
        boolean isAnd = false;

        for(Map.Entry<String,String> elem : params.entrySet()){
            key = elem.getKey();
            value = elem.getValue();

            if(isAnd){
                sbParam.append("&");
            }

            sbParam.append(key).append("=").append(value);

            if(!isAnd){
                if(params.size() >= 2){
                    isAnd = true;
                }
            }
        }

        return sbParam.toString();
    }

    public void jsonParse(String page){



        try{
            jarray = new JSONArray(page);

            StringBuilder sb2 = new StringBuilder();

            for(int i=0; i<jarray.length(); i++){
                item = jarray.getJSONObject(i);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==1){
            if(requestCode == RESULT_OK){
                Toast.makeText(this,data.getStringExtra("returnData"), Toast.LENGTH_SHORT).show();
            }
        }

        //requestCode
        //super.onActivityResult(requestCode, resultCode, data);
    }
}
