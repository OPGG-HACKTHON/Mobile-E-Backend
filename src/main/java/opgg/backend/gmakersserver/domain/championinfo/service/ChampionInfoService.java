package opgg.backend.gmakersserver.domain.championinfo.service;

import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.Champions;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChampionInfoService {

    public Map<Integer, String> getChampionInfo() {
        Champions champions = Champions.get();
        Map<Integer, String> championInfo = new HashMap<>();

        for(Champion c : champions){
            championInfo.put(c.getId(), c.getName());
        }

        return championInfo;
    }

}
