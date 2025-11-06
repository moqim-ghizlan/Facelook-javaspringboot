package moqim.me.facelook.repository;

import moqim.me.facelook.domain.entities.Emotion;
import moqim.me.facelook.domain.entities.Post;
import moqim.me.facelook.domain.entities.User;
import moqim.me.facelook.domain.enums.EmotionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Long> {
    Optional<Emotion> findByPostAndUser(Post post, User user);

    // Optional helpers you may want:
    long countByPostAndStatus(Post post, EmotionStatus status);
    void deleteByPostAndUser(Post post, User user);
}