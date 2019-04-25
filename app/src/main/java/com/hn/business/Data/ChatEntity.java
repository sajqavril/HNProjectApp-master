package com.hn.business.Data;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import as.hn.com.hnprojectapp.R;

//群聊实体
//包括群聊名称，群聊Id，群聊头像，群聊人员，消息是否可见，消息内容列表
//对于最新消息：最新消息实体，最新消息时间
//三种情况：单人（私密对话），team对话，自建讨论组对话

public class ChatEntity {
    //chatId是随机生成的
    protected UUID ChatId;
    protected String ChatName;
    //聊天头像
    protected Drawable ChatFace;
    //聊天人员
    protected List<UserInfoEntity> ChatMembers;
    //是否提示新消息
    protected boolean Notification;
    //消息内容 列表存储
    protected List<ChatMessage> ChatMessages;


    //以下有关：最新一条消息
    //消息的时间
    protected Date LatestMessageTime;

    //群聊的最新消息
    protected ChatMessage LatestMessage;

    public ChatEntity(){
        //无参构造方法
        this.Notification = true;
        this.ChatId = UUID.randomUUID();
        this.ChatMessages = new ArrayList<>();
        //this.LatestMessage = ChatMessages.get((ChatMessages.size()-1>=0)?(ChatMessages.size()-1):0);
        //this.LatestMessageTime = this.LatestMessage.getSendTime();

    }


    //以拉群或者点击单人的方式构建讨论组
    public ChatEntity(List<UserInfoEntity> chatMembers) {
        this();

        //test region
        //每一个chat用户加上默认的20条初始信息
        ChatMembers = chatMembers;
        ChatMessage message = new ChatMessage("1234",UUID.randomUUID());
        for(int i=0; i<20; i++){
            message.setType(i%2);
            ChatMessages.add(message);
        }
        ChatName = chatMembers.get(0).getUserName();
        //end test region

        //ChatMessages.size()>1? this.LatestMessage = ChatMessages.get(ChatMessages.size()-1) : this.LatestMessage = ChatMessages.get(0);

        //this.LatestMessageTime = this.LatestMessage.getSendTime();
    }

    //以team为单位创建对话
    public ChatEntity(TeamEntity teamEntity){
        this();
        this.ChatName = teamEntity.getTeamName();
        this.ChatMembers = teamEntity.getTeamMembers();

    }


    //得到，设置群聊名称
    public String getChatName() {
        return ChatName;
    }

    public void setChatName(String chatName) {
        ChatName = chatName;
    }

    //得到，设置群聊头像
    public Drawable getChatFace() {
        return ChatFace;
    }

    public void setChatFace(Drawable chatFace) {
        ChatFace = chatFace;
    }

    //得到，设置群聊成员
    public List<UserInfoEntity> getChatMembers() {
        return ChatMembers;
    }

    public void setChatMembers(List<UserInfoEntity> chatMembers) {
        ChatMembers = chatMembers;
    }

    //得到，设置群聊是否新消息提示
    public boolean isNotification() {
        return Notification;
    }

    public void setNotification(boolean notification) {
        Notification = notification;
    }

    //得到最新消息、时间
    public String getLatestMessageTime() {
        return LatestMessageTime.toString();
    }

    public String getLatestMessage() {
        return LatestMessage.toString();
    }

    //得到群聊的UUID
    public UUID getChatId() {
        return ChatId;
    }

    public List<ChatMessage> getChatMessages() {
        return ChatMessages;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        ChatMessages = chatMessages;
    }
}


