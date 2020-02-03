package com.scitmaster.miniproject.waterdietaryapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leehyungi on 02/03/2018.
 */

public class JoinActivity extends AppCompatActivity {

    String ipAddress = "192.168.0.2:8888";
    EditText id, name, gender, password,email;
    JSONArray jarray = null;
    JSONObject item = null;
    TextView validity, returnLogin;


    URL url;
    HttpURLConnection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_activity);

        /*
            Main Thread에서 네트워크 접속 가능하도록 설정
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        id = findViewById(R.id.joinId);
        name = findViewById(R.id.joinName);
        gender = findViewById(R.id.joinGender);
        password = findViewById(R.id.joinPassword);
        email = findViewById(R.id.joinEmail);

        validity = findViewById(R.id.validity);
        returnLogin = findViewById(R.id.returnLogin);

        returnLogin.setTextColor(Color.BLUE);
        returnLogin.setPaintFlags(returnLogin.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        returnLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //회원가입을 위한 버튼 이벤트
    public void join(View view){
        String jId = id.getText().toString();
        String jName = name.getText().toString();
        String jGender = gender.getText().toString();
        String jPassword = password.getText().toString();
        String jEmail = email.getText().toString();


        HashMap<String, String> params = new HashMap<>();
        params.put("id", jId);
        params.put("name", jName);
        params.put("gender",jGender);
        params.put("password",jPassword);
        params.put("email",jEmail);

        //요청시 보낼 쿼리스트림으로 변환
        String param = makeParams(params);

        try{
            //서버의 IP주소, PORT번호, Context root, Request Mapping경로
            url = new URL("http://"+ipAddress+"/www/insertMember");
        } catch (MalformedURLException e){
            Toast.makeText(this,"잘못된 URL입니다.", Toast.LENGTH_SHORT).show();
        }

        try{
            con = (HttpURLConnection) url.openConnection();

            if(con != null){

                con.setConnectTimeout(10000);	//연결제한시간. 0은 무한대기.
                con.setUseCaches(false);		//캐쉬 사용여부
                con.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
                con.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

                OutputStream os = con.getOutputStream();
                os.write(param.getBytes("UTF-8"));
                os.flush();
                os.close();

                if(con.getResponseCode() == HttpURLConnection.HTTP_OK){

                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                    String line;
                    String page = "";

                    while ((line = reader.readLine()) != null){
                        page += line;
                    }

                    Toast.makeText(this, page, Toast.LENGTH_SHORT).show();

                }

            }
        }catch (Exception e){
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if(con != null){
                con.disconnect();
            }
        }
    }



    public void idCheck(View view){
        String jId = id.getText().toString();

        HashMap<String,String> params = new HashMap<>();
        params.put("joinId", jId);

        //쿼리스트림으로 변환
        String param  = makeParams(params);

        System.out.println(param);

        try{
            //서버의 IP주소, PORT번호, Context root, Request Mapping경로
            //www(project명)뒤에 메서드 처리명 바꿔주기.
            url = new URL("http://"+ipAddress+"/www/checkId"+"?id="+jId);
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
        if(item !=null) {
            Toast.makeText(JoinActivity.this, item.toString(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(JoinActivity.this, "NULL!!", Toast.LENGTH_SHORT).show();
        }
        if(item != null) {
            //ID Check 부분
            validity.setText("This ID is already in use.(Not available)");
            validity.setTextColor(Color.RED);
            item = null;

        }else{

            validity.setText("This ID can be used.(Available)");
            validity.setTextColor(Color.BLUE);
        }
    }

    public void reset(View view){
        id.setText("");
        name.setText("");
        gender.setText("");
        password.setText("");
        email.setText("");
        validity.setText("Validity Check Text");
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


    }
