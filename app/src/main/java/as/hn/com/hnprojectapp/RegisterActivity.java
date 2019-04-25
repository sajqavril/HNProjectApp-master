package as.hn.com.hnprojectapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.widgets.Dialog;
import com.hn.business.Data.ServiceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.nickname)
    EditText nickname;
    @BindView(R.id.newpassword)
    EditText newpassword;
    @BindView(R.id.verpassword)
    EditText verpassword;
    @BindView(R.id.email_edit_pwd_button)
    Button email_edit_pwd_button;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        email_edit_pwd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterLogin();
            }
        });
    }

    private void RegisterLogin() {
        String usercode = email.getText().toString();
        String strUserName = username.getText().toString();
        String strNickName = nickname.getText().toString();
        String newpwd = newpassword.getText().toString();
        String verpwd = verpassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!newpwd.equals(verpwd)) {
            verpassword.setError(getString(R.string.error_invalid_verpassword));
            focusView = verpassword;
            cancel = true;
        } else if (!TextUtils.isEmpty(newpwd) && !isPasswordValid(newpwd)) {
            newpassword.setError(getString(R.string.error_invalid_password));
            focusView = newpassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            //region 去服务器注册账户
            ServiceHelper serviceHelper = new ServiceHelper();
            if (serviceHelper.UserRegiter(usercode, strUserName, strNickName, newpwd)) {
                ShowMessage("注册成功，请用新的账号密码登录系统！", true);
            } else {
                ShowMessage("注册失败，该用户名已经存在！", false);
            }
            //endregion
        }

    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    private void ShowMessage(String meg, final boolean isOK) {
        Dialog dialog = new Dialog(RegisterActivity.this, "操作提示", meg);
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOK) {
                    ToLogin();
                } else {

                }
            }
        });
        dialog.show();
    }

    private void ToLogin() {
        //回到登录页
//        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//        startActivity(intent);
        RegisterActivity.this.finish();
    }

    //region 菜单控件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            //返回上级登录页面
            ToLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

}
