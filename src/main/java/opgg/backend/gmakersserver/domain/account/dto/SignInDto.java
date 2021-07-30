package opgg.backend.gmakersserver.domain.account.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignInDto {

    @NotBlank
    @Length(max = 30)
    private String loginId;

    @NotBlank
    private String password;

}
