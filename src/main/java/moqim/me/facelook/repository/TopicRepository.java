package moqim.me.facelook.repository;

import moqim.me.facelook.domain.entities.Topic;
import moqim.me.facelook.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic,Long> {
    List<Topic> findByNameContaining(String name);
    List<Topic> findByCreator(User user);
}
