package opgg.backend.gmakersserver.domain.account.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.entity.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank
    @Size(min = 4, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9]{4,20}$")
    private String username;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9]{8,20}$")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public Account toEntity(PasswordEncoder passwordEncoder) {
        return Account.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.ROLE_USER)
                .activated(true)
                .build();
    }

}
