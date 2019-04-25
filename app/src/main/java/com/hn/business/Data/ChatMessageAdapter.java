package com.hn.business.Data;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import as.hn.com.hnprojectapp.CrashApplication;
import as.hn.com.hnprojectapp.R;

//用于展示某个chatId中最新消息的adapter

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {
    //收到一串信息
    private List<ChatMessage> mMessageList;

    //定义子类：ViewHolder，用于绑定每一个空间和view
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View view){
            super(view);
            leftLayout=view.findViewById(R.id.left_layout);
            rightLayout=view.findViewById(R.id.right_layout);
            leftMsg=view.findViewById(R.id.left_msg);
            rightMsg=view.findViewById(R.id.right_msg);
        }


    }
    public ChatMessageAdapter(List<ChatMessage> msgList){
        //构造针对一串信息的adapter
        mMessageList = msgList;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //onCreateViewHolder()用于创建ViewHolder实例,绑定最外层的布局，以便bindView
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_fragment,parent,false);
        return new ViewHolder(view);
        //把加载出来的布局传到构造函数中，再返回
    }

    //对每一项数据操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int position){
        //onBindViewHolder()用于对RecyclerView子项的数据进行赋值，会在每个子项被滚动到屏幕内的时候执行
        ChatMessage message = mMessageList.get(position);
        //如果一条消息的sender和保存的登录用户账号相同，那么type==TYPE_SENT
        message.setType(CrashApplication.getInstance().getSharedPreferences("mypsd",Context.MODE_PRIVATE).getString("USER_CODE", null));

        //如果是接收信息
        if(message.getType() == ChatMessage.TYPE_RECEIVED){
            //增加对消息类的判断，如果这条消息是收到的，显示左边布局，是发出的，显示右边布局
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(message.getMessage());
        }else if(message.getType() == ChatMessage.TYPE_SENT) {
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(message.getMessage());
        }
    }
    @Override
    public int getItemCount(){
        return mMessageList.size();
    }
}
