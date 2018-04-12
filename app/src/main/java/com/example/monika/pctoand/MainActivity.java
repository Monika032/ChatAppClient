package com.example.monika.pctoand;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> listview;

    String message = "connect#client 0";
    String msg1;
    private static String ip = "10.100.127.219";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.msg_input);
        msgShow = findViewById(R.id.msg_received);
        msgSent = findViewById(R.id.msg_sent);
        handler = new Handler();
        handlerSent = new Handler();


    }

    public void send_text(View v){

        message = input.getText().toString();
        msg1 = message;
        StringTokenizer st = new StringTokenizer(msg1, "#");
        MsgToSend = st.nextToken();
        list.add(MsgToSend);

        listview = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,list);

        myTask mt = new myTask();
        mt.execute();

        Toast.makeText(getApplicationContext(),"Data Sent",Toast.LENGTH_LONG).show();


    }

    void printMsg (String msgRcd){
        msgShow.setText(msgRcd);
    }


    class myTask extends AsyncTask<Void,Void,Void>{

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
                                    handlerSent.post(new Runnable() {
                                        public void run() {
                                            msgSent.setText(MsgToSend);
                                        }
                                    });
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
                                msg = dis.readUTF();
                                Log.d("Error:","fuck you");
                                //System.out.println(msg);
                                if(!msg.equals(""))
                                {
                                    list.add(msg);
                                    //msgShow.setText(msg);
                                    handler.post(new Runnable() {
                                                     public void run() {
                                                         msgShow.setText(msg);
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



                sendMessage.start();
                readMessage.start();

            }catch (IOException e){
                e.printStackTrace();
            }


            return null;
        }
    }
}
