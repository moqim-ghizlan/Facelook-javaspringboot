package moqim.me.facelook.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Name must not be empty")
    private String name;

    @NotNull(message = "Address mail must not be empty")
    @Size(min=6, max=30, message = "Username must be between {min} and {max} characters")
    private String email;

    @NotNull(message = "Username must not be empty")
    @Size(min=6, max=30, message = "Username must be between {min} and {max} characters")
    private String username;

    @NotNull(message = "Password must not be empty")
    @Size(min=8, max=30, message = "Password must be between {min} and {max} characters")
    private String password1;

    @NotNull(message = "Password must not be empty")
    @Size(min=8, max=30, message = "Password must be between {min} and {max} characters")
    private String password2;

    @Size(max=250, message = "Bio can only have {max} characters")
    private String bio;

}
