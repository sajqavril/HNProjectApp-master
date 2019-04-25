package as.hn.com.hnprojectapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.business.Data.ServiceHelper;
import com.hn.business.Data.UserInfoEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFileActivity extends MyActivityBase {

    @BindView(R.id.editview)
    EditText mEditText;

    @BindView(R.id.search_icon)
    ImageView searchIcon;

    private String searchWord;

    //存储自己要展示的搜索结果
    private List<FileEntity> mFiles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);
        ButterKnife.bind(this);
        //生成一个ServiceHelper对象用于搜索数据库
        final ServiceHelper serviceHelper = new ServiceHelper();

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //当按下enter
                if(keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() || i == EditorInfo.IME_ACTION_DONE){
                    searchWord = textView.getText().toString();
                    textView.setText("");
                    UserInfoEntity mUser = serviceHelper.LocalToUser(getApplicationContext());

                    //拿到自己账号在数据库中的所有文件
                    serviceHelper.getSearchFileEntities(mUser.getUserCode(),mUser.getPwdCode(),searchWord);

                    //生成一个检索结果的展示页面
                    FragmentManager fm = getSupportFragmentManager();
                    Fragment searchResultListFragment = ShareFileFragment.newInstance("search",searchWord);
                    fm.beginTransaction().add(R.id.frame_layout,searchResultListFragment).commit();

                    //search的icon改变成为返回键
                    searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_arrow_black,null));

                }
                return false;
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击icon回到文件页面
                Intent intent = new Intent(SearchFileActivity.this, ShareFilePagerActivity.class);
                startActivity(intent);
            }
        });



    }
}
