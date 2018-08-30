package ms.me.meetingroom.controller;

import ms.me.meetingroom.entity.Room;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    public void 회의실_조회(){
        final ParameterizedTypeReference<List<Room>> typeReference = new ParameterizedTypeReference<List<Room>>() {};
        final ResponseEntity<List<Room>> responseEntity = testRestTemplate.exchange("/rooms",
                HttpMethod.GET,
                null,
                typeReference);
//        System.out.println(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}