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

//Nos hacemos cargo de la interfaz de usuario captando sus elementos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText= findViewById(R.id.editText);
        textView=findViewById(R.id.textView);
        btnIr=findViewById(R.id.button);
    }

//Cuando pulse el botón, iniciamos la AsyncTask que se encarga del trabajo sin bloquear la UI. Le enviamos como argumento un string con la url que el usuario ha introducido en el editText

    @Override
    protected void onStart() {
        super.onStart();
        btnIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              new AsyncTaskDescarga().execute(editText.getText().toString());
            }
        });
    }

//Definimos la clase AsyncTask

    private class AsyncTaskDescarga extends AsyncTask<String,Void,String>{

//No tenemos onPreExecute(), por lo primero que se ejecutará sera el doInBackground. A ese le llegan los params que llegan a través de strings (url).

        @Override
        protected String doInBackground(String... strings) {

            try{
                return descargaUrl(strings[0]); //Llamamos al método descargaUrl envíandole como argumento un string con la url
            }catch (IOException e){
                return "No se puede cargar la página web";
            }
        }

//Cuando doInBackground haya terminado con la lectura, nos devolvera un string con el texto que debemos presentar en el textView

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(s);
        }

//Este método recibe un string con la url a la que queremos conectar y tiene que devolver un string con el contenido del la web leída

        private String descargaUrl(String miUrl)throws IOException{
            InputStream is=null;

            try{

                URL url=new URL(miUrl); //crea un objeto URL que tiene como argumento el string que ha introducido el usuario
                HttpURLConnection conn=(HttpURLConnection) url.openConnection(); //creamos un objeto para abrir la conexión con la url indicada
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(1500);
                conn.setRequestMethod("GET"); //indicamos que vamos a traer elementos
                conn.setDoInput(true);

                conn.connect(); //conectamos con la url
                is=conn.getInputStream(); //obtenemos un flujo de datos de entrada desde la conexión establecida

                return Leer(is); //llamamos a este método que se encargará de tomar el flujo de entrada y convertirlo en un string, que devolveremos a doInBackground
            }
            finally{
                if (is!=null){
                    is.close();
                }
            }

        }
        private String Leer(InputStream is){

            try{
                ByteArrayOutputStream bo= new ByteArrayOutputStream(); //creamos un array de bytes en el que escribir
                int i=is.read(); //leemos un byte de el flujo de entrada
                while (i!=-1){    //mientras haya bytes los escribimos en el array de bytes. Cuando se acabe, la lectura nos devuelve -1 para indicar que no hay más
                    bo.write(i);
                    i=is.read();
                }
                return bo.toString(); //devolvemos un string a descargaUrl que a su vez lo devolverá a doInBackground que se lo dará a onPostExecute que lo escribirá en el textView
            }catch (IOException e){
                return"";
            }
        }
    }


}
