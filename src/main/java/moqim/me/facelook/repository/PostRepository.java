package moqim.me.facelook.repository;

import moqim.me.facelook.domain.entities.Post;
import moqim.me.facelook.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByAuthor(User user);
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByAuthorInOrderByCreatedAtDesc(List<User> authors);
}
