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
    private String hostName;

    private String gustName;

    @Generated(hash = 1173753438)
    public HomeFace(Long id, @NotNull String homeAddr, String hostName,
            String gustName) {
        this.id = id;
        this.homeAddr = homeAddr;
        this.hostName = hostName;
        this.gustName = gustName;
    }

    @Generated(hash = 84504636)
    public HomeFace() {
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
}
