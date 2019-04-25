package com.hn.business.Data;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import as.hn.com.hnprojectapp.CrashApplication;
import as.hn.com.hnprojectapp.R;
import as.hn.com.hnprojectapp.Teams.ChatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private List<ChatEntity> ChatList;

    public ChatListAdapter(){
        this.ChatList = new ArrayList<>();}

    public ChatListAdapter(List<ChatEntity> chatList){
        this.ChatList = chatList;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建用于管理
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //拿到单个的ChatEntity与viewHolder内的每一项view进行对接
        ChatEntity chatEntity = ChatList.get(position);

        holder.groupFace.setImageDrawable(chatEntity.getChatFace());
        holder.groupTitle.setText(chatEntity.getChatName());
        if(chatEntity.ChatMessages.size()<1){
            holder.groupMessage.setText(chatEntity.ChatMessages.get(0).getMessage());
            holder.chatTime.setText(chatEntity.ChatMessages.get(0).getSendTime().toString());
        }else {
            holder.groupMessage.setText(chatEntity.ChatMessages.get(chatEntity.ChatMessages.size() - 1).getMessage());
            holder.chatTime.setText(chatEntity.ChatMessages.get(chatEntity.ChatMessages.size() - 1).getSendTime().toString());

        }
        holder.ifNotify.setImageDrawable(chatEntity.Notification? CrashApplication.getInstance().getDrawable(R.drawable.bell_on): CrashApplication.getInstance().getDrawable(R.drawable.bell_off));
    }

    @Override
    public int getItemCount() {
        return ChatList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.group_face)
        protected ImageView groupFace;

        @BindView(R.id.group_name)
        protected TextView groupTitle;

        @BindView(R.id.group_latest_message)
        protected TextView groupMessage;

        @BindView(R.id.chat_time)
        protected TextView chatTime;

        @BindView(R.id.group_if_notify_signal)
        protected ImageView ifNotify;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            //点击单个的view，进入另一个activity

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    intent.putExtra("chatTitle",groupTitle.getText());
                    startActivity(intent);
                }
            });


        }
    }
}
