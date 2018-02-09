package com.example.mapsgps;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Registor extends AppCompatActivity {
    EditText e1;
    Button b1;
   // SQLiteDatabase db=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor);
        e1=(EditText)findViewById(R.id.editText);
        b1=(Button)findViewById(R.id.button2);
       // db=openOrCreateDatabase("college",MODE_PRIVATE,null);
       // db.execSQL("create table if not exists gps(longitude varchar(50),latitude varchar(50),place varchar(50))");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=getIntent();
                String logi=in.getStringExtra("logi");
                String lati=in.getStringExtra("lati");
                String name=e1.getText().toString();
               new ExecuteTask().execute(logi,lati,name);
               /* db.execSQL("insert into gps values('"+logi+"','"+lati+"','"+name+"')");
                Toast.makeText(getApplication(),"OK",Toast.LENGTH_LONG).show();*/

            }
        });
    }
   class ExecuteTask extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String res=PostData(params);
            return res;
        }

        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(getApplication(),result,Toast.LENGTH_LONG).show();
        }
        public String PostData(String[] values)
        {
            String s=" ";
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://192.168.43.57:8181/android/reg.php");
                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("longitude",values[0]));
                list.add(new BasicNameValuePair("latitude",values[1]));
                list.add(new BasicNameValuePair("place",values[2]));
                //list.add(new BasicNameValuePair("answer",values[3]));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=httpClient.execute(httpPost);
                HttpEntity httpEntity=httpResponse.getEntity();
                s=readResponse(httpResponse);
            }
            catch (Exception e)
            {

            }
            return s;
        }
        public String readResponse(HttpResponse res)
        {
            InputStream is=null;
            String return_text=" ";
            try
            {
                is=res.getEntity().getContent();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String line=" ";
                StringBuffer sb=new StringBuffer();
                while ((line=bufferedReader.readLine())!=null)
                {
                    sb.append(line);
                }
                return_text=sb.toString();

            }
            catch (Exception e)
            {

            }
            return return_text;
        }
    }
}
