package com.example.monika.pctoand;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by monika on 4/12/18.
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, ArrayList<Message> msg_list){
        super(context,0,msg_list);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Message item = getItem(position);

            if (item.getFlag()) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.msg_sent, parent, false);

                TextView sending = convertView.findViewById(R.id.msg_item);
                sending.setText(item.getMessage());
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.msg_rcvd, parent, false);

                TextView receiving = convertView.findViewById(R.id.msg_rcv_item);
                receiving.setText(item.getMessage());
            }


        return convertView;
    }
}
