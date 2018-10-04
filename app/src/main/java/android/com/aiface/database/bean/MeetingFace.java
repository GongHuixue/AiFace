package android.com.aiface.database.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MeetingFace extends FaceBean{
    @Id
    private Long id;
    @NotNull
    private String meetingName;
    private long meetingTime;
    @NotNull
    private String meetingTimeString;
    @NotNull
    private String meetingAddr;

    @NotNull
    private String participantName;
    @NotNull
    private String participantPart;
    @Generated(hash = 342336224)
    public MeetingFace(Long id, @NotNull String meetingName, long meetingTime,
            @NotNull String meetingTimeString, @NotNull String meetingAddr,
            @NotNull String participantName, @NotNull String participantPart) {
        this.id = id;
        this.meetingName = meetingName;
        this.meetingTime = meetingTime;
        this.meetingTimeString = meetingTimeString;
        this.meetingAddr = meetingAddr;
        this.participantName = participantName;
        this.participantPart = participantPart;
    }

    public MeetingFace() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMeetingName() {
        return this.meetingName;
    }
    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }
    public long getMeetingTime() {
        return this.meetingTime;
    }
    public void setMeetingTime(long meetingTime) {
        this.meetingTime = meetingTime;
    }
    public String getMeetingAddr() {
        return this.meetingAddr;
    }
    public void setMeetingAddr(String meetingAddr) {
        this.meetingAddr = meetingAddr;
    }
    public String getParticipantName() {
        return this.participantName;
    }
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }
    public String getParticipantPart() {
        return this.participantPart;
    }
    public void setParticipantPart(String participantPart) {
        this.participantPart = participantPart;
    }

    public String getMeetingTimeString() {
        return this.meetingTimeString;
    }

    public void setMeetingTimeString(String meetingTimeString) {
        this.meetingTimeString = meetingTimeString;
    }
}
