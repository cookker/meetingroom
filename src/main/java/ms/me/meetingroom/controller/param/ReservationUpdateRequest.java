package ms.me.meetingroom.controller.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReservationUpdateRequest {
    private Long reservationId;
    private String comment;
    private String reservedDate;
    private String reservedTime;
}
