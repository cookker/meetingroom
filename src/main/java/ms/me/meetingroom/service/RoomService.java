package ms.me.meetingroom.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ms.me.meetingroom.entity.Room;
import ms.me.meetingroom.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RoomService {
    private final RoomRepository roomRepository;

    public Room createRoom(String roomName){
        Room room = new Room();
        room.setName(roomName);
        final Optional<Room> optionalRoom = roomRepository.findByName(roomName);
        if(optionalRoom.isPresent()){
            throw new RuntimeException("이미 같은 이름의 회의실이 존재합니다.");
        }

        return roomRepository.save(room);
    }

    public List<Room> getRooms(){
        final List<Room> roomList = roomRepository.findAll();
        log.debug("room list: {}", roomList);
        return roomList;
    }
}
