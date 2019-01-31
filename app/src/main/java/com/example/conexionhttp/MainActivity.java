package com.example.conexionhttp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    Button btnIr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText= findViewById(R.id.editText);
        textView=findViewById(R.id.textView);
        btnIr=findViewById(R.id.button);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Descargar();
            }
        });
    }

    public void Descargar(){

        new AsyncTaskDescarga().execute(editText.getText().toString());
    }


    private class AsyncTaskDescarga extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            try{
                return descargaUrl(strings[0]);
            }catch (IOException e){
                return "No se puede cargar la página web";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(s);
        }

        private String Leer(InputStream is){

            try{
                ByteArrayOutputStream bo= new ByteArrayOutputStream();
                int i=is.read();
                while (i!=-1){
                    bo.write(i);
                    i=is.read();
                }
                return bo.toString();
            }catch (IOException e){
                return"";
            }
        }

        private String descargaUrl(String miUrl)throws IOException{
            InputStream is=null;

            try{
                Log.w("Información",miUrl);
                URL url=new URL(miUrl);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(1500);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                is=conn.getInputStream();

                return Leer(is);
            }
            finally{
                if (is!=null){
                    is.close();
                }
            }

        }
    }


}
