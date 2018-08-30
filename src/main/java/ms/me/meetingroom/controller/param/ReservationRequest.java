package ms.me.meetingroom.controller.param;

public class ReservationRequest {
    private String memberName;
    private String roomName;
    private String date;
    private String time;
    private int repeat;
    private String comment;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ReservationRequest{" +
                "memberName='" + memberName + '\'' +
                ", roomName='" + roomName + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", repeat=" + repeat +
                ", comment='" + comment + '\'' +
                '}';
    }
}
