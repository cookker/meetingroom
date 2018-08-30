package ms.me.meetingroom.service;

import ms.me.meetingroom.common.DateUtils;
import ms.me.meetingroom.controller.param.ReservationRequest;
import ms.me.meetingroom.controller.param.ReservationUpdateRequest;
import ms.me.meetingroom.entity.Member;
import ms.me.meetingroom.entity.Reservation;
import ms.me.meetingroom.entity.Room;
import ms.me.meetingroom.repository.MemberRepository;
import ms.me.meetingroom.repository.ReservationRepository;
import ms.me.meetingroom.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static ms.me.meetingroom.service.ReservationValidator.dateFormatter;
import static ms.me.meetingroom.service.ReservationValidator.timeFormatter;

@Service
public class ReservationService {
    static Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.roomRepository = roomRepository;
    }

    private static final int NUMBER_OF_WEEK = 7;

    @Transactional
    public List<Reservation> createReservation(ReservationRequest request) {
        final Optional<Member> optionalMember = memberRepository.findByName(request.getMemberName());
        final Optional<Room> optionalRoom = roomRepository.findByName(request.getRoomName());

        String [] fromToHHmm = DateUtils.splitTimes(request.getTime());
        return this.createReservation(optionalMember.get(), optionalRoom.get(), request.getDate(), fromToHHmm[0], fromToHHmm[1], request.getRepeat(), request.getComment());
    }

    @Transactional
    public List<Reservation> createReservation(Member member, Room room
            , String yyyyMMdd, String fromHHmm, String toHHmm
            , int repeatableCount, String comment){
        List<Reservation> reservationList = new ArrayList<>();

        IntStream.range(0, repeatableCount).forEach(i -> {
            final String reserveDate = LocalDate.parse(yyyyMMdd, dateFormatter).plusDays(i * NUMBER_OF_WEEK).format(dateFormatter);
            Reservation reservation = new Reservation();
            reservation.setReservedDate(reserveDate);
            reservation.setReservedTimeFrom(fromHHmm);
            reservation.setReservedTimeTo(toHHmm);
            reservation.setComment(comment);
            reservation.setDayOfWeek(LocalDate.parse(yyyyMMdd, dateFormatter).getDayOfWeek());
            reservation.setRepeatSeq(i);
            reservation.setRepeatTotal(repeatableCount);
            this.checkValidDateAndTimeFormat(reservation);

            member.addReservation(reservation);
            room.addReservation(reservation);
            reservationList.add(reservation);

            isContainsReservedDate(room.getName(), reserveDate, fromHHmm, toHHmm);
        });
        reservationRepository.saveAll(reservationList);

        return reservationList;
    }

    public void isContainsReservedDate(String roomName, String yyyyMMdd, String fromHHmm, String toHHmm){
        final List<Reservation> reservationList = this.getReservationList(roomName, yyyyMMdd);
        final LocalDateTime start = LocalDateTime.parse(yyyyMMdd + fromHHmm, timeFormatter);
        final LocalDateTime end = LocalDateTime.parse(yyyyMMdd + toHHmm, timeFormatter);

        List<ReservationValidator.ReservationDateDto> datetimeList = new ArrayList<>();
        reservationList.forEach(r -> datetimeList.add(new ReservationValidator.ReservationDateDto(
                r.getReservedDate()+r.getReservedTimeFrom(),
                r.getReservedDate()+r.getReservedTimeTo())));

        ReservationValidator.checkDuplicateDate(start, end, datetimeList);
    }

    public List<Reservation> getReservationList(String reserveDateyyyyMMdd) {
        return reservationRepository.findAllByReservedDate(reserveDateyyyyMMdd);
    }

    private void checkValidDateAndTimeFormat(Reservation reservation){
        ReservationValidator validator = new ReservationValidator(reservation.getReservedDate()
                , reservation.getReservedTimeFrom()
                , reservation.getReservedTimeTo());

        validator.checkValidStardAndEndTime()
                .checkValidWeekDay()
                .checkValidDuration()
                .checkValid30min();
    }

    public List<Reservation> getReservationList(long memberId, long roomId) {
        return reservationRepository.findAllByMemberIdAndRoomId(memberId, roomId);
    }

    public List<Reservation> getReservationList(String roomName, String reservedDate) {
        final String monday = DateUtils.getMonday(reservedDate);
        final String friday = DateUtils.getFriday(reservedDate);
        return reservationRepository.findAllByRoomNameAndDate(roomName, monday, friday);
    }

    public Reservation getReservation(String roomName, String reservedDate, String from, String to) {
        Optional<Reservation> optionalReservation = reservationRepository.findByRoomNameAndReservedDateTime(roomName, reservedDate, from, to);
        return optionalReservation.orElseThrow(() -> new RuntimeException("찾는 데이터가 없습니다."));
    }

    @Transactional
    public void deleteReservation(Long reservationId) {
        final Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        optionalReservation.orElseThrow(() -> new RuntimeException("삭제하려는 데이터가 없습니다."));
        final Reservation reservation = optionalReservation.get();
        final Room room = reservation.getRoom();
        room.removeReservation(reservation);
        final Member member = reservation.getMember();
        member.removeReservation(reservation);

        reservationRepository.deleteById(reservationId);
    }

    @Transactional
    public void updateReservation(ReservationUpdateRequest request) {
        final Optional<Reservation> optionalReservation = reservationRepository.findById(request.getReservationId());
        optionalReservation.orElseThrow(() -> new RuntimeException("수정하려는 데이터가 없습니다."));
        final Reservation reservation = optionalReservation.get();
        reservation.setComment(request.getComment());
        reservation.setReservedDate(request.getReservedDate());
        final String[] splitTimes = DateUtils.splitTimes(request.getReservedTime());
        reservation.setReservedTimeFrom(splitTimes[0]);
        reservation.setReservedTimeTo(splitTimes[1]);

        //검증.
        this.checkValidDateAndTimeFormat(reservation);
        isContainsReservedDate(reservation.getRoom().getName(), reservation.getReservedDate(), reservation.getReservedTimeFrom(), reservation.getReservedTimeTo());
    }
}
