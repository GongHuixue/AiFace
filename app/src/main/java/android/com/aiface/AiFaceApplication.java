package android.com.aiface;

import android.app.Application;

import com.baidu.aip.FaceSDKManager;
import com.baidu.aip.FaceEnvironment;
import com.baidu.idl.facesdk.FaceTracker;

import android.com.aiface.baidu.APIService;
import android.com.aiface.baidu.Config;
import android.com.aiface.baidu.exception.FaceError;
import android.com.aiface.baidu.model.AccessToken;
import android.com.aiface.baidu.utils.OnResultListener;
import android.com.aiface.greendao.DaoMaster;
import android.com.aiface.greendao.DaoSession;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.greendao.database.Database;

public class AiFaceApplication extends Application {
    private final static String TAG = AiFaceApplication.class.getSimpleName();
    private Handler handler;

    private static Context mContext;
    private final static String DB_NAME = "aiface.db";
    private volatile static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        handler = new Handler(Looper.getMainLooper());

        initDaoManager();
        initBaiduComp();
    }

    public static Context getGlobalContext() {
        return mContext;
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }

    private void initDaoManager() {
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME);
        Database db = mHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        Log.d(TAG, "GreenDao init completed");
    }

    private void initBaiduComp() {
        initLib();

        APIService.getInstance().init(this);
        APIService.getInstance().setGroupId(Config.groupID);
        // 用ak，sk获取token, 调用在线api，如：注册、识别等。为了ak、sk安全，建议放您的服务器，
        APIService.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                Log.i("wtf", "AccessToken->" + result.getAccessToken());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AiFaceApplication.this, "启动成功", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(FaceError error) {
                Log.e("xx", "AccessTokenError:" + error);
                error.printStackTrace();

            }
        }, this, Config.apiKey, Config.secretKey);

    }

    /**
     * 初始化SDK
     */
    private void initLib() {
        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().init(this, Config.licenseID, Config.licenseFileName);
        setFaceConfig();
    }

    private void setFaceConfig() {
        FaceTracker tracker = FaceSDKManager.getInstance().getFaceTracker(this);  //.getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整

        // 模糊度范围 (0-1) 推荐小于0.7
        tracker.set_blur_thr(FaceEnvironment.VALUE_BLURNESS);
        // 光照范围 (0-1) 推荐大于40
        tracker.set_illum_thr(FaceEnvironment.VALUE_BRIGHTNESS);
        // 裁剪人脸大小
        tracker.set_cropFaceSize(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        // 人脸yaw,pitch,row 角度，范围（-45，45），推荐-15-15
        tracker.set_eulur_angle_thr(FaceEnvironment.VALUE_HEAD_PITCH, FaceEnvironment.VALUE_HEAD_ROLL,
                FaceEnvironment.VALUE_HEAD_YAW);

        // 最小检测人脸（在图片人脸能够被检测到最小值）80-200， 越小越耗性能，推荐120-200
        tracker.set_min_face_size(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        //
        tracker.set_notFace_thr(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        // 人脸遮挡范围 （0-1） 推荐小于0.5
        tracker.set_occlu_thr(FaceEnvironment.VALUE_OCCLUSION);
        // 是否进行质量检测
        tracker.set_isCheckQuality(true);
        // 是否进行活体校验
        tracker.set_isVerifyLive(false);
    }
}
