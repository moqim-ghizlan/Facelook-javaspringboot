package moqim.me.facelook.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moqim.me.facelook.domain.entities.Post;
import moqim.me.facelook.domain.entities.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicDto {
    private long id;
    private String title;
    private long creatorId;
    List<Post> posts = new ArrayList<>();
    private Integer postCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
