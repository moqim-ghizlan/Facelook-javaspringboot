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
public class UpdatePostRequestDto {

    @NotNull(message = "Post ID is not valid")
    private long id;

    @NotBlank(message = "Post content must not be blank")
    @Size(min=2, max = 5000, message = "Post content size must be between {min} and {max}")
    private String content;
}
