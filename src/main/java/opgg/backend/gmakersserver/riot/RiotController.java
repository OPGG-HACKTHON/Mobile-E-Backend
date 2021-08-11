package opgg.backend.gmakersserver.riot;

import opgg.backend.gmakersserver.riot.request.RiotKeyRequest;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static opgg.backend.gmakersserver.riot.RiotKey.getRiotKey;
import static opgg.backend.gmakersserver.riot.RiotKey.setRiotKey;

@RestController
public class RiotController {

    @PutMapping("/api/key")
    public String keyChange(@Valid @RequestBody RiotKeyRequest riotKeyRequest) {
        setRiotKey(riotKeyRequest.getKey());
        return getRiotKey();
    }

}
