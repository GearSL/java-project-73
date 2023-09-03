package hexlet.code.reporsitory;

import com.querydsl.core.types.dsl.StringPath;
import hexlet.code.model.QTask;
import hexlet.code.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, QuerydslPredicateExecutor<Task>,
        QuerydslBinderCustomizer<QTask> {
    Optional<Task> findByName(String name);

    @Override
    default void customize(QuerydslBindings bindings, QTask root) {
        bindings.bind(String.class).first(
                (StringPath path, String value) -> path.containsIgnoreCase(value));

        bindings.excluding(root.createdAt);
    }
}
