package android.com.aiface;

import android.app.Application;
import android.com.aiface.greendao.DaoMaster;
import android.com.aiface.greendao.DaoSession;
import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

public class AiFaceApplication extends Application{
    private final static String TAG = AiFaceApplication.class.getSimpleName();

    private static Context mContext;
    private final static String DB_NAME = "aiface.db";
    private volatile static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        initDaoManager();
    }

    public static Context getGlobalContext() {
        return mContext;
    }

    private void initDaoManager() {
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME);
        Database db = mHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        Log.d(TAG, "GreenDao init completed");
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }
}
