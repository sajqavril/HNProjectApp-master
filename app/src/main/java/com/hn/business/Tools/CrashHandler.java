package com.hn.business.Tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.hn.business.Data.ServiceHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrashHandler implements Thread.UncaughtExceptionHandler {


    public static final String TAG = "CrashHandler";


    //系统默认的UncaughtException处理类
    private static CrashHandler INSTANCE = new CrashHandler();

    //CrashHandler实例
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    //程序的Context对象
    private Context mContext;

    //用来存储设备信息和异常信息

    private Map<String, String> infos = new HashMap<String, String>();


    //用于格式化日期,作为日志文件名的一部分

    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");


    /**
     * 保证只有一个CrashHandler实例
     */

    private CrashHandler() {

    }


    /**
     * 获取CrashHandler实例 ,单例模式
     */

    public static CrashHandler getInstance() {

        return INSTANCE;

    }


    /**
     * 初始化
     *
     * @param context
     */

    public void init(Context context) {

        mContext = context;

        //获取系统默认的UncaughtException处理器

        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        //设置该CrashHandler为程序的默认处理器

        Thread.setDefaultUncaughtExceptionHandler(this);

    }


    /**
     * 当UncaughtException发生时会转入该函数来处理
     */

    @Override

    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {

            //如果用户没有处理则让系统默认的异常处理器来处理

            mDefaultHandler.uncaughtException(thread, ex);

        } else {

            try {

                Thread.sleep(3000);

            } catch (InterruptedException e) {

                Log.e(TAG, "error : ", e);

            }

            //退出程序

            android.os.Process.killProcess(android.os.Process.myPid());

            System.exit(1);

        }

    }


    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */

    private boolean handleException(Throwable ex) {

        if (ex == null) {

            return false;

        }

        //使用Toast来显示异常信息

        new Thread() {

            @Override

            public void run() {

                Looper.prepare();

                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();

                Looper.loop();

            }

        }.start();

        //收集设备参数信息

        collectDeviceInfo(mContext);

        //保存日志文件
        saveCrashInfo2Service(ex);
//        saveCrashInfo2File(ex);

        return true;

    }


    /**
     * 收集设备参数信息
     *
     * @param ctx
     */

    public void collectDeviceInfo(Context ctx) {

        try {

            PackageManager pm = ctx.getPackageManager();

            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);

            if (pi != null) {

                String versionName = pi.versionName == null ? "null" : pi.versionName;

                String versionCode = pi.versionCode + "";

                infos.put("versionName", versionName);

                infos.put("versionCode", versionCode);

            }

        } catch (PackageManager.NameNotFoundException e) {

            Log.e(TAG, "an error occured when collect package info", e);

        }

        Field[] fields = Build.class.getDeclaredFields();

        for (Field field : fields) {

            try {

                field.setAccessible(true);

                infos.put(field.getName(), field.get(null).toString());

                Log.d(TAG, field.getName() + " : " + field.get(null));

            } catch (Exception e) {

                Log.e(TAG, "an error occured when collect crash info", e);

            }

        }

    }

    //region 内部获取参数数据

    /**
     * 获取错误的信息
     *
     * @param arg1
     * @return
     */
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }

    /**
     * 获取手机的硬件信息
     *
     * @return
     */
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        //通过反射获取系统的硬件信息
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                //暴力反射 ,获取私有的信息
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取手机的版本信息
     *
     * @return
     */
    private String getVersionInfo() {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }
    //endregion

    /**
     * 保存错误信息到服务器上去
     *
     * @param ex
     */
    private void saveCrashInfo2Service(Throwable ex) {

        // 1.获取当前程序的版本号. 版本的id
        String versioninfo = getVersionInfo();

        // 2.获取手机的硬件信息.
        String mobileInfo = getMobileInfo();

        // 3.把错误的堆栈信息 获取出来
        String errorinfo = getErrorInfo(ex);

        //把数据上传到服务进行记录
        try {
            ServiceHelper serviceHelper = new ServiceHelper();
            serviceHelper.SendErrorToService(mContext, versioninfo, mobileInfo, errorinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */

    private String saveCrashInfo2File(Throwable ex) {


        StringBuffer sb = new StringBuffer();

        for (Map.Entry<String, String> entry : infos.entrySet()) {

            String key = entry.getKey();

            String value = entry.getValue();

            sb.append(key + "=" + value + "\n");

        }


        Writer writer = new StringWriter();

        PrintWriter printWriter = new PrintWriter(writer);

        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();

        while (cause != null) {

            cause.printStackTrace(printWriter);

            cause = cause.getCause();

        }

        printWriter.close();

        String result = writer.toString();

        sb.append(result);

        try {

            long timestamp = System.currentTimeMillis();

            String time = formatter.format(new Date());

            String fileName = "crash-" + time + "-" + timestamp + ".log";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                String path = "/sdcard/crash/";

                File dir = new File(path);

                if (!dir.exists()) {

                    dir.mkdirs();

                }

                FileOutputStream fos = new FileOutputStream(path + fileName);

                fos.write(sb.toString().getBytes());

                fos.close();

            }

            return fileName;

        } catch (Exception e) {

            Log.e(TAG, "an error occured while writing file...", e);

        }

        return null;

    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

}
