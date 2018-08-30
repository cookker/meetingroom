package ms.me.meetingroom.common;


import ms.me.meetingroom.controller.param.WeekDayResponse;
import org.junit.Ignore;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilsTest {

    @Test
    public void 날짜에서특수문자가_포함되지_않아야한다(){
        String date = "2018-08-25";
        final String onlyNumber = date.replaceAll("[^0-9]", "");
        assertThat(onlyNumber).isEqualTo("20180825");
    }

    @Test
    public void 입력받은날짜로_월요일계산(){
        final LocalDate day1 = LocalDate.parse("20180829", DateTimeFormatter.ofPattern("yyyyMMdd"));
        assertThat(day1.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()).isEqualTo("2018-08-27");

        final LocalDate day2 = LocalDate.parse("20180903", DateTimeFormatter.ofPattern("yyyyMMdd"));
        assertThat(day2.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()).isEqualTo("2018-09-03");

        final LocalDate day3 = LocalDate.parse("20180801", DateTimeFormatter.ofPattern("yyyyMMdd"));
        assertThat(day3.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()).isEqualTo("2018-07-30");
    }

    @Test
    public void 입력받은날짜로_월금계산(){
        assertThat(DateUtils.getMonday("20180829")).isEqualTo("20180827");
        assertThat(DateUtils.getFriday("20180829")).isEqualTo("20180831");
    }

    @Test
    public void 시간날짜_fromTo_에대한포맷확인(){
        final String[] times = DateUtils.splitTimes("1100~1200");
        assertThat(times[0]).isEqualTo("1100");
        assertThat(times[1]).isEqualTo("1200");
    }

    @Test
    public void 이전주날짜와_다음주날짜를_계산한다(){
        final WeekDayResponse previousAndNextWeekDay = DateUtils.getPreviousAndNextWeekDay("20180829");
        assertThat(previousAndNextWeekDay.getPreviousWeekDay()).isEqualTo("2018-08-22");
        assertThat(previousAndNextWeekDay.getToday()).isEqualTo("2018-08-29");
        assertThat(previousAndNextWeekDay.getNextWeekDay()).isEqualTo("2018-09-05");
        assertThat(previousAndNextWeekDay.getMonday()).isEqualTo("20180827");
        assertThat(previousAndNextWeekDay.getFriday()).isEqualTo("20180831");
    }
}