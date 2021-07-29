package opgg.backend.gmakersserver.domain.account.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInDto {
    private String loginId;
    private String password;
}
