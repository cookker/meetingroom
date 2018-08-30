package ms.me.meetingroom.controller;

import ms.me.meetingroom.common.Response;
import ms.me.meetingroom.controller.param.RoomRequest;
import ms.me.meetingroom.entity.Room;
import ms.me.meetingroom.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomController {
    Logger log = LoggerFactory.getLogger(RoomController.class);
    @Autowired
    private RoomService roomService;

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
