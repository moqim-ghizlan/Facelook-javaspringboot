package moqim.me.facelook.domain.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "Username can not be empty")
    @Size(min=6, max=30, message = "Username must be between {min} and {max} characters")
    private String username;

    @NotBlank(message = "Password can not be empty")
    @Size(min=8, max=50, message = "Password must be between {min} and {max} characters")
    private String password;

}
