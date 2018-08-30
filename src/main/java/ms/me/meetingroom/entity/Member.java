package ms.me.meetingroom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
