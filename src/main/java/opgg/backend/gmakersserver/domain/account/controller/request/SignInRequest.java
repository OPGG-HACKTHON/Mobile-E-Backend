package opgg.backend.gmakersserver.domain.account.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    @NotBlank
    @Size(min = 4, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9]{4,20}$")
    private String username;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9]{8,20}$")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}
