package android.com.aiface.utils;

import android.com.aiface.AiFaceApplication;
import android.content.Context;
import android.widget.Toast;

public class ToastMsg {
    private static ToastMsg toastMsg;
    private Context mContext;

    private ToastMsg() {
        mContext = AiFaceApplication.getGlobalContext();
    }

    public static ToastMsg getToastInstance() {
        if(toastMsg == null) {
            toastMsg = new ToastMsg();
        }
        return toastMsg;
    }

    public void showShortToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

}
