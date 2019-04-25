package as.hn.com.hnprojectapp;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hn.business.Data.ChatEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import as.hn.com.hnprojectapp.Teams.ChatFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareFilePagerActivity extends MyActivityBase {

    public static final String TAG = "SharedFileActivity";
    @BindView(R.id.share_file_pager)
    protected ViewPager mViewPager;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.tab_layout)
    protected TabLayout mTabLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_file_pager);

        ButterKnife.bind(this);

        //设置toolbar
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("文件共享");

        if(mViewPager != null){
            Log.i(TAG,"setUpViewPager");
            setUpViewPager(mViewPager);
        }

        mTabLayout.setupWithViewPager(mViewPager);
        Log.i(TAG,"oncreate()");


    }

    private void setUpViewPager(ViewPager viewPager){
        Log.i(TAG,"addFragments");
        FragmentManager fm = getSupportFragmentManager();
        ShareFileAdapter adapter = new ShareFileAdapter(fm);
        adapter.addFragment(ShareFileFragment.newInstance(ShareFileFragment.UPLOAD),"已上传");
        adapter.addFragment(ShareFileFragment.newInstance(ShareFileFragment.RECEIVE),"已接收");
        adapter.addFragment(ShareFileFragment.newInstance(ShareFileFragment.LOADING),"正在传输");
        viewPager.setAdapter(adapter);
        Log.i(TAG,adapter.getCount() + "viewpager.setAdapter");

    }
    //设置菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG,"onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_add_file,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //设置菜单选择项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.search_file:
                Intent intent = new Intent(this, SearchFileActivity.class);
                startActivity(intent);
                break;
            case R.id.new_file:
                Intent intent1 = new Intent(this, AddFileActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //定义针对三个不同的文件分享fragment的adapter
    static class ShareFileAdapter extends FragmentPagerAdapter{
        private final List<String> mTitles = new ArrayList<>();
        private final List<Fragment> mShareFileFragments = new ArrayList<>();

        //构造adapter传入的参数是fm，里面可以添加若干个fragments
        public ShareFileAdapter(FragmentManager fm){
            super(fm);
        }

        public void addFragment(Fragment fragment, String title){
            mShareFileFragments.add(fragment);
            mTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mShareFileFragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            //设置fragment的标题文字
            return mTitles.get(position);
        }

        @Override
        public int getCount() {
            return mShareFileFragments.size();
        }
    }
}
