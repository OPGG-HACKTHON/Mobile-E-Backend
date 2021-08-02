package opgg.backend.gmakersserver.domain.account.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpDto {

    @NotBlank
    @Size(min = 4, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9]{4,20}$")
    private String loginId;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9]{8,20}$")
    private String password;

}
