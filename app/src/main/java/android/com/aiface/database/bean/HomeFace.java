package android.com.aiface.database.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class HomeFace extends FaceBean{
    @Id
    private Long id;
    @NotNull
    private String homeAddr;
    @NotNull
    private String hostName;
    @NotNull
    private String userId;

    private String gustName;
    private String visitTime;

    @Generated(hash = 303221103)
    public HomeFace(Long id, @NotNull String homeAddr, @NotNull String hostName,
            @NotNull String userId, String gustName, String visitTime) {
        this.id = id;
        this.homeAddr = homeAddr;
        this.hostName = hostName;
        this.userId = userId;
        this.gustName = gustName;
        this.visitTime = visitTime;
    }

    @Generated(hash = 84504636)
    public HomeFace() {
    }

    public HomeFace(String gustName) {
        this.gustName = gustName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHomeAddr() {
        return this.homeAddr;
    }

    public void setHomeAddr(String homeAddr) {
        this.homeAddr = homeAddr;
    }

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getGustName() {
        return this.gustName;
    }

    public void setGustName(String gustName) {
        this.gustName = gustName;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVisitTime() {
        return this.visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }
}
