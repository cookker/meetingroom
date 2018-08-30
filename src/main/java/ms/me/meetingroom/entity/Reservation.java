package ms.me.meetingroom.entity;

import javax.persistence.*;
import java.time.DayOfWeek;

@Entity
public class Reservation {
    @Id @GeneratedValue
    private Long reservationId;
    @ManyToOne
    private Member member;
    @ManyToOne
    private Room room;
    private String comment;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    private String reservedDate;
    private String reservedTimeFrom;
    private String reservedTimeTo;
    private int repeatSeq;
    private int repeatTotal;

    public Reservation() {
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", member=" + member +
                ", room=" + room +
                ", comment='" + comment + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                ", reservedDate='" + reservedDate + '\'' +
                ", reservedTimeFrom='" + reservedTimeFrom + '\'' +
                ", reservedTimeTo='" + reservedTimeTo + '\'' +
                ", repeatSeq=" + repeatSeq +
                ", repeatTotal=" + repeatTotal +
                '}';
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }

    public String getReservedTimeFrom() {
        return reservedTimeFrom;
    }

    public void setReservedTimeFrom(String reservedTimeFrom) {
        this.reservedTimeFrom = reservedTimeFrom;
    }

    public String getReservedTimeTo() {
        return reservedTimeTo;
    }

    public void setReservedTimeTo(String reservedTimeTo) {
        this.reservedTimeTo = reservedTimeTo;
    }

    public int getRepeatSeq() {
        return repeatSeq;
    }

    public void setRepeatSeq(int repeatSeq) {
        this.repeatSeq = repeatSeq;
    }

    public int getRepeatTotal() {
        return repeatTotal;
    }

    public void setRepeatTotal(int repeatTotal) {
        this.repeatTotal = repeatTotal;
    }
}
