package android.com.aiface.ui.fragment;

public class FragmentFactory {

    static FragmentFactory mInstance;

    private FragmentFactory() {
    }

    public static FragmentFactory getInstance() {
        if (mInstance == null) {
            synchronized (FragmentFactory.class) {
                if (mInstance == null) {
                    mInstance = new FragmentFactory();
                }
            }
        }
        return mInstance;
    }

    private CollectFragment mCollectFragment;
    private DetectFragment mDetectFragment;
    private MeFragment mMeFragment;

    public CollectFragment getCollectFragment() {
        if (mCollectFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mCollectFragment == null) {
                    mCollectFragment = new CollectFragment();
                }
            }
        }
        return mCollectFragment;
    }

    public DetectFragment getDetectFragment() {
        if (mDetectFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mDetectFragment == null) {
                    mDetectFragment = new DetectFragment();
                }
            }
        }
        return mDetectFragment;
    }

    public MeFragment getMeFragment() {
        if (mMeFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mMeFragment == null) {
                    mMeFragment = new MeFragment();
                }
            }
        }
        return mMeFragment;
    }
}
