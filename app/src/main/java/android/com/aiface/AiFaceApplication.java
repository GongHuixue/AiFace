package android.com.aiface;

import android.app.Application;
import android.content.Context;

public class AiFaceApplication extends Application{
    private final static String TAG = AiFaceApplication.class.getSimpleName();

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getGlobalContext() {
        return mContext;
    }
}
