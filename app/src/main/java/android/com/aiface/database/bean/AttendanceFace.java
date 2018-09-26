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
    private String attendancePart;
    @NotNull
    private String attendanceName;
    @Generated(hash = 1929452080)
    public AttendanceFace(Long id, @NotNull String attendancePart,
            @NotNull String attendanceName) {
        this.id = id;
        this.attendancePart = attendancePart;
        this.attendanceName = attendanceName;
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
}
