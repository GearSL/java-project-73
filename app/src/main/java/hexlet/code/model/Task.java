package hexlet.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import java.time.Instant;
import static jakarta.persistence.TemporalType.TIMESTAMP;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Length(min = 1)
    private String name;
    private String description;
    @ManyToOne
    private TaskStatus taskStatus;
    @ManyToOne
    private User author;
    @ManyToOne
    private User executor;
    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Instant createdAt;

}
