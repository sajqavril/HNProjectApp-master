package as.hn.com.hnprojectapp.Teams;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hn.business.Data.ServiceHelper;
import com.hn.business.Data.UserInfoEntity;

import java.util.List;

import as.hn.com.hnprojectapp.Appplan;
import as.hn.com.hnprojectapp.LoginActivity;
import as.hn.com.hnprojectapp.MyActivityBase;
import as.hn.com.hnprojectapp.PlanListActivity;
import as.hn.com.hnprojectapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamListActivity extends MyActivityBase {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private List<UserInfoEntity> userList;
    private TeamUserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region 加载数据
        ServiceHelper serviceHelper = new ServiceHelper();
        userList = serviceHelper.GetUserTeams(getMyUser().getUserToken());

        adapter = new TeamUserListAdapter(userList, TeamListActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //endregion
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plan_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addplan) {
            //新增计划
            Intent intent = new Intent(TeamListActivity.this, Appplan.class);
            startActivity(intent);
        } else if (id == R.id.action_login) {
            //打开登陆页面
            Intent intent = new Intent(TeamListActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            //打开主页
            TeamListActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
