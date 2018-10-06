package android.com.aiface.database;

import android.com.aiface.AiFaceApplication;
import android.com.aiface.database.bean.AttendanceFace;
import android.com.aiface.database.bean.FaceBean;
import android.com.aiface.database.bean.GateFace;
import android.com.aiface.database.bean.HomeFace;
import android.com.aiface.database.bean.MeetingFace;
import android.com.aiface.greendao.AttendanceFaceDao;
import android.com.aiface.greendao.GateFaceDao;
import android.com.aiface.greendao.HomeFaceDao;
import android.com.aiface.greendao.MeetingFaceDao;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.List;

public class GreenDaoManager {
    private final static String TAG = GreenDaoManager.class.getSimpleName();
    private static GreenDaoManager singleInstance;

    private Handler mHandler;
    private HandlerThread mHT;

//    private BrowserMediaFile browserMediaFile = new BrowserMediaFile();
//    private NotificationHandler ntf = NotificationHandler.getInstance();

    private GreenDaoManager() {
        createHandlerThread();
    }

    /*The thread was used for operate database*/
    private void createHandlerThread() {
        if(mHT == null) {
            mHT = new HandlerThread("GreenDaoManager_HT");
        }
        mHT.start();

        mHandler = new Handler(mHT.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
    }

    private void destroyHandlerThread() {
        if(mHT != null) {
            mHT.quit();
            mHT.interrupt();
            mHT = null;
            mHandler = null;
        }
    }

    public static synchronized GreenDaoManager getSingleInstance() {
        if(singleInstance == null) {
            singleInstance = new GreenDaoManager();
        }
        return singleInstance;
    }

    public List<MeetingFace> getMeetingInfo() {
        List<MeetingFace> meetingFaceList = AiFaceApplication.getDaoSession()
                .getMeetingFaceDao()
                .queryBuilder()
                .where(MeetingFaceDao.Properties.MeetingName.isNotNull(), MeetingFaceDao.Properties.MeetingTime.isNotNull())
                .orderDesc(MeetingFaceDao.Properties.MeetingTime) //must order by desc;
                .list();
        return meetingFaceList;
    }

    public List<AttendanceFace> getAttendanceInfo() {
        List<AttendanceFace> attendanceFaceList = AiFaceApplication.getDaoSession()
                .getAttendanceFaceDao()
                .queryBuilder()
                .where(AttendanceFaceDao.Properties.AttendanceName.isNotNull(), AttendanceFaceDao.Properties.AttendanceName.isNotNull())
                .list();
        return attendanceFaceList;
    }

    public List<HomeFace> getHomeHostInfo() {
        List<HomeFace> homeFaceList = AiFaceApplication.getDaoSession()
                .getHomeFaceDao()
                .queryBuilder()
                .where(HomeFaceDao.Properties.HostName.isNotNull(), HomeFaceDao.Properties.HomeAddr.isNotNull())
                .orderDesc(HomeFaceDao.Properties.HomeAddr)
                .list();
        return homeFaceList;
    }

    public List<GateFace> getGateInfo() {
        List<GateFace> gateFaceList = AiFaceApplication.getDaoSession()
                .getGateFaceDao()
                .queryBuilder()
                .where(GateFaceDao.Properties.UserName.isNotNull(), GateFaceDao.Properties.UserId.isNotNull())
                .orderDesc(GateFaceDao.Properties.Id)
                .list();
        return gateFaceList;
    }

    public void insertFaceData(FaceBean faceBean) {
        if(faceBean instanceof MeetingFace) {
            Log.d(TAG, "insert meeting data");
            AiFaceApplication.getDaoSession().getMeetingFaceDao().insertOrReplace((MeetingFace)faceBean);
        }else if(faceBean instanceof AttendanceFace) {
            AiFaceApplication.getDaoSession().getAttendanceFaceDao().insertOrReplace((AttendanceFace)faceBean);
        }else if(faceBean instanceof HomeFace) {
            AiFaceApplication.getDaoSession().getHomeFaceDao().insertOrReplace((HomeFace)faceBean);
        }else if(faceBean instanceof GateFace) {
            AiFaceApplication.getDaoSession().getGateFaceDao().insertOrReplace((GateFace)faceBean);
        }
    }

    public void updateWorkTime(String userId, String userName, String onTime, String offTime) {
        /*update onwork time*/
        AttendanceFace faceInfo;
        if(onTime != null) {
            if((userId != null) && (userName != null)) {
                faceInfo = (AttendanceFace) queryFaceByIdName(userId, userName, "Attendance");
                faceInfo.setOnworktime(onTime);
                AiFaceApplication.getDaoSession().getAttendanceFaceDao().update(faceInfo);
            }
        }

        /*update offwork time*/
        if(offTime != null) {
            if((userId != null) && (userName != null)) {
                faceInfo = (AttendanceFace) queryFaceByIdName(userId, userName, "Attendance");
                faceInfo.setOffworktime(offTime);
                AiFaceApplication.getDaoSession().getAttendanceFaceDao().update(faceInfo);
            }
        }
    }

    public FaceBean queryFaceByIdName(String userId, String userName, String table) {
        FaceBean faceInfo = new FaceBean();
        if(table.equals("Meeting")) {
            faceInfo = AiFaceApplication.getDaoSession()
                    .getMeetingFaceDao()
                    .queryBuilder()
                    .where(MeetingFaceDao.Properties.UserId.eq(userId), MeetingFaceDao.Properties.ParticipantName.eq(userName))
                    .unique();
        }else if(table.equals("Attendance")) {
            faceInfo = AiFaceApplication.getDaoSession()
                    .getAttendanceFaceDao()
                    .queryBuilder()
                    .where(AttendanceFaceDao.Properties.UserId.eq(userId), AttendanceFaceDao.Properties.AttendanceName.eq(userName))
                    .orderDesc(AttendanceFaceDao.Properties.Id)
                    .limit(1)
                    .unique();
        }else if(table.equals("Home")) {
            faceInfo = AiFaceApplication.getDaoSession()
                    .getHomeFaceDao()
                    .queryBuilder()
                    .where(HomeFaceDao.Properties.UserId.eq(userId), HomeFaceDao.Properties.GustName.eq(userName))
                    .unique();
        }else if(table.equals("Gate")) {
            faceInfo = AiFaceApplication.getDaoSession()
                    .getGateFaceDao()
                    .queryBuilder()
                    .where(GateFaceDao.Properties.UserId.eq(userId), GateFaceDao.Properties.UserName.eq(userName))
                    .unique();
        }
        return faceInfo;
    }
}
