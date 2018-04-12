package com.example.monika.pctoand;

import android.os.AsyncTask;
import android.os.Handler;
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
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    EditText input;
    private static Socket s;
//    private static PrintWriter pw;
    DataOutputStream dos;
    DataInputStream dis;
//    TextView msgShow;
    String msg, MsgToSend;
    Handler handler;
    Handler handlerSent;
    ListView listView;
    Boolean send = false;
//    TextView msgSent;
    boolean flag = false;

    ArrayList<Message> list = new ArrayList<>();
    //ArrayAdapter<String> listAdapter;
    MessageAdapter adapter;

    String message = "connect#client 0";
    String msg1;
    private static String ip = "10.100.127.219";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.msg_input);
        handler = new Handler();
        handlerSent = new Handler();

        //listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        adapter = new MessageAdapter(this,list);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        myTask mt = new myTask();
        mt.execute();

    }





    public void send_text(View v) {

        message = input.getText().toString();
        msg1 = message;
        StringTokenizer st = new StringTokenizer(msg1, "#");
        MsgToSend = st.nextToken();

        list.add(new Message(MsgToSend,true));
        adapter.notifyDataSetChanged();

        Log.d("ErrorList:",""+list.size());

        myTaskSend mt2 = new myTaskSend();
        mt2.execute(message);




        Toast.makeText(getApplicationContext(),"Data Sent",Toast.LENGTH_LONG).show();
    }



    class myTaskSend extends AsyncTask<String,String,String>{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if(!message.equals("")) {

                try {
                    dos.writeUTF(message);
                    dos.flush();
                    send = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                message = "";
            }
            return null;
        }
    }







    class myTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                s = new Socket(ip, 1234);
                dos = new DataOutputStream(s.getOutputStream());
                dis = new DataInputStream(s.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Thread readMessage = new Thread(new Runnable()
            {
                @Override
                public void run() {

                    while (true) {
                        try {

                           // msg = "";
                            msg = dis.readUTF();
                            Log.d("Error:",msg);
                            //System.out.println(msg);
                            if(!msg.equals("") || msg!=null)
                            {
//                                    msgShow.setText(msg);

                                handler.post(new Runnable() {
                                    public void run() {
                                        list.add(new Message(msg,false));
                                        adapter.notifyDataSetChanged();
                                        Log.d("ErrorList:",""+list.size());

                                    }
                                });

                                Log.d("Error1:",msg);
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
