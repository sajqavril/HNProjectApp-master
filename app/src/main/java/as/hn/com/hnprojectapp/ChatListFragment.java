package as.hn.com.hnprojectapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hn.business.Data.ChatEntity;
import com.hn.business.Data.ChatListAdapter;
import com.hn.business.Data.UserInfoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatListFragment extends Fragment {

    public static final String USER_CODE = "User_CODE";

    protected String mUserCode ;

    @BindView(R.id.chat_list_recyler_view)
    protected RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //从bundle里面拿到argument
        mUserCode = this.getArguments().getString(USER_CODE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_list_fragment, container, false);
        ButterKnife.bind(this,view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //拿到userCode对应的DB内部的该用户所有的chatgroup的信息
        //List<ChatEntity> chatList = DB.getChatEntitiesByUserCode(mUserCode);

        //test region begin
        List<ChatEntity> chatList = new ArrayList<>();
        List<UserInfoEntity> user = new ArrayList<>();
        user.add(new UserInfoEntity(123,"123","123","123","123"));
        user.add(new UserInfoEntity(456,"456","456","456","456"));

        for(int i = 0; i<10;i++){
            chatList.add(new ChatEntity(user));
        }
        //end test region
        mRecyclerView.setAdapter(new ChatListAdapter(chatList));

        return view;
    }

    public static ChatListFragment newInstance(String userCode){
        Bundle bundle = new Bundle();
        bundle.putString(USER_CODE,userCode);
        ChatListFragment chatListFragment = new ChatListFragment();
        chatListFragment.setArguments(bundle);

        return chatListFragment;

    }
}
