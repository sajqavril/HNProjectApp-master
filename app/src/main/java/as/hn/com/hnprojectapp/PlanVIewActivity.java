package as.hn.com.hnprojectapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hn.business.Data.PlanPercentageEntity;
import com.hn.business.Data.ProjectPlanEntity;
import com.hn.gc.views.CircleProgressView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlanVIewActivity extends MyActivityBase {

    private ProjectPlanEntity entity;
    private List<PlanPercentageEntity> perList;

    @BindView(R.id.plan_info_date)
    TextView base_swipe_item_Date;
    @BindView(R.id.plan_info_title)
    TextView base_swipe_item_Title;
    @BindView(R.id.plan_info_desc)
    TextView plan_info_desc;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_add_percentage)
    Button btnAdd;
    @BindView(R.id.btn_view_percentage)
    Button btnView;
    @BindView(R.id.circleProgressbar)
    CircleProgressView mCircleBar;
    @BindView(R.id.endPercentage)
    CircleProgressView mPercentageBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planview);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //region 加载数据

        Intent intent = getIntent();
        entity = (ProjectPlanEntity) intent.getSerializableExtra("plan");

        base_swipe_item_Date.setText(entity.GetPublishDate());
        base_swipe_item_Title.setText(entity.getPlanTitle());
        plan_info_desc.setText(entity.getPlanContent());
        //endregion

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开添加进度
                Intent intent = new Intent(PlanVIewActivity.this, AddPercentage.class);
                intent.putExtra("plan", entity);
                startActivity(intent);
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开所有进度情况

            }
        });

        //region 加载进度条数据

        mCircleBar.setProgress(entity.getTimeUsePercentage());
        mCircleBar.setmTxtHint1("使用了");
        mCircleBar.setmTxtHint2("的时间");

        mPercentageBar.setProgress(entity.getEndPercentage());
        mPercentageBar.setmTxtHint1("已完成");
        mPercentageBar.setmTxtHint2("工作量");
        //endregion

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            //打开主页
            //打开列表页面
//            Intent intent = new Intent(PlanVIewActivity.this, PlanListActivity.class);
//            startActivity(intent);
            PlanVIewActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
