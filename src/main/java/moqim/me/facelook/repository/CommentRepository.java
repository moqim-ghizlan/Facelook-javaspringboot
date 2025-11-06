package moqim.me.facelook.repository;

import moqim.me.facelook.domain.entities.Comment;
import moqim.me.facelook.domain.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
