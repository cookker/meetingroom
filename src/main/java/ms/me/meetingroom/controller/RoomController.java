package ms.me.meetingroom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ms.me.meetingroom.common.Response;
import ms.me.meetingroom.controller.param.RoomRequest;
import ms.me.meetingroom.entity.Room;
import ms.me.meetingroom.repository.RoomRepository;
import ms.me.meetingroom.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/rooms")
    public List<Room> getRooms(){
        return roomService.getRooms();
    }

    @PostMapping("/room")
    public Response<String> createRoom(@RequestBody RoomRequest request){
        log.debug("회의실 생성 시 회의실이름:{}", request);
        roomService.createRoom(request.getRoomName());
        return Response.success("ok");
    }
}
