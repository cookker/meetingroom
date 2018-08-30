package ms.me.meetingroom.repository;

import ms.me.meetingroom.entity.Member;
import ms.me.meetingroom.entity.Reservation;
import ms.me.meetingroom.entity.Room;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReservationRepositoryTest {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void repositoryInjectionConfirm(){
        assertThat(reservationRepository).isNotNull();
        assertThat(roomRepository).isNotNull();
        assertThat(memberRepository).isNotNull();
    }

    @Test
    @Rollback(false)
    public void 관계설정확인(){
        reservationRepository.deleteAll();
        final String userName = "사용자1";
        final String roomName = "회의실A";
        Member member = createMember(userName);

        Room room = createRoom(roomName);

        Reservation reservation = new Reservation();
        reservation.setComment("여긴 내구역임");
        reservation.setRoom(room);
        reservation.setMember(member);
        reservationRepository.save(reservation);

        member.addReservation(reservation);
        room.addReservation(reservation);

        final List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations.get(0).getMember().getName()).isEqualTo(userName);
        assertThat(reservations.get(0).getRoom().getName()).isEqualTo(roomName);

        assertThat(member.getReservations()).isNotNull();
        assertThat(room.getReservations()).isNotNull();
    }

    @Test
    @Rollback(false)
    public void 예약을_할수있다(){
        Member member = createMember();
        Room room = createRoom();

        Reservation reservation = new Reservation();
        reservation.setMember(member);
        reservation.setRoom(room);

        reservation.setReservedDate("20180826");
        reservation.setReservedTimeFrom("1300");
        reservation.setReservedTimeTo("1400");
        reservation.setComment("코멘트");
        reservation.setDayOfWeek(LocalDate.of(2018, 8, 26).getDayOfWeek());
//        reservation.setRepeatableCount(1);

        reservationRepository.save(reservation);
        final Optional<Reservation> optionalReservation = reservationRepository.findById(reservation.getReservationId());
        assertThat(optionalReservation).isNotEmpty();
    }

    @Test
    public void 예약한날짜로조회하면_예약날짜의모든데이터를조회한다(){
        reservationRepository.deleteAll();

        final int loop = 3;
        createReservation(loop, createMember(), createRoom());
        final List<Reservation> reservationList = reservationRepository.findAllByReservedDate("20180826");
        assertThat(reservationList.size()).isEqualTo(loop);
    }


    @Test
    public void 예약한날짜와_사용자로조회(){
        final Member member = createMember();
        createReservation(100, member, createRoom());
        final List<Reservation> reservationList = reservationRepository.findAllByReservedDateAndMemberId("20180826", member.getMemberId());
        assertThat(reservationList.size()).isEqualTo(100);
    }

    @Test
    public void 예약한날짜와_회의실로조회(){
        final Room room = createRoom();
        createReservation(100, createMember(), room);
        final List<Reservation> reservationList = reservationRepository.findAllByReservedDateAndRoomId("20180826", room.getRoomId());
        assertThat(reservationList.size()).isEqualTo(100);
    }

    @Test
    public void 회원과_회의실로조회(){
        final Member member = createMember();
        final Room room = createRoom();
        createReservation(11, member, room);
        final List<Reservation> reservationList = reservationRepository.findAllByMemberIdAndRoomId(member.getMemberId(), room.getRoomId());
        assertThat(reservationList.size()).isEqualTo(11);
    }

    @Test
    public void 방이름과_날짜로조회(){
        final Member member = createMember();
        final Room room = createRoom("testRoom");
        createReservation(11, member, room);
        final List<Reservation> reservationList = reservationRepository.findAllByRoomNameAndDate("testRoom", "20180826", "20180831");
        assertThat(reservationList.size()).isEqualTo(11);
    }



    private Member createMember(String userName) {
        Member member = new Member();
        member.setName(userName);
        member.setPassword("1234");
        memberRepository.save(member);
        return member;
    }

    private Member createMember() {
        return createMember("사용자_" + RandomStringUtils.randomAlphanumeric(5));
    }

    private Room createRoom(String roomName) {
        Room room = new Room();
        room.setName(roomName);
        roomRepository.save(room);
        return room;
    }

    private Room createRoom() {
        return createRoom("회의실_" + RandomStringUtils.randomAlphanumeric(5));
    }

    private void createReservation(int loop, Member member, Room room) {
        final String reservedDate = "20180826";
        IntStream.rangeClosed(1, loop).forEach(i -> {
            Reservation reservation = new Reservation();
            reservation.setComment("예약1");
            reservation.setMember(member);
            reservation.setRoom(room);
            reservation.setReservedDate(reservedDate);
            reservation.setReservedTimeFrom("1000");
            reservation.setReservedTimeTo("1200");
            reservationRepository.save(reservation);
        });
    }

}