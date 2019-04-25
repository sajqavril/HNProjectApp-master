package as.hn.com.hnprojectapp.Teams;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import as.hn.com.hnprojectapp.MyActivityBase;
import as.hn.com.hnprojectapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class ChatFragmentActivityBase extends MyActivityBase {
    //toolbar的标题文字
    @BindView(R.id.chat_title)
    protected TextView textView;

    //back按键
    @BindView(R.id.back_button)
    protected ImageView backImageView;

    //menu按键
    @BindView(R.id.menu_button)
    protected ImageView menuImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //黄油刀绑定控件
        ButterKnife.bind(this);

        //把传回来的Title设置为toolbar的标题
        textView.setText(getIntent().getStringExtra("chatTitle"));

        //绑定按键
        backImageView.setOnClickListener(new ChatActivity.BackListener());
        menuImageView.setOnClickListener(new ChatActivity.MenuListener());

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.chat_fragment_container);
        if(fragment==null){
            fragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.chat_fragment_container,fragment).commit();
        }

    }

    class BackListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            //结束当前界面
            finish();

        }
    }

    class MenuListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            //展示menu

        }
    }



    //抽象方法，子类去复写
    protected abstract Fragment createFragment();

}
