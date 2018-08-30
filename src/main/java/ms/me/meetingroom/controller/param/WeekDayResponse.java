package ms.me.meetingroom.controller.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class WeekDayResponse {
    private String previousWeekDay;
    private String today;
    private String nextWeekDay;
    private String monday;
    private String friday;
}
