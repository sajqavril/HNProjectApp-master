package as.hn.com.hnprojectapp;


import android.content.Context;
import android.support.v4.app.Fragment;


public class ChatListActivity extends ChatListFragmentActivityBase{



    @Override
    public Fragment createFragment() {
        //获取当前的登录用户code
        String userCode = getApplication().getSharedPreferences("mypsd",Context.MODE_PRIVATE).getString("USER_CODE", null);
        Fragment fragment = ChatListFragment.newInstance(userCode);
        return fragment;
    }
}
