package com.example.monika.pctoand;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Online_List extends AppCompatActivity {

    ArrayList<String> chatList;
    ArrayAdapter adapter;
    Handler handler;
    String myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online__list);
        handler = new Handler();

        chatList = new ArrayList<String>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,chatList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#E0F2F1"));

                return v;
            }
        };
        ListView onlinePeople = findViewById(R.id.online_list_items);
        onlinePeople.setAdapter(adapter);

        onlinePeople.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String userToChatTo = String.valueOf(parent.getItemAtPosition(position));
                        Intent intent = new Intent(Online_List.this,MainActivity.class);
                        intent.putExtra("userToChatTo",userToChatTo);
                        startActivity(intent);

                    }
                }
        );

        Intent intent = getIntent();
        myName = intent.getStringExtra("client_name");

    }

    public void updateList(View view){

        UserList myList = new UserList();
        myList.execute();

    }

    class UserList extends AsyncTask<String,String,String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

                try {
                    FirstPage.dos.writeUTF("StatusUpdate");
                    FirstPage.dos.flush();

                    String sizeStr = FirstPage.dis.readUTF();
                    int size = Integer.parseInt(sizeStr);
                    chatList.clear();
                    for(int i = 0; i<size; i++){
                        final String user = FirstPage.dis.readUTF();

                        if(!user.equals(myName)) {
                            Log.d("user", user);
                            handler.post(new Runnable() {
                                public void run() {
                                    chatList.add(user);
                                    adapter.notifyDataSetChanged();
                                    Log.d("ErrorList:", "" + chatList.size());

                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return null;
        }
    }

}
