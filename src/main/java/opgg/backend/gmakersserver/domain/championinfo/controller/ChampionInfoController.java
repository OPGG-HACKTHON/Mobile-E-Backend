package opgg.backend.gmakersserver.domain.championinfo.controller;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.championinfo.service.ChampionInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChampionInfoController {

    private final ChampionInfoService championInfoService;

    @GetMapping("/champion-info")
    @ResponseStatus(HttpStatus.OK)
    public Map<Integer, String> getChampionInfo() {
        return championInfoService.getChampionInfo();
    }
}
