package moqim.me.facelook.domain.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostRequestDto {
    @NotBlank(message = "Post must have content")
    @Size(min=2, max = 5000, message = "Post content size must be between {min} and {max}")
    private String content;
    @NotNull(message = "Post must have a topicId")
    private Long topicId;

}
