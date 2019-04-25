package as.hn.com.hnprojectapp;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.hn.business.Data.ChatEntity;
import com.hn.business.Data.ChatMessage;
import com.hn.business.Data.ServiceHelper;
import com.hn.business.Data.UserInfoEntity;

import java.util.ArrayList;
import java.util.List;

import database.AppDBSchema.AppDBSchema;

import static database.AppDBSchema.AppDBSchema.*;

public class MyActivityBase extends AppCompatActivity {
    /**聊天对话信息初始化**/
    protected ChatEntity mChats;

    /**数据库对象**/
    protected SQLiteDatabase mDatabase;
    protected Context mContext;
    /***封装toast对象**/

    private static Toast toast;
    /***获取TAG的activity名称**/

    protected final String TAG = this.getClass().getSimpleName();
    /***是否显示标题栏*/

    private boolean isshowtitle = true;
    /***是否显示标题栏*/

    private boolean isshowstate = true;

    //登录账户的信息
    //每一个activity都会传入登录账户的信息
    private UserInfoEntity MyUser;

    /**
     * 蒲公英的APPID
     */
    protected final String PgyAppId = "3462c64c8839f1f4f2120710b910f679";

    public static ContentValues getContentValues(ChatEntity chat){
        ContentValues values = new ContentValues();
        values.put(ChatTable.Cols.UUID,chat.getChatId().toString());
        values.put(ChatTable.Cols.CHATNAME,chat.getChatName());
        values.put(ChatTable.Cols.CHATFACE,chat.getChatFace().toString()); //drawable对象
        values.put(ChatTable.Cols.CHATMEMBERS,chat.getChatMembers().toString()); //list chat entity对象
        values.put(ChatTable.Cols.CHATMESSAGES,chat.getChatMessages().toString()); //list chat message对象
        values.put(ChatTable.Cols.NOTIFY,chat.isNotification()); //boolean对象
        return values;
    }

    //向数据库对象中添加chat entity
    public void addChatEntity(ChatEntity chat){
        ContentValues values = getContentValues(chat);
        mDatabase.insert(ChatTable.NAME,null,values);
    }

    //用于更新记录
    public void updateChatEntity(ChatEntity chat){
        String uuidString = chat.getChatId().toString();
        ContentValues values = getContentValues(chat);
        /**用该方法来插入String数据**/
        mDatabase.update(ChatTable.NAME,values,ChatTable.Cols.UUID + "= ?" ,new String[]{uuidString});
    }

    //之后不同的页面重写该方法
    public void initData(){
        UserInfoEntity user = new UserInfoEntity(123,"123456","SunJiaQi","sun","123456");
        List<UserInfoEntity> users = new ArrayList<>();
        users.add(user);
        mChats = new ChatEntity(users);

    }

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        //初始化数据库
        mDatabase = new DbBaseHelper(mContext).getWritableDatabase();

        //向数据库中添加数据
        initData();
        addChatEntity(mChats);



        //设置数据库对象


        //设置标题栏

        if (!isshowtitle) {

            requestWindowFeature(Window.FEATURE_NO_TITLE);

        }

        //设置状态栏
        if (isshowstate) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,

                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }

        //设置布局

//        setContentView(intiLayout());

        //初始化控件

//        initView();

        //设置数据

//        initData();

        //region 初始化登陆
        ServiceHelper serviceHelper = new ServiceHelper();

        //把本地保存的用户信息临时文件传入生成MyUser

        /**暴力添加用户登录信息*/
        if(MyUser == null)
            this.MyUser = new UserInfoEntity(007,"sunjq8","sunjq8","sunjq8","Aptx4869");
        //serviceHelper.UserLoginApp("sunjq8","Aptx4869",getApplicationContext());
        //this.MyUser = serviceHelper.LocalToUser(MyActivityBase.this);
        if (!this.MyUser.UserIsLogin()) {
            //未成功登陆需要进入注册界面
            Intent intent = new Intent(MyActivityBase.this, LoginActivity.class);
            startActivity(intent);
        }
        //endregion
    }


    /**
     * 设置布局
     *
     * @return
     */

//    public abstract int intiLayout();


    /**
     * 初始化布局
     */

//    public abstract void initView();


    /**
     * 设置数据
     */

//    public abstract void initData();


    /**
     * 是否设置标题栏
     *
     * @return
     */

    public void setTitle(boolean ishow) {

        isshowtitle = ishow;

    }


    /**
     * 设置是否显示状态栏
     *
     * @param ishow
     */

    public void setState(boolean ishow) {

        isshowstate = ishow;

    }


    /**
     * 显示长toast
     *
     * @param msg
     */

    public void toastLong(String msg) {

        if (null == toast) {

            toast = new Toast(this);

            toast.setDuration(Toast.LENGTH_LONG);

            toast.setText(msg);

            toast.show();

        } else {

            toast.setText(msg);

            toast.show();

        }

    }


    /**
     * 显示短toast
     *
     * @param msg
     */

    public void toastShort(String msg) {

        if (null == toast) {

            toast = new Toast(this);

            toast.setDuration(Toast.LENGTH_SHORT);

            toast.setText(msg);

            toast.show();

        } else {

            toast.setText(msg);

            toast.show();

        }

    }


    public UserInfoEntity getMyUser() {
        return MyUser;
    }

    public void setMyUser(UserInfoEntity myUser) {
        MyUser = myUser;
    }

    //用于隐藏软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //如果触摸
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            //获得当前的触摸区域
            View v = getCurrentFocus();
            if(MyActivityBase.ifHideSoftInput(v, ev)){
                MyActivityBase.hideSoftInput(MyActivityBase.this, v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public static boolean ifHideSoftInput(View v, MotionEvent mv){
        if(v!=null && (v instanceof EditText)){
            int[] i = {0,0};
            v.getLocationInWindow(i);
            int left = i[0], top = i[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if(mv.getRawY() > top && mv.getRawY() < bottom){
                return false;
            }else {
                return true;
            }
        }

        return false;
    }

    public static void hideSoftInput(Activity activity, IBinder token){
        if(token!=null){
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
