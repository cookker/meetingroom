package ms.me.meetingroom.controller.param;

public class ReservationUpdateRequest {
    private Long reservationId;
    private String comment;
    private String reservedDate;
    private String reservedTime;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }

    public String getReservedTime() {
        return reservedTime;
    }

    public void setReservedTime(String reservedTime) {
        this.reservedTime = reservedTime;
    }

    @Override
    public String toString() {
        return "ReservationUpdateRequest{" +
                "reservationId=" + reservationId +
                ", comment='" + comment + '\'' +
                ", reservedDate='" + reservedDate + '\'' +
                ", reservedTime='" + reservedTime + '\'' +
                '}';
    }
}
