package opgg.backend.gmakersserver.riot.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RiotKeyRequest {

    @NotBlank
    private String key;

}
