package ms.me.meetingroom.common;

import lombok.RequiredArgsConstructor;
import ms.me.meetingroom.entity.Member;
import ms.me.meetingroom.entity.Room;
import ms.me.meetingroom.service.MemberService;
import ms.me.meetingroom.service.ReservationService;
import ms.me.meetingroom.service.RoomService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static ms.me.meetingroom.service.ReservationValidator.dateFormatter;

@Component
@RequiredArgsConstructor
public class InitLoaderRunner implements ApplicationRunner {
    private final RoomService roomService;
    private final MemberService memberService;
    private final ReservationService reservationService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Room room1 = roomService.createRoom("회의실A");
        final Room room2 = roomService.createRoom("회의실B");
        final Room room3 = roomService.createRoom("회의실C");

        final Member member = memberService.createMember("user", "1234");

        final String today = LocalDate.parse("20180808", dateFormatter).format(dateFormatter);
        final String yesterday = LocalDate.parse("20180807", dateFormatter).format(dateFormatter);
        final String nextday = LocalDate.parse("20180809", dateFormatter).format(dateFormatter);
        reservationService.createReservation(member, room1, today, "1030", "1230", 1, "회의실에서는조용히");
        reservationService.createReservation(member, room1, yesterday, "0930", "1230", 1, "월간회의");
        reservationService.createReservation(member, room1, nextday, "1030", "1230", 1, "주간회의");

        reservationService.createReservation(member, room2, today, "0900", "1600", 1, "질문하는회의");
        reservationService.createReservation(member, room2, yesterday, "0900", "1100", 1, "해외프로젝트건");
        reservationService.createReservation(member, room2, nextday, "1730", "1800", 1, "TF회의");

        reservationService.createReservation(member, room3, today, "1100", "1130", 1,"밥먹으면서하는회의");
        reservationService.createReservation(member, room3, yesterday, "1300", "1530", 1,"워크샵");
        reservationService.createReservation(member, room3, nextday, "0900", "1800", 1,"기획디자인미팅");
    }
}
