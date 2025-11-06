package moqim.me.facelook.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentRequestDto {
    @NotBlank(message = "Comment content is required")
    @Size(min = 1, max = 1000, message = "Comment length must be between {min} and {max}")
    private String content;
}
