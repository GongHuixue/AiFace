package android.com.aiface.settings;

import android.com.aiface.settings.AiFaceEnum.*;
public class SettingManager {

    private static SettingManager smInstance;
    private CollectMode mCollectMode = CollectMode.COLLECT_NONE;
    private DetectMode mDetectMode = DetectMode.DETECT_NONE;


    public synchronized static SettingManager getSmInstance() {
        if(smInstance == null) {
            smInstance = new SettingManager();
        }
        return smInstance;
    }

    public void setCollectMode(CollectMode mode) {
        mCollectMode = mode;
    }

    public void setDetectMode(DetectMode mode) {
        mDetectMode = mode;
    }

    public CollectMode getCollectMode() {
        return mCollectMode;
    }

    public DetectMode getDetectMode() {
        return mDetectMode;
    }
}
