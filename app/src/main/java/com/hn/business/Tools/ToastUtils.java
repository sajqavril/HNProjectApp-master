package com.hn.business.Tools;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import as.hn.com.hnprojectapp.R;

public class ToastUtils {
    private static Toast mTextToast;
    private static Toast mViewToast;

    public static void showToast(Context mContext, String msg) {
        if (null != mTextToast) {
            mTextToast.cancel();
        }
        mTextToast = new Toast(mContext);
        View toastView = View.inflate(mContext, R.layout.layout_auth_edit_text, null);
        mTextToast.setGravity(Gravity.CENTER, 0, 0);
        mTextToast.setDuration(Toast.LENGTH_SHORT);
        mTextToast.setView(toastView);

//        TextView tv_toast = (TextView) mTextToast.getView().findViewById(R.id.toastText);
//        tv_toast.setText(msg);

        mTextToast.show();
    }

    public static void showToast(Context context, View view) {
        if (null == mViewToast) {
            mViewToast = new Toast(context);
        }
        mViewToast.setDuration(Toast.LENGTH_SHORT);
        mViewToast.setView(view);
        mViewToast.show();
    }

    public static  void  ShowDialog(Context mContext, String msg){


    }
}
