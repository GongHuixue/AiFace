package android.com.aiface.ui.base;

import android.Manifest;
import android.com.aiface.baidu.APIService;
import android.com.aiface.baidu.exception.FaceError;
import android.com.aiface.baidu.model.RegResult;
import android.com.aiface.baidu.utils.Md5;
import android.com.aiface.baidu.utils.OnResultListener;
import android.com.aiface.database.GreenDaoManager;
import android.com.aiface.settings.SettingManager;
import android.com.aiface.ui.activity.FaceDetectActivity;
import android.com.aiface.utils.ToastMsg;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
    private final static String TAG = BaseActivity.class.getSimpleName();
    protected T mPresenter;
    protected SettingManager mSettingManager;
    protected GreenDaoManager greenDaoManager;
    protected ToastMsg mToastInstance;

    protected static final int REQUEST_CODE_DETECT_FACE = 1000;
    protected static final int REQUEST_CODE_PICK_IMAGE = 1001;

    private String userId;
    private IFaceRegCallback faceListener;

    protected final String UPLOAD_FROM_CAMERA = "upload_from_camera";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();
        if(mPresenter != null) {
            mPresenter.attachView((V)this);
        }
        mSettingManager = SettingManager.getSmInstance();

        setContentView(getLayoutId());
        initCommonPart();
        initView();
        initData();
    }

    private void initCommonPart() {
        greenDaoManager = GreenDaoManager.getSingleInstance();
        mToastInstance = ToastMsg.getToastInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mPresenter != null) {
            mPresenter.detachView();
        }
    }

    protected abstract T createPresenter();

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();

    public interface IFaceRegCallback {
        void FaceRegSuccess(String userId);
        void FaceRegFailed();
    }

    public void jumpToActivity(Intent intent) {
        startActivity(intent);
    }

    public void jumpToActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            return;
        }
    }

    public void uploadFromAlbum() {
        checkPermission();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    public void uploadFromCamera() {
        checkPermission();
        Intent intent = new Intent(this, FaceDetectActivity.class);
        intent.putExtra(UPLOAD_FROM_CAMERA, true);
        startActivityForResult(intent, REQUEST_CODE_DETECT_FACE);
    }

    public String getFacePathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void registerFaceImage(String facePath, final String userName) {
        if(TextUtils.isEmpty(userName)) {
            mToastInstance.showShortToast("姓名不能为空");
            return;
        }

        if(TextUtils.isEmpty(facePath)) {
            mToastInstance.showShortToast("图像不能为空");
            return;
        }

        final File file = new File(facePath);
        if(!file.exists()) {
            mToastInstance.showShortToast("文件不存在");
            return;
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                RegisterFaceToBaidu(file, userName);
            }
        });
    }

    private void RegisterFaceToBaidu(File file, String userName) {
        userId = Md5.MD5(userName, "utf-8");
        Log.d(TAG, "userid = " + userId);
        APIService.getInstance().reg(new OnResultListener<RegResult>() {
            @Override
            public void onResult(RegResult result) {
                Log.d(TAG, "register result = " + result.getJsonRes());
                mToastInstance.showShortToast("注册成功");
                if(faceListener != null) {
                    faceListener.FaceRegSuccess(userId);
                }
            }

            @Override
            public void onError(FaceError error) {
                mToastInstance.showShortToast("注册失败");
                if(faceListener != null) {
                    faceListener.FaceRegFailed();
                }
            }
        }, file, userId, userName);
    }

    public void setFaceGroup(String groupId) {
        APIService.getInstance().setGroupId(groupId);
    }

    public void registerFaceListener(IFaceRegCallback listener) {
        faceListener = listener;
    }

    public void unregisterFaceListener() {
        faceListener = null;
    }
}
