package com.example.monika.pctoand;

/**
 * Created by monika on 4/12/18.
 */

public class Message {

    private String message;
    private boolean flag;//true means sentmsg false is rcvdmsg

    public Message(String message,boolean flag){
        this.message = message;
        this.flag = flag;
    }

    public String getMessage(){
        return message;
    }

    public boolean getFlag(){
        return flag;
    }
}
