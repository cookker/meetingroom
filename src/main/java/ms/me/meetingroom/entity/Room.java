package ms.me.meetingroom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString(exclude = "reservations")
@Slf4j
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
}
