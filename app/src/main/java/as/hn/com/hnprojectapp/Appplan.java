package as.hn.com.hnprojectapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.hn.business.Data.ProjectPlanEntity;
import com.hn.business.Data.ServiceHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Appplan extends MyActivityBase {

    @BindView(R.id.txtTitle)EditText txtTitle;
    @BindView(R.id.txtContent)EditText txtContent;
    @BindView(R.id.editText)EditText editText;
    @BindView(R.id.editEndText)EditText editEndText;
    @BindView(R.id.btSave)com.gc.materialdesign.views.ButtonRectangle btSave;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnDate)
    Button btnDate;
    @BindView(R.id.btnEndDate)
    Button btnEndDate;

    //region 日期选择的相关方法
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            //一旦开始日期发生了改变，就需要将结束日期设置成空日期
            EditText editEndText = (EditText) findViewById(R.id.editEndText);
            editEndText.setText("");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appplan);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region 日期操作方法
        editText.addTextChangedListener(watcher);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusable(false);

        editEndText.setInputType(InputType.TYPE_NULL);
        editEndText.setFocusable(false);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date dt = new Date();
                onCreateDialog(editText, dt).show();
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //结束日期要获取开始日期的时间来定义下限
                Date dt = new Date();
                String beginDate = editText.getText().toString();
                if (!(beginDate == null || beginDate.isEmpty())) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        dt = dateFormat.parse(beginDate);

                    } catch (Exception ex) {
                    }
                }

                onCreateDialog(editEndText, dt).show();
            }
        });
        //endregion

        //region 保存数据
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = txtTitle.getText().toString();
                String remark = txtContent.getText().toString();
                String beginDate = editText.getText().toString();
                String endDate = editEndText.getText().toString();

                ProjectPlanEntity item = new ProjectPlanEntity();
                item.setPlanTitle(title);
                item.setPlanContent(remark);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dtBegin = new Date();
                Date dtEnd = new Date();

                try {
                    dtBegin = dateFormat.parse(beginDate);
                    dtEnd = dateFormat.parse(endDate);
                } catch (Exception ex) {
                }
                item.setBeginDate(dtBegin);
                item.setEndDate(dtEnd);

                //region 将数据写入webservice
                ServiceHelper serviceHelper = new ServiceHelper();
                serviceHelper.AddPlanEntity(item, getMyUser().getUserCode());
                //endregion

                //跳转进入列表页
                Intent intent = new Intent(Appplan.this, MainActivity.class);
                startActivity(intent);
                Appplan.this.finish();

            }
        });
        //endregion
    }

    //region 日期控件事件
    protected Dialog onCreateDialog(EditText editText, Date minDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = null;
        DatePickerDialog.OnDateSetListener dateListener =
                new MyOnDateSetListener(editText);

        String txt = editText.getText().toString();

        if (!(txt == null || txt.isEmpty())) {
            //设置日期控件当前定位的时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = new Date();
            try {
                dt = dateFormat.parse(txt);

            } catch (Exception ex) {
            }
            calendar.setTime(dt);
        }

        dialog = new DatePickerDialog(this,
                dateListener,
                calendar.get(calendar.YEAR),
                calendar.get(calendar.MONTH),
                calendar.get(calendar.DATE));

        DatePicker dp = dialog.getDatePicker();
        dp.setMinDate(minDate.getTime());

        return dialog;
    }

    private static class MyOnDateSetListener implements DatePickerDialog.OnDateSetListener {
        private final EditText editText;

        public MyOnDateSetListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onDateSet(DatePicker datePicker,
                              int year, int month, int dayOfMonth) {
            //Calendar月份是从0开始,所以month要加1
            editText.setText(year + "-" +
                    (month + 1) + "-" + dayOfMonth);
        }
    }
    //endregion

    //region 菜单控件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //打开列表页面
            Intent intent = new Intent(Appplan.this, PlanListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_login) {
            //打开登陆页面
            Intent intent = new Intent(Appplan.this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            //打开主页
            Intent intent = new Intent(Appplan.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion
}
