package android.com.aiface.database.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AttendanceFace extends FaceBean{
    @Id
    private Long id;
    @NotNull
    private String userId;
    @NotNull
    private String attendancePart;
    @NotNull
    private String attendanceName;
    private String onworktime;
    private String offworktime;


    @Generated(hash = 1298912290)
    public AttendanceFace(Long id, @NotNull String userId,
            @NotNull String attendancePart, @NotNull String attendanceName,
            String onworktime, String offworktime) {
        this.id = id;
        this.userId = userId;
        this.attendancePart = attendancePart;
        this.attendanceName = attendanceName;
        this.onworktime = onworktime;
        this.offworktime = offworktime;
    }
    @Generated(hash = 1549355235)
    public AttendanceFace() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAttendancePart() {
        return this.attendancePart;
    }
    public void setAttendancePart(String attendancePart) {
        this.attendancePart = attendancePart;
    }
    public String getAttendanceName() {
        return this.attendanceName;
    }
    public void setAttendanceName(String attendanceName) {
        this.attendanceName = attendanceName;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getOnworktime() {
        return this.onworktime;
    }
    public void setOnworktime(String onworktime) {
        this.onworktime = onworktime;
    }
    public String getOffworktime() {
        return this.offworktime;
    }
    public void setOffworktime(String offworktime) {
        this.offworktime = offworktime;
    }
}
