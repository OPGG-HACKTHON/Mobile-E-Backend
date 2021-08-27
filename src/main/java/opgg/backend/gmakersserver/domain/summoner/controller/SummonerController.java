package opgg.backend.gmakersserver.domain.summoner.controller;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.summoner.controller.response.SummonerResponse;
import opgg.backend.gmakersserver.domain.summoner.service.SummonerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SummonerController {

    private final SummonerService summonerService;

    @GetMapping("/summoner")
    @ResponseStatus(HttpStatus.OK)
    public SummonerResponse getSummoner(
            @RequestParam(value = "summonerName", required = false) String summonerName,
            @AuthenticationPrincipal Long id) {

        return summonerService.getSummoner(summonerName, id);
    }
}
