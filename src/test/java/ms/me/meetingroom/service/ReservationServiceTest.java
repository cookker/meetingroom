package ms.me.meetingroom.service;

import ms.me.meetingroom.common.ReservationException;
import ms.me.meetingroom.entity.Member;
import ms.me.meetingroom.entity.Reservation;
import ms.me.meetingroom.entity.Room;
import ms.me.meetingroom.repository.MemberRepository;
import ms.me.meetingroom.repository.ReservationRepository;
import ms.me.meetingroom.repository.RoomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoomRepository roomRepository;

    @Test
    public void 날짜로_요일계산(){
        assertThat(LocalDate.of(2018, 8,26).getDayOfWeek()).isEqualTo(DayOfWeek.SUNDAY);
    }

    @Test
    public void 반복예약일경우_다음주날짜계산(){
        String yyyyMMdd = "20180827";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        System.out.println(LocalDate.parse(yyyyMMdd, formatter).plusDays(0 * 7).toString());
        System.out.println(LocalDate.parse(yyyyMMdd, formatter).plusDays(1 * 7).toString());
        System.out.println(LocalDate.parse(yyyyMMdd, formatter).plusDays(2 * 7).toString());
    }

    @Test
    @Rollback(false)
    @Transactional
    public void 반복예약의_회수만큼_예약이되야한다(){
        Member member = new Member();
        member.setName("사용자1A");
        member.setPassword("1234");
        final Member savedMember = memberRepository.save(member);

        Room room = new Room();
        room.setName("회의실1B");
        final Room savedRoom = roomRepository.save(room);

        reservationService.createReservation(savedMember
        , savedRoom
        , "20180827"
        , "1600"
        ,"1700"
        , 11
        ,"필요한회의");

        List<Reservation> reservations = reservationService.getReservationList(savedMember.getMemberId(), savedRoom.getRoomId());
        assertThat(reservations.size()).isEqualTo(11);
    }

    @Test
    @Transactional
    public void 예약조회를할경우_날짜를입력받은것으로_일주일치를조회해야한다(){

        Member member = new Member();
        member.setMemberId(1L);
        member.setName("사용자1");
        member.setPassword("1234");
        final Member savedMember = memberRepository.save(member);

        Room room = new Room();
        room.setName("회의실1");
        room.setRoomId(1L);
        final Room savedRoom = roomRepository.save(room);

        reservationService.createReservation(savedMember, savedRoom, "20180723", "1600","1700", 1 ,"필요한회의");
        reservationService.createReservation(savedMember, savedRoom, "20180727", "1600","1700", 1 ,"필요한회의");
        reservationService.createReservation(savedMember, savedRoom, "20180725", "1600","1700", 1 ,"필요한회의");
        reservationService.createReservation(savedMember, savedRoom, "20180710", "1600","1700", 1 ,"필요한회의");
        reservationService.createReservation(savedMember, savedRoom, "20180731", "1600","1700", 1 ,"필요한회의");

        final List<Reservation> reservationList = reservationService.getReservationList(savedRoom.getName(), "20180725");
        assertThat(reservationList.size()).isEqualTo(3);
    }

    @Test
    public void 반복예약의경우에도_이미예약한곳에_중복으로예약이되면안된다(){
        final Room room1 = createRoom("회의실1");
        final Member member = createMember("user_1", "1234");

        final String today = LocalDate.now().format(ReservationValidator.dateFormatter);
        final String yesterday = LocalDate.now().minusDays(1).format(ReservationValidator.dateFormatter);
        final String nextday = LocalDate.now().plusDays(1).format(ReservationValidator.dateFormatter);
        reservationService.createReservation(member, room1, today, "1030", "1230", 1, "회의실에서는조용히");
        reservationService.createReservation(member, room1, yesterday, "0930", "1230", 1, "회의실에서는조용히");
        reservationService.createReservation(member, room1, nextday, "1030", "1230", 1, "회의실에서는조용히");
        //중복.
        assertThat(catchThrowable(()
                -> reservationService.createReservation(member, room1, "20180801", "1100", "1200", 10, "중복이되는가")))
                .isInstanceOf(ReservationException.class);
    }

    @Test
    public void 방이름_예약날짜및시간으로조회(){
        final Room room1 = createRoom("회의실1C");
        final Member member = createMember("user_3", "1234");

        final String today = LocalDate.now().format(ReservationValidator.dateFormatter);
        reservationService.createReservation(member, room1, today, "1030", "1230", 1, "회의실에서는조용히");
        final Reservation reservation = reservationService.getReservation(room1.getName(), today, "1030", "1230");
        assertThat(reservation).isNotNull();

    }

    @Test
    public void 예약정보_삭제(){
        final Room room1 = createRoom("회의실1z");
        final Member member = createMember("userz", "1234");
        final List<Reservation> reservations = reservationService.createReservation(member, room1, "20100101", "1030", "1230", 1, "회의실에서는조용히");
        reservationService.deleteReservation(reservations.get(0).getReservationId());
    }

    private Member createMember(String userName, String password) {
        Member member = new Member();
        member.setName(userName);
        member.setPassword(password);
        memberRepository.save(member);
        return member;
    }

    private Room createRoom(String roomName) {
        Room room = new Room();
        room.setName(roomName);
        roomRepository.save(room);
        return room;
    }
}