package ms.me.meetingroom.controller.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReservationRequest {
    private String memberName;
    private String roomName;
    private String date;
    private String time;
    private int repeat;
    private String comment;
}
