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
    @Generated(hash = 1102875792)
    public GateFace(Long id, @NotNull String facePath) {
        this.id = id;
        this.facePath = facePath;
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
}
