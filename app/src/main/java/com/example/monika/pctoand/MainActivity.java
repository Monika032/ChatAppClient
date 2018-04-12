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
    private static PrintWriter pw;
    DataOutputStream dos;
    DataInputStream dis;
    TextView msgShow;
    String msg, MsgToSend;
    Handler handler;
    Handler handlerSent;
    TextView msgSent;
    boolean flag = false;

    ArrayList<Message> list = new ArrayList<>();
    //ArrayAdapter<String> listAdapter;
    static MessageAdapter adapter;

    String message = "connect#client 0";
    String msg1;
    private static String ip = "192.168.43.45";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.msg_input);
        handler = new Handler();
        handlerSent = new Handler();

        //listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        adapter = new MessageAdapter(this,list);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);


    }

    void updateApapter() {
        adapter.notifyDataSetChanged();
    }

    public void send_text(View v){

        message = input.getText().toString();
        msg1 = message;
        StringTokenizer st = new StringTokenizer(msg1, "#");
        MsgToSend = st.nextToken();

       // if(list.size() == 8) {
            //list.remove(0);
            //adapter.notifyDataSetChanged();
       // }
        list.add(new Message(MsgToSend,true));
        adapter.notifyDataSetChanged();
        Log.d("ErrorList:",""+list.size());


        myTask mt = new myTask();
        mt.execute();

        Toast.makeText(getApplicationContext(),"Data Sent",Toast.LENGTH_LONG).show();


    }


    class myTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            updateApapter();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                if(flag == false) {
                    s = new Socket(ip, 1234);
                    flag = true;
                }
//                pw = new PrintWriter(s.getOutputStream());
//                pw.write(message);
//                pw.flush();
               dos = new DataOutputStream(s.getOutputStream());
//                dos.writeUTF(message);
               dis = new DataInputStream(s.getInputStream());
//                String read = dis.readUTF();
//                msgShow.setText(read);

                Thread sendMessage = new Thread(new Runnable()
                {
                    @Override
                    public void run() {
                        while (true) {

                            // read the message to deliver.
                            //String msg = scn.nextLine();

                            try {
                                // write on the output stream
                                if(!message.equals("")) {
//                                    handlerSent.post(new Runnable() {
//                                        public void run() {
//                                            msgSent.setText(MsgToSend);
//                                        }
//                                    });

                                    dos.writeUTF(message);


                                    message = "";
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                // readMessage thread
                Thread readMessage = new Thread(new Runnable()
                {
                    @Override
                    public void run() {

                        while (true) {
                            try {
                                // read the message sent to this client
                            msg = "";
                                msg = dis.readUTF().;
                                Log.d("Error:",msg);
                                //System.out.println(msg);
                                if(!msg.equals("") || msg!=null)
                                {
//                                    msgShow.setText(msg);

                                   handler.post(new Runnable() {
                                                     public void run() {
                                                        //msgShow.setText(msg);
                                                         //if(list.size() == 8) {
                                                            // list.remove(0);
                                                             //adapter.notifyDataSetChanged();
                                                        // }
                                                         list.add(new Message(msg,false));
                                                         adapter.notifyDataSetChanged();

                                                         Log.d("ErrorList:",""+list.size());

                                                     }
                                                 });
//
//                                    list.add(new Message(msg,false));
//                                    onProgressUpdate(msg);
                                   //adapter.notifyDataSetChanged();

                                    Log.d("Error1:",msg);
                                }


                            } catch (IOException e) {

                                e.printStackTrace();
                            }

                        }
                    }
                });



                sendMessage.start();
                readMessage.start();

            }catch (IOException e){
                e.printStackTrace();
            }


            return null;
        }
    }
}
