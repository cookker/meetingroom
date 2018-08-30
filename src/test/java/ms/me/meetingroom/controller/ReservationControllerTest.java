package ms.me.meetingroom.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import ms.me.meetingroom.common.Response;
import ms.me.meetingroom.controller.param.ReservationRequest;
import ms.me.meetingroom.entity.Room;
import ms.me.meetingroom.service.ReservationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;
    @MockBean
    ReservationService reservationService;
    HttpHeaders headers = new HttpHeaders();
    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setup(){
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @Test
    @WithMockUser
    public void 예약_조회(){
        final ResponseEntity<String> responseEntity = testRestTemplate.getForEntity("/reservations/회의실A?date=20180809", String.class);
        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @WithMockUser
    public void 예약하기() {
        ReservationRequest request = new ReservationRequest();
        request.setComment("comment");
        request.setDate("20180506");
        request.setMemberName("member");
        request.setRoomName("room");
        request.setTime("1100~1300");

        HttpEntity<ReservationRequest> entity = new HttpEntity<>(request, headers);
        final ResponseEntity<String> responseEntity = testRestTemplate.exchange("/reservation",
                HttpMethod.POST,
                entity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains("SUCCESS");
    }


}