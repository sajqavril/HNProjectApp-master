package as.hn.com.hnprojectapp.Teams;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hn.business.Data.ChatMessage;
import com.hn.business.Data.ChatMessageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import as.hn.com.hnprojectapp.MyActivityBase;
import as.hn.com.hnprojectapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

//复用ChatFragment
public class ChatActivity extends ChatFragmentActivityBase {


    @Override
    public Fragment createFragment(){
        //收到点击ChatListActivity中的ChatEntity对象的ChatId值，通过intent传送
        UUID chatId = (UUID) getIntent().getSerializableExtra("chat_id");
        Fragment chatFragment = ChatFragment.newInstance(chatId);
        return chatFragment;

    }


}
