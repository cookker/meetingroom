package ms.me.meetingroom.service;

import ms.me.meetingroom.common.ReservationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ms.me.meetingroom.service.ReservationValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ReservationValidatorTest {

    @Test
    public void 날짜포맷확인() {
        assertThat(catchThrowable(() -> new ReservationValidator("", "", "")))
                .isInstanceOf(ReservationException.class);
        assertThat(catchThrowable(() -> new ReservationValidator("12345678", "1100", "1200")))
                .isInstanceOf(ReservationException.class);
        assertThat(catchThrowable(() -> new ReservationValidator("2o180101", "1100", "1200")))
                .isInstanceOf(ReservationException.class);
        assertThat(catchThrowable(() -> new ReservationValidator("20180101", "", "")))
                .isInstanceOf(ReservationException.class);
        assertThat(catchThrowable(() -> new ReservationValidator("20180101", "230a", "")))
                .isInstanceOf(ReservationException.class);
        assertThat(catchThrowable(() -> new ReservationValidator("20180101", "1100", "230a")))
                .isInstanceOf(ReservationException.class);
        assertThat(catchThrowable(() -> new ReservationValidator("20180101", "2501", "2502")))
                .isInstanceOf(ReservationException.class);
        assertThat(new ReservationValidator("20180101", "1100", "1200")).isNotNull();
    }

    @Test
    public void 예약시작시각과종료시각에대한검증_시각이같으면안된다() {
        assertThat(catchThrowable(() -> new ReservationValidator("20180101", "1100", "1100")
                .checkValidStardAndEndTime()))
                .isInstanceOf(ReservationException.class);
    }

    @Test
    public void 예약시작시각과종료시각에대한검증_종료시각이더늦어야한다() {
        assertThat(catchThrowable(() -> new ReservationValidator("20180101", "1330", "1300")
                .checkValidStardAndEndTime()))
                .isInstanceOf(ReservationException.class);

        assertThat(new ReservationValidator("20180101", "1330", "1400")
                .checkValidStardAndEndTime()).isNotNull();
    }

    @Test
    public void 예약시각은_30분단위로가능하다() {
        assertThat(catchThrowable(() -> new ReservationValidator("20180101", "1331", "1400")
                .checkValid30min()))
                .isInstanceOf(ReservationException.class);
        assertThat(catchThrowable(() -> new ReservationValidator("20180101", "1330", "1401")
                .checkValid30min()))
                .isInstanceOf(ReservationException.class);
        assertThat(catchThrowable(() -> new ReservationValidator("20180101", "1335", "1405")
                .checkValid30min()))
                .isInstanceOf(ReservationException.class);
        assertThat(new ReservationValidator("20180101", "1330", "1400")
                .checkValid30min()).isNotNull();
        assertThat(new ReservationValidator("20180101", "1300", "1430")
                .checkValid30min()).isNotNull();
    }

    @Test
    public void 에약시작시각과종료시각에대한검증_근무시간내에서예약가능하다() {
        assertThat(catchThrowable(() -> new ReservationValidator("20180826", "1300", "1400")
                .checkValidWeekDay()))
                .isInstanceOf(ReservationException.class);
        assertThat(catchThrowable(() -> new ReservationValidator("20180825", "1300", "1400")
                .checkValidWeekDay()))
                .isInstanceOf(ReservationException.class);
        assertThat(new ReservationValidator("20180827", "1300", "1400").checkValidWeekDay()).isNotNull();
    }

    @Test
    public void 예약시작과종료시각에대한검증_근무시간만예약가능하다() {
        assertThat(catchThrowable(() -> new ReservationValidator("20180825", "0600", "0700")
                .checkValidDuration()))
                .isInstanceOf(ReservationException.class);
        assertThat(catchThrowable(() -> new ReservationValidator("20180825", "0600", "1300")
                .checkValidDuration()))
                .isInstanceOf(ReservationException.class);
        assertThat(catchThrowable(() -> new ReservationValidator("20180825", "1600", "2300")
                .checkValidDuration()))
                .isInstanceOf(ReservationException.class);
        assertThat(new ReservationValidator("20180824", "0900", "1800").checkValidDuration()).isNotNull();
        assertThat(new ReservationValidator("20180824", "0900", "1000").checkValidDuration()).isNotNull();
        assertThat(new ReservationValidator("20180824", "1700", "1800").checkValidDuration()).isNotNull();
    }

    @Test
    public void 예약은_중복해서_할수없다() {
        ReservationDateDto dateDto1 = new ReservationDateDto("201808270900", "201808271100");
        ReservationDateDto dateDto2 = new ReservationDateDto("201808271300", "201808271500");
        ReservationDateDto dateDto3 = new ReservationDateDto("201808271730", "201808271800");
        List<ReservationDateDto> reservedDateDtos = new ArrayList<>(Arrays.asList(dateDto1, dateDto2, dateDto3));

        assertThat(catchThrowable(() -> checkDuplicateDate(
                LocalDateTime.of(2018, 8, 27, 10, 0)
                , LocalDateTime.of(2018, 8, 27, 11, 30)
                , reservedDateDtos))).isInstanceOf(ReservationException.class);

        assertThat(catchThrowable(() -> checkDuplicateDate(
                LocalDateTime.of(2018, 8, 27, 11, 0)
                , LocalDateTime.of(2018, 8, 27, 13, 30)
                , reservedDateDtos))).isInstanceOf(ReservationException.class);

        assertThat(catchThrowable(() -> checkDuplicateDate(
                LocalDateTime.of(2018, 8, 27, 17, 30)
                , LocalDateTime.of(2018, 8, 27, 18, 0)
                , reservedDateDtos))).isInstanceOf(ReservationException.class);

        assertThat(catchThrowable(() -> checkDuplicateDate(
                LocalDateTime.of(2018, 8, 27, 13, 30)
                , LocalDateTime.of(2018, 8, 27, 18, 0)
                , reservedDateDtos))).isInstanceOf(ReservationException.class);

        assertThat(catchThrowable(() -> checkDuplicateDate(
                LocalDateTime.of(2018, 8, 27, 9, 0)
                , LocalDateTime.of(2018, 8, 27, 18, 0)
                , reservedDateDtos))).isInstanceOf(ReservationException.class);

        assertThat(catchThrowable(() -> checkDuplicateDate(
                LocalDateTime.of(2018, 8, 27, 14, 0)
                , LocalDateTime.of(2018, 8, 27, 14, 30)
                , reservedDateDtos))).isInstanceOf(ReservationException.class);
    }

    @Test
    public void 예약은_중복이아니면_할수있다() {
        ReservationDateDto dateDto1 = new ReservationDateDto("201808270900", "201808271100");
        ReservationDateDto dateDto2 = new ReservationDateDto("201808271300", "201808271500");
        ReservationDateDto dateDto3 = new ReservationDateDto("201808271730", "201808271800");
        List<ReservationDateDto> reservedDateDtos = new ArrayList<>(Arrays.asList(dateDto1, dateDto2, dateDto3));

        assertThat(checkDuplicateDate(
                LocalDateTime.of(2018, 8, 27, 11, 0)
                , LocalDateTime.of(2018, 8, 27, 11, 30)
                , reservedDateDtos)).isTrue();

        assertThat(checkDuplicateDate(
                LocalDateTime.of(2018, 8, 27, 11, 0)
                , LocalDateTime.of(2018, 8, 27, 13, 0)
                , reservedDateDtos)).isTrue();

        assertThat(checkDuplicateDate(
                LocalDateTime.of(2018, 8, 27, 11, 30)
                , LocalDateTime.of(2018, 8, 27, 13, 0)
                , reservedDateDtos)).isTrue();

        assertThat(checkDuplicateDate(
                LocalDateTime.of(2018, 8, 27, 12, 30)
                , LocalDateTime.of(2018, 8, 27, 13, 0)
                , reservedDateDtos)).isTrue();
    }

}