package ms.me.meetingroom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ms.me.meetingroom.common.DateUtils;
import ms.me.meetingroom.common.Response;
import ms.me.meetingroom.controller.param.ReservationRequest;
import ms.me.meetingroom.controller.param.ReservationUpdateRequest;
import ms.me.meetingroom.controller.param.WeekDayResponse;
import ms.me.meetingroom.entity.Reservation;
import ms.me.meetingroom.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/reservations/{roomName}")
    public List<Reservation> getReservations(@PathVariable String roomName, @RequestParam("date") String weekOfdate){
        log.info("roomName:{}, today:{}", roomName, weekOfdate);
        return reservationService.getReservationList(roomName, DateUtils.getOnlyNumberDate(weekOfdate));
    }

    @GetMapping("/reservations/{roomName}/{date}/{from}/{to}")
    public Reservation getReservationsOnDate(@PathVariable String roomName,
                                                   @PathVariable("date") String date,
                                                   @PathVariable("from") String from,
                                                   @PathVariable("to") String to){
        log.info("roomName:{}, today:{}", roomName, date);
        return reservationService.getReservation(roomName, DateUtils.getOnlyNumberDate(date), from, to);
    }

    @PostMapping("/reservation")
    public Response<String> createReservation(@RequestBody ReservationRequest request){
        log.info("예약하기위해 받은 파라미터 정보: {}", request);
        reservationService.createReservation(request);
        return Response.success("ok");
    }

    @GetMapping("/dates/{currentDate}")
    public Response<WeekDayResponse> getCurrentWeekDay(@PathVariable("currentDate") String currentDate){
        WeekDayResponse weekDayResponse = DateUtils.getPreviousAndNextWeekDay(currentDate);
        return Response.success(weekDayResponse);
    }

    @DeleteMapping("/reservation/{reservationId}")
    public Response<String> deleteReservation(@PathVariable("reservationId") Long reservationId){
        reservationService.deleteReservation(reservationId);
        return Response.success("ok");
    }

    @PutMapping("/reservation/{reservationId}")
    public Response<String> updateReservation(@PathVariable("reservationId") Long reservationId,
                                              @RequestBody ReservationUpdateRequest request){
        request.setReservationId(reservationId);
        reservationService.updateReservation(request);
        return Response.success("ok");
    }
}
