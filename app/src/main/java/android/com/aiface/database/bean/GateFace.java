package android.com.aiface.database.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class GateFace extends FaceBean{
    @Id
    private Long id;
    @NotNull
    private String userName;
    @NotNull
    private String userId;

    private String checkTime;
    @Generated(hash = 2015871224)
    public GateFace(Long id, @NotNull String userName, @NotNull String userId,
            String checkTime) {
        this.id = id;
        this.userName = userName;
        this.userId = userId;
        this.checkTime = checkTime;
    }
    @Generated(hash = 1704070578)
    public GateFace() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getCheckTime() {
        return this.checkTime;
    }
    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }
}
