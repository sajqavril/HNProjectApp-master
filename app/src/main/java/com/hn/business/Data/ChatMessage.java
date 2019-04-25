package com.hn.business.Data;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import as.hn.com.hnprojectapp.CrashApplication;

//定义一条信息
//是用于任何群聊的通用类，对于某一条特定消息类型
//包括：所属群聊id，发送者Code，发送时间
public class ChatMessage implements Serializable {

    public static final int TYPE_SENT = 1;
    public static final int TYPE_RECEIVED = 0;
    //目前先定义只能发送文字信息
    private String message;

    //消息类型
    private int type;

    //发送者id
    private String senderCode;

    //所属群聊id
    private UUID chatId;

    //发送时间
    public String sendTime;

    public ChatMessage(String message,  UUID chatId){
        ServiceHelper serviceHelper = new ServiceHelper();
        this.sendTime = serviceHelper.getCurrentTime();
        this.message = message;
        this.senderCode = CrashApplication.getInstance().getSharedPreferences("mypsd",Context.MODE_PRIVATE).getString("USER_CODE", null);
        //以用户身份构造的信息只能是发送type
        this.type = TYPE_SENT;
        this.chatId = chatId;
    }

    //在展示群聊的时候，获取消息类型
    public int getType(){
        return this.type;
    }

    public String getMessage(){
        return this.message;
    }

    //展示的时候,设置消息类型
    public void setType(String userCode) {
        this.type = ((this.senderCode==userCode)? TYPE_SENT : TYPE_RECEIVED);
    }

    public void setType(int i){
        this.type = i;
    }

    public String getSendTime(){
        return this.sendTime;
    }
}
