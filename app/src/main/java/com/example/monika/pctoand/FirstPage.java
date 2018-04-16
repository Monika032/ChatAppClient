package com.example.monika.pctoand;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class FirstPage extends AppCompatActivity {

    public static Socket s;
    public static DataOutputStream dos;
    public static DataInputStream dis;
    private static String ip = "10.100.127.219";
    String name;
    boolean blank=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
    }

    public void loginMethod(View view){


        EditText user=findViewById(R.id.user_name);
        name=user.getText().toString();

        if (name.equals("")){
            Toast.makeText(this,"Enter A Username",Toast.LENGTH_LONG).show();
            blank = true;
        }else {
            blank=false;

            name = name.toUpperCase();

            myTask mt = new myTask();
            mt.execute();

            Intent intent = new Intent(FirstPage.this, Online_List.class);
            intent.putExtra("client_name", name);
            startActivity(intent);
        }

    }


    class myTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                s = new Socket(ip, 1234);
                dos = new DataOutputStream(s.getOutputStream());
                dis = new DataInputStream(s.getInputStream());
                dos.writeUTF(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
