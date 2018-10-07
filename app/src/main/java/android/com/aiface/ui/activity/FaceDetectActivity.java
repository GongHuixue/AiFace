package android.com.aiface.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.com.aiface.R;
import android.com.aiface.baidu.APIService;
import android.com.aiface.baidu.exception.FaceError;
import android.com.aiface.baidu.model.RegResult;
import android.com.aiface.baidu.utils.ImageSaveUtil;
import android.com.aiface.baidu.utils.ImageUtil;
import android.com.aiface.baidu.utils.OnResultListener;
import android.com.aiface.baidu.widget.BrightnessTools;
import android.com.aiface.baidu.widget.FaceRoundView;
import android.com.aiface.baidu.widget.WaveHelper;
import android.com.aiface.baidu.widget.WaveView;
import android.com.aiface.settings.AiFaceEnum;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.DetectPresenter;
import android.com.aiface.ui.view.IDetectView;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.aip.FaceSDKManager;
import com.baidu.aip.ImageFrame;
import com.baidu.aip.face.CameraImageSource;
import com.baidu.aip.face.DetectRegionProcessor;
import com.baidu.aip.face.FaceDetectManager;
import com.baidu.aip.face.FaceFilter;
import com.baidu.aip.face.PreviewView;
import com.baidu.aip.face.camera.PermissionCallback;
import com.baidu.idl.facesdk.FaceInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.UUID;

import static android.com.aiface.baidu.utils.Base64RequestBody.readFile;

public class FaceDetectActivity extends BaseActivity<IDetectView, DetectPresenter> implements IDetectView {
    private final static String TAG = FaceDetectActivity.class.getSimpleName();
    private static final int MSG_INITVIEW = 1001;
    private static final int MSG_BEGIN_DETECT = 1002;
    private TextView nameTextView;
    private PreviewView previewView;
    private View mMainView;
    private FaceRoundView rectView;
    private ImageView closeIv;
    private boolean mGoodDetect = false;
    private static final double ANGLE = 15;
    private boolean mDetectStoped = false;
    private Handler mHandler;
    private String mCurTips;
    private boolean mDetectTime = true;
    private boolean mUploading = false;
    private long mLastTipsTime = 0;
    private int mDetectCount = 0;
    private int mCurFaceId = -1;

    private FaceDetectManager faceDetectManager;
    private DetectRegionProcessor cropProcessor = new DetectRegionProcessor();
    private WaveHelper mWaveHelper;
    private WaveView mWaveview;
    private int mBorderColor = Color.parseColor("#28FFFFFF");
    private int mBorderWidth = 0;
    private int mScreenW;
    private int mScreenH;
    private boolean mSavedBmp = false;
    // 开始人脸检测
    private boolean mBeginDetect = false;
    private static CameraImageSource cameraImageSource;
    private boolean mUploadFromCamera = false;

    private String returnUserId;
    private String returnUserName;
    private AiFaceEnum.DetectMode detectMode = AiFaceEnum.DetectMode.DETECT_NONE;

    @Override
    public int getLayoutId() {
        return R.layout.activity_face_detect;
    }

    @Override
    protected DetectPresenter createPresenter() {
        return new DetectPresenter(this);
    }

    @Override
    public void initData() {
        mUploadFromCamera = getIntent().getBooleanExtra(UPLOAD_FROM_CAMERA, false);
        //init baidu api facetracker;
        initFaceTracker();

        //get screen width/height
        WindowManager manager = getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        mScreenW = outMetrics.widthPixels;
        mScreenH = outMetrics.heightPixels;

        mHandler = new InnerHandler(this);
        mHandler.sendEmptyMessageDelayed(MSG_INITVIEW, 500);
        mHandler.sendEmptyMessageDelayed(MSG_BEGIN_DETECT, 500);
    }

    @Override
    public void initView() {
        faceDetectManager = new FaceDetectManager(this);

        mMainView = (FrameLayout) findViewById(R.id.camera_layout);
        previewView = (PreviewView) findViewById(R.id.preview_view);
        rectView = (FaceRoundView) findViewById(R.id.rect_view);
        nameTextView = (TextView) findViewById(R.id.name_text_view);
        closeIv = (ImageView) findViewById(R.id.closeIv);
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cameraImageSource = new CameraImageSource(this);
        cameraImageSource.setPreviewView(previewView);

        faceDetectManager.setImageSource(cameraImageSource);
        faceDetectManager.setOnFaceDetectListener(new FaceDetectManager.OnFaceDetectListener() {
            @Override
            public void onDetectFace(final int retCode, FaceInfo[] infos, ImageFrame frame) {
                handleFaceDetectResult(retCode, infos, frame);
            }
        });
        faceDetectManager.setOnTrackListener(new FaceFilter.OnTrackListener() {
            @Override
            public void onTrack(FaceFilter.TrackedModel trackedModel) {
                Log.d(TAG, "upload from camera = " + mUploadFromCamera);
                if (mUploadFromCamera) {
                    //return the detect face to register activity;
                    if (trackedModel.meetCriteria() && mGoodDetect) {
                        // upload(trackedModel);
                        mGoodDetect = false;
                        if (!mSavedBmp && mBeginDetect) {
                            if (saveFaceBmp(trackedModel)) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    }
                } else {
                    //return the identify face to detect result activity;
                    upload(trackedModel);
                }
            }
        });
        // 设置检测裁剪处理器
        faceDetectManager.addPreProcessor(cropProcessor);

        //check camera permission
        cameraImageSource.getCameraControl().setPermissionCallback(new PermissionCallback() {
            @Override
            public boolean onRequestPermission() {
                ActivityCompat.requestPermissions(FaceDetectActivity.this,
                        new String[]{Manifest.permission.CAMERA}, 100);
                return true;
            }
        });
        cameraImageSource.getCameraControl().setPreviewView(previewView);

        rectView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                start();
                rectView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        //set screen orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            previewView.setScaleType(PreviewView.ScaleType.FIT_WIDTH);
        } else {
            previewView.setScaleType(PreviewView.ScaleType.FIT_HEIGHT);
        }

        cameraImageSource.getCameraControl().setDisplayOrientation(getWindowManager().getDefaultDisplay().getRotation());
    }


    @Override
    public void initDetectView() {

    }


    private void initWaveview(Rect rect) {
        RelativeLayout rootView = (RelativeLayout) findViewById(R.id.root_view);

        RelativeLayout.LayoutParams waveParams = new RelativeLayout.LayoutParams(
                rect.width(), rect.height());

        waveParams.setMargins(rect.left, rect.top, rect.left, rect.top);
        waveParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        waveParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

        mWaveview = new WaveView(this);
        rootView.addView(mWaveview, waveParams);

        // mWaveview = (WaveView) findViewById(R.id.wave);
        mWaveHelper = new WaveHelper(mWaveview);

        mWaveview.setShapeType(WaveView.ShapeType.CIRCLE);
        mWaveview.setWaveColor(
                Color.parseColor("#28FFFFFF"),
                Color.parseColor("#3cFFFFFF"));

//        mWaveview.setWaveColor(
//                Color.parseColor("#28f16d7a"),
//                Color.parseColor("#3cf16d7a"));

        mBorderColor = Color.parseColor("#28f16d7a");
        mWaveview.setBorder(mBorderWidth, mBorderColor);
    }

    private void visibleView() {
        mMainView.setVisibility(View.INVISIBLE);
    }

    private boolean saveFaceBmp(FaceFilter.TrackedModel model) {
        boolean saved = false;
        final Bitmap face = model.cropFace();
        if (face != null) {
            Log.d("save", "save bmp");
            ImageSaveUtil.saveCameraBitmap(FaceDetectActivity.this, face, "head_tmp.jpg");
        }
        String filePath = ImageSaveUtil.loadCameraBitmapPath(this, "head_tmp.jpg");
        final File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }

        try {
            byte[] buf = readFile(file);
            if (buf.length > 0) {
                saved = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!saved) {
            Log.d("fileSize", "file size >=-99");
        } else {
            mSavedBmp = true;
        }
        return saved;
    }

    private void initBrightness() {
        int brightness = BrightnessTools.getScreenBrightness(FaceDetectActivity.this);
        if (brightness < 200) {
            BrightnessTools.setBrightness(this, 200);
        }
    }


    private void initFaceTracker() {

        FaceSDKManager.getInstance().getFaceTracker(this).set_min_face_size(200);
        FaceSDKManager.getInstance().getFaceTracker(this).set_isCheckQuality(true);
        // 该角度为商学，左右，偏头的角度的阀值，大于将无法检测出人脸，为了在1：n的时候分数高，注册尽量使用比较正的人脸，可自行条件角度
        FaceSDKManager.getInstance().getFaceTracker(this).set_eulur_angle_thr(15, 15, 15);
        FaceSDKManager.getInstance().getFaceTracker(this).set_isVerifyLive(true);
        FaceSDKManager.getInstance().getFaceTracker(this).set_notFace_thr(0.2f);
        FaceSDKManager.getInstance().getFaceTracker(this).set_occlu_thr(0.1f);

        initBrightness();
    }

    private void handleFaceDetectResult(final int retCode, FaceInfo[] infos, ImageFrame frame) {
        if (mUploading) {
            Log.d(TAG, "is uploading ,not detect time");
            return;
        }
        Log.d(TAG, "retCode is:" + retCode);
        String str = "";
        if (retCode == 0) {
            if (infos != null && infos[0] != null) {
                FaceInfo info = infos[0];
                boolean distance = false;
                if (info != null && frame != null) {
                    if (info.mWidth >= (0.9 * frame.getWidth())) {
                        distance = false;
                        str = getResources().getString(R.string.detect_zoom_out);
                    } else if (info.mWidth <= 0.4 * frame.getWidth()) {
                        distance = false;
                        str = getResources().getString(R.string.detect_zoom_in);
                    } else {
                        distance = true;
                    }
                }
                boolean headUpDown;
                if (info != null) {
                    if (info.headPose[0] >= ANGLE) {
                        headUpDown = false;
                        str = getResources().getString(R.string.detect_head_up);
                    } else if (info.headPose[0] <= -ANGLE) {
                        headUpDown = false;
                        str = getResources().getString(R.string.detect_head_down);
                    } else {
                        headUpDown = true;
                    }

                    boolean headLeftRight;
                    if (info.headPose[1] >= ANGLE) {
                        headLeftRight = false;
                        str = getResources().getString(R.string.detect_head_left);
                    } else if (info.headPose[1] <= -ANGLE) {
                        headLeftRight = false;
                        str = getResources().getString(R.string.detect_head_right);
                    } else {
                        headLeftRight = true;
                    }

                    if (distance && headUpDown && headLeftRight) {
                        mGoodDetect = true;
                    } else {
                        mGoodDetect = false;
                    }

                }
            }
        } else if (retCode == 1) {
            str = getResources().getString(R.string.detect_head_up);
        } else if (retCode == 2) {
            str = getResources().getString(R.string.detect_head_down);
        } else if (retCode == 3) {
            str = getResources().getString(R.string.detect_head_left);
        } else if (retCode == 4) {
            str = getResources().getString(R.string.detect_head_right);
        } else if (retCode == 5) {
            str = getResources().getString(R.string.detect_low_light);
        } else if (retCode == 6) {
            str = getResources().getString(R.string.detect_face_in);
        } else if (retCode == 7) {
            str = getResources().getString(R.string.detect_face_in);
        } else if (retCode == 10) {
            str = getResources().getString(R.string.detect_keep);
        } else if (retCode == 11) {
            str = getResources().getString(R.string.detect_occ_right_eye);
        } else if (retCode == 12) {
            str = getResources().getString(R.string.detect_occ_left_eye);
        } else if (retCode == 13) {
            str = getResources().getString(R.string.detect_occ_nose);
        } else if (retCode == 14) {
            str = getResources().getString(R.string.detect_occ_mouth);
        } else if (retCode == 15) {
            str = getResources().getString(R.string.detect_right_contour);
        } else if (retCode == 16) {
            str = getResources().getString(R.string.detect_left_contour);
        } else if (retCode == 17) {
            str = getResources().getString(R.string.detect_chin_contour);
        }

        boolean faceChanged = true;
        if (infos != null && infos[0] != null) {
            Log.d("DetectLogin", "face id is:" + infos[0].face_id);
            if (infos[0].face_id == mCurFaceId) {
                faceChanged = false;
            } else {
                faceChanged = true;
            }
            mCurFaceId = infos[0].face_id;
        }

        if (faceChanged) {
            showProgressBar(false);
        }

        final int resultCode = retCode;
        if (!(mGoodDetect && retCode == 0)) {
            if (faceChanged) {
                showProgressBar(false);
            }
        }

        if (retCode == 6 || retCode == 7 || retCode < 0) {
            rectView.processDrawState(true);
        } else {
            rectView.processDrawState(false);
        }

        mCurTips = str;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ((System.currentTimeMillis() - mLastTipsTime) > 1000) {
                    nameTextView.setText(mCurTips);
                    mLastTipsTime = System.currentTimeMillis();
                }
                if (mGoodDetect && resultCode == 0) {
                    nameTextView.setText("");
                    showProgressBar(true);
                }
            }
        });

        if (infos == null) {
            mGoodDetect = false;
        }

    }

    private void start() {
        Rect dRect = rectView.getFaceRoundRect();

        //   RectF newDetectedRect = new RectF(detectedRect);
        int preGap = getResources().getDimensionPixelOffset(R.dimen.preview_margin);
        int w = getResources().getDimensionPixelOffset(R.dimen.detect_out);

        int orientation = getResources().getConfiguration().orientation;
        boolean isPortrait = (orientation == Configuration.ORIENTATION_PORTRAIT);
        if (isPortrait) {
            // 检测区域矩形宽度
            int rWidth = mScreenW - 2 * preGap;
            // 圆框宽度
            int dRectW = dRect.width();
            // 检测矩形和圆框偏移
            int h = (rWidth - dRectW) / 2;
            //  Log.d("liujinhui hi is:", " h is:" + h + "d is:" + (dRect.left - 150));
            int rLeft = w;
            int rRight = rWidth - w;
            int rTop = dRect.top - h - preGap + w;
            int rBottom = rTop + rWidth - w;

            //  Log.d("liujinhui", " rLeft is:" + rLeft + "rRight is:" + rRight + "rTop is:" + rTop + "rBottom is:" + rBottom);
            RectF newDetectedRect = new RectF(rLeft, rTop, rRight, rBottom);
            cropProcessor.setDetectedRect(newDetectedRect);
        } else {
            int rLeft = mScreenW / 2 - mScreenH / 2 + w;
            int rRight = mScreenW / 2 + mScreenH / 2 + w;
            int rTop = 0;
            int rBottom = mScreenH;

            RectF newDetectedRect = new RectF(rLeft, rTop, rRight, rBottom);
            cropProcessor.setDetectedRect(newDetectedRect);
        }

        faceDetectManager.start();
        initWaveview(dRect);
    }

    @Override
    protected void onStop() {
        super.onStop();
        faceDetectManager.stop();
        mDetectStoped = true;
        if (mWaveview != null) {
            mWaveview.setVisibility(View.GONE);
            mWaveHelper.cancel();
        }
    }

    private void showProgressBar(final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    if (mWaveview != null) {
                        mWaveview.setVisibility(View.VISIBLE);
                        mWaveHelper.start();
                    }
                } else {
                    if (mWaveview != null) {
                        mWaveview.setVisibility(View.GONE);
                        mWaveHelper.cancel();
                    }
                }

            }
        });
    }

    private void dispatchDetectResult() {
        detectMode = mSettingManager.getDetectMode();
        Log.d(TAG, "Current detect mode = " + detectMode);
        Intent intent = null;
        switch (detectMode) {
            case DETECT_MEETING:
                intent = new Intent(FaceDetectActivity.this, MeetingSignedActivity.class);
                break;
            case DETECT_ATTENDANCE:
                intent = new Intent(FaceDetectActivity.this, AttendanceQueryActivity.class);
                break;
            case DETECT_HOME:
                intent = new Intent(FaceDetectActivity.this, HomeVistorActivity.class);
                break;
            case DETECT_GATE:
                intent = new Intent(FaceDetectActivity.this, GateAccessActivity.class);
                break;
                default:
                    break;
        }

        intent.putExtra("login_success", true);
        intent.putExtra("user_info", returnUserName);
        intent.putExtra("uid", returnUserId);
//        intent.putExtra("score", maxScore);
        jumpToActivity(intent);

    }

    /**
     * 参考https://ai.baidu.com/docs#/Face-API/top 人脸识别接口
     * 无需知道uid，如果同一个人多次注册，可能返回任意一个帐号的uid
     * 建议上传人脸到自己的服务器，在服务器端调用https://aip.baidubce.com/rest/2.0/face/v3/search，比对分数阀值（如：80分），
     * 认为登录通过
     * group_id	是	string	用户组id（由数字、字母、下划线组成），长度限制128B，如果需要查询多个用户组id，用逗号分隔
     * image	是	string	图像base64编码，每次仅支持单张图片，图片编码后大小不超过10M
     * <p>
     * 返回登录认证的参数给客户端
     *
     * @param model
     */
    private void upload(FaceFilter.TrackedModel model) {
        if (mUploading) {
            Log.d("liujinhui", "is uploading");
            return;
        }
        mUploading = true;

        if (model.getEvent() != FaceFilter.Event.OnLeave) {
            mDetectCount++;

            try {
                final Bitmap face = model.cropFace();
                final File file = File.createTempFile(UUID.randomUUID().toString() + "", ".jpg");
                ImageUtil.resize(face, file, 200, 200);
                ImageSaveUtil.saveCameraBitmap(FaceDetectActivity.this, face, "head_tmp.jpg");

                APIService.getInstance().identify(new OnResultListener<RegResult>() {
                    @Override
                    public void onResult(RegResult result) {
                        deleteFace(file);
                        if (result == null) {
                            mUploading = false;
                            if (mDetectCount >= 3) {
                                mToastInstance.showShortToast("人脸校验不通过,请确认是否已注册");
                                finish();
                            }
                            return;
                        }

                        String res = result.getJsonRes();
                        Log.d("FaceDetectActivity", "res is:" + res);
                        double maxScore = 0;
                        if (TextUtils.isEmpty(res)) {
                            return;
                        }
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(res);
                            JSONObject resObj = obj.optJSONObject("result");
                            if (resObj != null) {
                                JSONArray resArray = resObj.optJSONArray("user_list");
                                int size = resArray.length();


                                for (int i = 0; i < size; i++) {
                                    JSONObject s = (JSONObject) resArray.get(i);
                                    if (s != null) {
                                        double score = s.getDouble("score");
                                        if (score > maxScore) {
                                            maxScore = score;
                                            returnUserId = s.getString("user_id");
                                            returnUserName = s.getString("user_info");
                                            Log.d(TAG, "Score = " + maxScore + ",id = " + returnUserId + ", name = " + returnUserName);
                                        }

                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (maxScore > 80) {
                            Log.d("FaceDetectActivity", "onResult ok");
                            mDetectTime = false;
                            dispatchDetectResult();
                            finish();
                            return;
                        } else {
                            Log.d("FaceDetectActivity", "onResult fail");
                            if (mDetectCount >= 3) {
                                mDetectTime = false;
                                mToastInstance.showShortToast("人脸校验不通过,请确认是否已注册");
                                finish();
                                return;
                            }

                        }
                        mUploading = false;
                    }

                    @Override
                    public void onError(FaceError error) {
                        error.printStackTrace();
                        deleteFace(file);

                        mUploading = false;
                        if (error.getErrorCode() == 216611) {
                            mDetectTime = false;
                            Intent intent = new Intent();
                            intent.putExtra("login_success", false);
                            intent.putExtra("error_code", error.getErrorCode());
                            intent.putExtra("error_msg", error.getErrorMessage());
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                            return;
                        }

                        if (mDetectCount >= 3) {
                            mDetectTime = false;
                            if (error.getErrorCode() == 10000) {
                                mToastInstance.showShortToast("人脸校验不通过,请检查网络后重试");
                            } else {
                                mToastInstance.showShortToast("人脸校验不通过");
                            }
                            finish();
                            return;
                        }
                    }
                }, file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            showProgressBar(false);
            mUploading = false;
        }
    }

    private void deleteFace(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWaveview != null) {
            mWaveHelper.cancel();
            mWaveview.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDetectStoped) {
            faceDetectManager.start();
            mDetectStoped = false;
            mDetectTime = true;
        }

    }


    private static class InnerHandler extends Handler {
        private WeakReference<FaceDetectActivity> mWeakReference;

        public InnerHandler(FaceDetectActivity activity) {
            super();
            this.mWeakReference = new WeakReference<FaceDetectActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mWeakReference == null || mWeakReference.get() == null) {
                return;
            }
            FaceDetectActivity activity = mWeakReference.get();
            if (activity == null) {
                return;
            }
            if (msg == null) {
                return;

            }
            switch (msg.what) {
                case MSG_INITVIEW:
                    activity.visibleView();
                    break;
                case MSG_BEGIN_DETECT:
                    activity.mBeginDetect = true;
                    break;
                default:
                    break;
            }
        }
    }

}
