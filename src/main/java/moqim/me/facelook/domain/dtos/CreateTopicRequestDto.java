package moqim.me.facelook.domain.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTopicRequestDto {
    @NotBlank(message = "Post must have a title")
    @Size(min=2, max = 50, message = "Post's title size must be between {min} and {max}")
    private String title;
}
