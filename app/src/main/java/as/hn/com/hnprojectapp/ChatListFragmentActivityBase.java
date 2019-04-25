package as.hn.com.hnprojectapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class ChatListFragmentActivityBase extends MyActivityBase {
    @BindView(R.id.add_chat)
    protected ImageView add;

    @BindView(R.id.back)
    protected ImageView back;

    @BindView(R.id.chat_list_fragment_container)
    protected FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        ButterKnife.bind(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.chat_list_fragment_container);
        if(fragment==null){
            fragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.chat_list_fragment_container,fragment).commit();
        }



        //绑定两个按键
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChatListFragmentActivityBase.this,AddChatActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //结束当前页面，返回上一级
                finish();
            }
        });
    }

    public abstract Fragment createFragment();
}
