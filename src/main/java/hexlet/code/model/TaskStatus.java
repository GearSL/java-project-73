package hexlet.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import static jakarta.persistence.TemporalType.TIMESTAMP;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "task_statuses")
@NoArgsConstructor
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Instant createdAt;

    public TaskStatus(String name) {
        this.name = name;
    }
}
