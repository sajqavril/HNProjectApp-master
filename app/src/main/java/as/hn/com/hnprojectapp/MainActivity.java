package as.hn.com.hnprojectapp;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.pgyersdk.update.PgyUpdateManager;

import com.hn.business.Data.ServiceHelper;


import java.util.ArrayList;
import java.util.List;

import as.hn.com.hnprojectapp.Teams.ChatActivity;
import as.hn.com.hnprojectapp.Teams.TeamListActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends MyActivityBase {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)//侧边导航栏
    NavigationView navigationView;

    @BindView(R.id.viewpager)//
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("20190228","main.oncreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //在setContent之后绑定butterknife
        ButterKnife.bind(this);

        //设置toolbar
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);


        if (navigationView != null) {
            View Headview = navigationView.getHeaderView(0);
            TextView tvUser = Headview.findViewById(R.id.currentUser);
            tvUser.setText(getMyUser().getUserName());
            setupDrawerContent(navigationView);
        }

        ServiceHelper serviceHelper = new ServiceHelper();
        //        serviceHelper.UserRegiter()

        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        tabLayout.setupWithViewPager(viewPager);

        //调用蒲公英的接口来自动更新
        PgyUpdateManager.register(MainActivity.this);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        int id = menuItem.getItemId();
                        Intent intent;
                        switch (id) {
                            case R.id.SetDay:
                                //日间模式
                                setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                break;
                            case R.id.SetNight:
                                //夜间模式
                                setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                break;
                            case R.id.nav_change:
                                //修改密码
                                intent = new Intent(MainActivity.this, PwdEditActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_friends:
                                //我的团队
                                intent = new Intent(MainActivity.this, TeamListActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_messages:
                                //我的对话
                                intent = new Intent(MainActivity.this, ChatListActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_files:
                                //文件共享
                                intent = new Intent(MainActivity.this, ShareFilePagerActivity.class);
                                startActivity(intent);
                                break;

                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    //设置界面
    private void setNightMode(@AppCompatDelegate.NightMode int nightMode) {
        AppCompatDelegate.setDefaultNightMode(nightMode);

        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        //向adapter里面加入不同的fragment
        adapter.addFragment(CheeseListFragment.newInstance("Current"), "进行中");
        adapter.addFragment(CheeseListFragment.newInstance("Overtime"), "已超时");
        adapter.addFragment(CheeseListFragment.newInstance("End"), "已完成");
        viewPager.setAdapter(adapter);
    }

    //region 下拉菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addplan) {
            //新增计划
            Intent intent = new Intent(MainActivity.this, Appplan.class);
            startActivity(intent);
        } else if (id == R.id.action_settings) {
            //打开列表页面
            Intent intent = new Intent(MainActivity.this, PlanListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_login) {
            //打开登陆页面
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    static class Adapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public Adapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
}
