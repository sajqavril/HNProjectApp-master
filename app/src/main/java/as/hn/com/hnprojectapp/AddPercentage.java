package as.hn.com.hnprojectapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.Slider;
import com.hn.business.Data.PlanPercentageEntity;
import com.hn.business.Data.ProjectPlanEntity;
import com.hn.business.Data.ServiceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPercentage extends MyActivityBase {

    private ProjectPlanEntity entity;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.endPercentage)
    Slider endPercentage;
    @BindView(R.id.txtPlanTitle)
    TextView txtPlanTitle;
    @BindView(R.id.btSave)
    ButtonRectangle btSave;
    @BindView(R.id.txtRemark)
    EditText txtRemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_percentage);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        entity = (ProjectPlanEntity) intent.getSerializableExtra("plan");
        txtPlanTitle.setText(entity.getPlanTitle());

        endPercentage.setMin(entity.getEndPercentage());

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region 保存进度数据
                int iPlanId = entity.getPlanId();
                String strUserCode = getMyUser().getUserCode();

                int iEndPercentage = endPercentage.getValue();
                boolean isEnd = false;
                if (iEndPercentage == 100) {
                    isEnd = true;
                }

                String strRemark = txtRemark.getText().toString();
                PlanPercentageEntity item = new PlanPercentageEntity(iPlanId, strUserCode, iEndPercentage, isEnd, strRemark);

                ServiceHelper serviceHelper = new ServiceHelper();
                if (serviceHelper.AddPlanPercentageEntity(item, getMyUser())) {
                    ToViewActivity();
                } else {

                    new AlertDialog.Builder(AddPercentage.this)
                            .setTitle("消息提示")
                            .setMessage("保存出现异常")
                            .setPositiveButton("确定", null)
                            .show();

                }
                //endregion
            }
        });
    }

    private void ToViewActivity() {

        //重新去服务器提取数据
        int iPlanId = entity.getPlanId();
        ServiceHelper serviceHelper = new ServiceHelper();
        entity = serviceHelper.GetProjectPlanEntity(iPlanId);

        Intent intent = new Intent(AddPercentage.this, PlanVIewActivity.class);
        intent.putExtra("plan", entity);
        startActivity(intent);
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
            AddPercentage.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
