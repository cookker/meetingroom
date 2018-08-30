package ms.me.meetingroom.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.DayOfWeek;

@Setter
@Getter
@ToString
@Slf4j
@Entity
@NoArgsConstructor
public class Reservation {
    @Id @GeneratedValue
    @Setter(AccessLevel.NONE)
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
}
