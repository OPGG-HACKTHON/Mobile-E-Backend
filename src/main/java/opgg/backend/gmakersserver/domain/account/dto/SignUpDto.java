package opgg.backend.gmakersserver.domain.account.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDto {
    private String loginId;
    private String password;
    private String passwordRe;
}
