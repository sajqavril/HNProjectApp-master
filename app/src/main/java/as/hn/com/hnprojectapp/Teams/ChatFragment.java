package as.hn.com.hnprojectapp.Teams;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.hn.business.Data.ChatMessage;
import com.hn.business.Data.ChatMessageAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import as.hn.com.hnprojectapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatFragment extends Fragment {

    public static final String CHAT_ID = "chatId to be shown in this fragment";
    @BindView(R.id.input_text)
    protected EditText input;

    @BindView(R.id.send)
    protected Button send;

    @BindView(R.id.chat_fragment_recycler_view)
    protected RecyclerView mRecylerView;

    //要展示的ChatMessages的list
    protected List<ChatMessage> mChats;

    protected UUID chatId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //从chatActivity那里拿到要生成的fragment的相应的信息，chatID UUID
        chatId = (UUID) getArguments().getSerializable(CHAT_ID);


    }

    @Nullable
    @Override
    //activity给了一个inflater给fragment来填充空白区域
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);
        ButterKnife.bind(this,view);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //缺少一个方法：从Chat数据库中获取chat message的方法
        //mChats = ChatsDB.getChatById(chatId);
        //test region begin
        mChats = new ArrayList<>();
        ChatMessage message = new ChatMessage("1234",UUID.randomUUID());
        for(int i = 0; i<20; i++) {
            message.setType(1);
            mChats.add(message);
            message.setType(0);
            mChats.add(message);
        }

        //end test region

        //设置adapter自动检测数据更新
        mRecylerView.setAdapter(new ChatMessageAdapter(mChats));

        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() || i == EditorInfo.IME_ACTION_DONE){
                    //保存信息到对应的chatId 并附上sender信息
                    String inputString = textView.getText().toString();
                    ChatMessage message = new ChatMessage(inputString, chatId);

                    //test region
                    mChats.add(message);
                    mRecylerView.getAdapter().notifyDataSetChanged();
                    mRecylerView.scrollToPosition(mChats.size()-1);
                    input.setText("");
                    //end test region

                    //将信息保存放进chatId对应的数据库中，不知道数据库是不是只能放bundle
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("newMessage",message);
                    //ChatsDB.putBundle(bundle);
                    //隐藏软键盘
                    return false;
                }
                return false;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击send之后隐藏软键盘
                InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                //保存信息到对应的chatId 并附上sender信息
                String inputString = input.getText().toString();
                ChatMessage message = new ChatMessage(inputString, chatId);

                //test region
                mChats.add(message);
                mRecylerView.getAdapter().notifyDataSetChanged();
                mRecylerView.scrollToPosition(mChats.size()-1);
                input.setText("");
                //end test region

                //将信息保存放进chatId对应的数据库中，不知道数据库是不是只能放bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("newMessage",message);
                //ChatsDB.putBundle(bundle);
            }
        });

        return view;
    }

    

    //由ChatActivity来调用，传入chatId
    //取代原来的fragment的构造方法
    public static ChatFragment newInstance(UUID chatId){
        Bundle args = new Bundle();
        args.putSerializable(CHAT_ID,chatId);
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(args);
        return chatFragment;
    }
}


