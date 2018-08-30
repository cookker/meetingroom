package ms.me.meetingroom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Room extends AbstractTimestamp{
    @Id @GeneratedValue
    private Long roomId;
    @Column(nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(@NonNull Reservation reservation){
        this.reservations.add(reservation);
        reservation.setRoom(this);
    }

    public void removeReservation(@NonNull Reservation reservation){
        this.reservations.remove(reservation);
        reservation.setRoom(null);
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
