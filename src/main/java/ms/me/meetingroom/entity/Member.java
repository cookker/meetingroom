package ms.me.meetingroom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
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
public class Member extends AbstractTimestamp {
    @Id @GeneratedValue
    private Long memberId;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(@NonNull Reservation reservation){
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }

        this.reservations.add(reservation);
        reservation.setMember(this);
    }

    public void removeReservation(@NonNull Reservation reservation){
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }
        this.reservations.remove(reservation);
        reservation.setMember(null);
    }
}
