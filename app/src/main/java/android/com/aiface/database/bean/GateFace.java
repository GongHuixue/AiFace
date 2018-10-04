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
    private String facePath;
    @NotNull
    private String userName;
    @Generated(hash = 777034890)
    public GateFace(Long id, @NotNull String facePath, @NotNull String userName) {
        this.id = id;
        this.facePath = facePath;
        this.userName = userName;
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
    public String getFacePath() {
        return this.facePath;
    }
    public void setFacePath(String facePath) {
        this.facePath = facePath;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
