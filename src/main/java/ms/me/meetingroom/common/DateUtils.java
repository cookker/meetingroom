package ms.me.meetingroom.common;

import ms.me.meetingroom.controller.param.WeekDayResponse;
import org.springframework.lang.NonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

import static ms.me.meetingroom.service.ReservationValidator.dateDashFormatter;
import static ms.me.meetingroom.service.ReservationValidator.dateFormatter;

public final class DateUtils {
    private DateUtils(){
        //don't use
    }

    public static String getOnlyNumberDate(@NonNull  String date){
        Objects.requireNonNull(date);
        return date.replaceAll("[^0-9]", "");
    }

    public static String getMonday(@NonNull String yyyyMMdd){
        final LocalDate date = LocalDate.parse(DateUtils.getOnlyNumberDate(yyyyMMdd), dateFormatter);
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).format(dateFormatter);
    }

    public static String getFriday(@NonNull String yyyyMMdd){
        final LocalDate date = LocalDate.parse(DateUtils.getOnlyNumberDate(yyyyMMdd), dateFormatter);
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY)).format(dateFormatter);
    }

    public static String[] splitTimes(@NonNull String time) {
        String onlyNumberTime = time.replaceAll("[^0-9]", "");
        if(onlyNumberTime.length() != 8){
            throw new RuntimeException("시간에 대한 날짜 포멧이 맞지 않습니다.");
        }
        return new String[]{onlyNumberTime.substring(0,4), onlyNumberTime.substring(4,8)};
    }

    public static WeekDayResponse getPreviousAndNextWeekDay(@NonNull String currentDate) {
        final String numberDate = DateUtils.getOnlyNumberDate(currentDate);
        final LocalDate now = LocalDate.parse(numberDate, dateFormatter);

        WeekDayResponse weekDayResponse = new WeekDayResponse(
                now.minusDays(7).format(dateDashFormatter),
                now.format(dateDashFormatter),
                now.plusDays(7).format(dateDashFormatter),
                getMonday(now.format(dateFormatter)),
                getFriday(now.format(dateFormatter)));
        return weekDayResponse;
    }
}
