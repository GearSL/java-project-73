package hexlet.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import static jakarta.persistence.TemporalType.TIMESTAMP;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    @CreationTimestamp
    @Temporal(TIMESTAMP)
    Instant createdAt;

    public TaskStatus(String name) {
        this.name = name;
    }
}
