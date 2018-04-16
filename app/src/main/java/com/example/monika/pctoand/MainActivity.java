package com.example.monika.pctoand;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    EditText input;
    String msg, MsgToSend;
    String userNameChattingWith;
    Handler handler;
    Handler handlerSent;
    ListView listView;
    Boolean send = false;
    String ts;
    Long tsLong;

    ArrayList<Message> list = new ArrayList<>();
    MessageAdapter adapter;

    String message = "connect#client 0";
    String msg1;
    boolean msgBlank = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.msg_input);
        handler = new Handler();
        handlerSent = new Handler();

        Intent intent = getIntent();
        userNameChattingWith = intent.getStringExtra("userToChatTo");
        TextView userHeader = findViewById(R.id.user_header);
        userHeader.setText(userNameChattingWith);

        adapter = new MessageAdapter(this,list);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        myTask mt = new myTask();
        mt.execute();

    }





    public void send_text(View v) {

        message = input.getText().toString();

        if(message.equals("")){
            Toast.makeText(this,"Cannot Send Blank Message",Toast.LENGTH_LONG).show();
            msgBlank = true;
        }else {

            msg1 = message;
            //tsLong = System.currentTimeMillis()/1000;
           //ts = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "";
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
//            String ts = simpleDateFormat.format(new Date());
            MsgToSend = message + "#" + userNameChattingWith;

            list.add(new Message(message, true));
            adapter.notifyDataSetChanged();

            myTaskSend mt2 = new myTaskSend();
            mt2.execute(MsgToSend);

            msgBlank = false;

        }
    }



    class myTaskSend extends AsyncTask<String,String,String>{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if(!MsgToSend.equals("") && msgBlank == false) {

                try {
                    FirstPage.dos.writeUTF(MsgToSend);

                    FirstPage.dos.flush();
                    send = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MsgToSend = "";
            }
            return null;
        }
    }

    class myTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {



            Thread readMessage = new Thread(new Runnable()
            {
                @Override
                public void run() {

                    while (true) {
                        try {

                            msg = FirstPage.dis.readUTF();
                            if(!msg.equals("") || msg!=null)
                            {

                                handler.post(new Runnable() {
                                    public void run() {
                                        list.add(new Message(msg,false));
                                        adapter.notifyDataSetChanged();

                                    }
                                });

                            }
                        } catch (IOException e) {

                            e.printStackTrace();
                        }

                    }
                }
            });

            readMessage.start();


            return null;
        }
    }
}
