package moqim.me.facelook.domain.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTopicRequest {
    private String title;
    private long creatorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
