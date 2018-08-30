package ms.me.meetingroom.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@ToString
@Getter
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt"},
        allowGetters = true
)
public class AbstractTimestamp {
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
