package as.hn.com.hnprojectapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddChatActivity extends MyActivityBase {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);
        ButterKnife.bind(this);

        FragmentManager fm = getSupportFragmentManager();
        Fragment searchFragment = new SearchFragment();

        //第一个搜索框fragment
        fm.beginTransaction().add(R.id.new_chat_frame1,searchFragment);

        //显示其他内容的fragment 放在frame2中



        fm.beginTransaction().commit();
    }
}
