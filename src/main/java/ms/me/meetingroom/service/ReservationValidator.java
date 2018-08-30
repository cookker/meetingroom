package ms.me.meetingroom.service;

import ms.me.meetingroom.common.ReservationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ReservationValidator {
    static Logger log = LoggerFactory.getLogger(ReservationValidator.class);

    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter dateDashFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private LocalDateTime from;
    private LocalDateTime to;

    public ReservationValidator(String yyyyMMdd, String fromHHmm, String toHHmm){
        checkValidDateFormat(yyyyMMdd);
        checkValidTimeFormat(fromHHmm);
        checkValidTimeFormat(toHHmm);

        this.from = LocalDateTime.parse(yyyyMMdd + fromHHmm, timeFormatter);
        this.to = LocalDateTime.parse(yyyyMMdd + toHHmm, timeFormatter);
    }

    public static boolean checkDuplicateDate(LocalDateTime from, LocalDateTime to, List<ReservationDateDto> reservedDatetimeList) {
        reservedDatetimeList.stream().forEach(d ->{
            final LocalDateTime start = LocalDateTime.parse(d.fromyyyyMMddHHmm, timeFormatter);
            final LocalDateTime end = LocalDateTime.parse(d.toyyyyMMddHHmm, timeFormatter);

            if (from.isBefore(end) && to.isAfter(end)) {
                log.error("#예약 시작날짜{}와 종료날짜:{}가 기존의 날짜{}~{}에 포함되어있습니다.", from, to, start, end);
                throw new ReservationException("예약시간이 중복됩니다.");
            }
            if(from.isBefore(start) && to.isAfter(start)){
                log.error("@예약 시작날짜{}와 종료날짜:{}가 기존의 날짜{}~{}에 포함되어있습니다.", from, to, start, end);
                throw new ReservationException("예약시간이 중복됩니다.");
            }
            if(from.compareTo(start) == 0 || to.compareTo(end) == 0){
                log.error("$예약 시작날짜{}와 종료날짜:{}가 기존의 날짜{}~{}에 포함되어있습니다.", from, to, start, end);
                throw new ReservationException("예약시간이 중복됩니다.");
            }
            if(from.isAfter(start) && to.isBefore(end)){
                log.error("%예약 시작날짜{}와 종료날짜:{}가 기존의 날짜{}~{}에 포함되어있습니다.", from, to, start, end);
                throw new ReservationException("예약시간이 중복됩니다.");
            }
        });
        return true;
    }

    private void checkValidDateFormat(String yyyyMMdd) {
        if(StringUtils.isEmpty(yyyyMMdd)){
            throw new ReservationException("예약시간에 오류가 있습니다. 시각:" + yyyyMMdd);
        }
        try {
            dateFormatter.parse(yyyyMMdd);
        }catch (DateTimeParseException ignore){
            throw new ReservationException("예약시간에 오류가 있습니다. 시각:" + yyyyMMdd);
        }
    }

    private void checkValidTimeFormat(String HHmm) {
        if(StringUtils.isEmpty(HHmm)){
            throw new ReservationException("예약시간에 오류가 있습니다. 시각:" + HHmm);
        }
        final String dummyDate = "20180101";
        try {
            timeFormatter.parse(dummyDate + HHmm);
        }catch (DateTimeParseException ignore){
            throw new ReservationException("예약시간에 오류가 있습니다. 시각:" + HHmm);
        }
    }

    public ReservationValidator checkValidStardAndEndTime(){
        if(from.compareTo(to) == 0 || from.isAfter(to)){
            log.error("예약시간의 시작시각은 종료시각보다 일러야합니다. 시작시각:{}, 종료시각:{}", from, to);
            throw new ReservationException("예약시간의 시작시각은 종료시각보다 일러야합니다.");
        }
        return this;
    }

    public ReservationValidator checkValid30min(){
        if(from.getMinute()%30 != 0 || to.getMinute()%30 != 0){
            log.error("예약시간은 30분단위로만 설정할 수 있습니다. 시작시각:{}, 종료시각:{}", from, to);
            throw new ReservationException("예약시간은 30분단위로만 설정할 수 있습니다.");
        }
        return this;
    }

    public ReservationValidator checkValidDuration(){
        if(from.getHour() < 9 || to.getHour() > 18){
            log.error("예약시간은 오전9시부터 오후6시까지만 설정할 수 있습니다. 시작시각:{}, 종료시각:{}", from, to);
            throw new ReservationException("예약시간은 오전9시부터 오후6시까지만 설정할 수 있습니다.");
        }
        return this;
    }

    public ReservationValidator checkValidWeekDay(){
        if(from.getDayOfWeek() == DayOfWeek.SUNDAY || from.getDayOfWeek() == DayOfWeek.SATURDAY ||
                to.getDayOfWeek() == DayOfWeek.SUNDAY || to.getDayOfWeek() == DayOfWeek.SATURDAY){
            log.error("예약시간은 월요일부터 금요일까지 가능합니다. .시작요일:{}, 종료요일:{}", from.getDayOfWeek(), to.getDayOfWeek());
            throw new ReservationException("예약시간은 월요일부터 금요일까지 가능합니다.");
        }
        return this;
    }

    public static class ReservationDateDto{
        String fromyyyyMMddHHmm;
        String toyyyyMMddHHmm;

        public ReservationDateDto(String fromyyyyMMddHHmm, String toyyyyMMddHHmm) {
            this.fromyyyyMMddHHmm = fromyyyyMMddHHmm;
            this.toyyyyMMddHHmm = toyyyyMMddHHmm;
        }
    }
}
