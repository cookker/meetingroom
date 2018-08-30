package ms.me.meetingroom.repository;

import ms.me.meetingroom.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.reservedDate = :yyyyMMdd")
    List<Reservation> findAllByReservedDate(@Param("yyyyMMdd") String yyyyMMdd);

    @Query("SELECT r FROM Reservation r JOIN r.member m WHERE r.reservedDate = :yyyyMMdd AND m.memberId = :memberId")
    List<Reservation> findAllByReservedDateAndMemberId(@Param("yyyyMMdd") String yyyyMMdd, @Param("memberId") Long memberId);

    @Query("SELECT r FROM Reservation r JOIN r.room mr  WHERE r.reservedDate = :yyyyMMdd AND mr.roomId = :roomId")
    List<Reservation> findAllByReservedDateAndRoomId(@Param("yyyyMMdd") String yyyyMMdd, @Param("roomId") Long roomId);

    @Query("SELECT r FROM Reservation r JOIN r.member m JOIN r.room mr WHERE m.memberId = :memberId AND mr.roomId = :roomId")
    List<Reservation> findAllByMemberIdAndRoomId(@Param("memberId") Long memberId, @Param("roomId") Long roomId);

    @Query("SELECT r FROM Reservation r JOIN r.room mr WHERE mr.name = :roomName AND r.reservedDate BETWEEN :monday AND :friday ORDER BY r.dayOfWeek")
    List<Reservation> findAllByRoomNameAndDate(@Param("roomName") String roomName, @Param("monday") String monday, @Param("friday") String friday);

    @Query("SELECT r FROM Reservation r JOIN r.room mr WHERE " +
            "mr.name = :roomName AND r.reservedDate = :reservedDate AND r.reservedTimeFrom = :fromTime AND r.reservedTimeTo = :toTime")
    Optional<Reservation> findByRoomNameAndReservedDateTime(@Param("roomName") String roomName,
                                                            @Param("reservedDate") String reservedDate,
                                                            @Param("fromTime") String fromTime,
                                                            @Param("toTime") String toTime);
}
