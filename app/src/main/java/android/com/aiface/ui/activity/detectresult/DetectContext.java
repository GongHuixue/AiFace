package android.com.aiface.ui.activity.detectresult;

public class DetectContext {
    DetectStrategy detectStrategy;

    public DetectContext(DetectStrategy faceStrategy) {
        this.detectStrategy = faceStrategy;
    }

    public int faceDetectContext() {
        return detectStrategy.faceDetectResult();
    }
}
