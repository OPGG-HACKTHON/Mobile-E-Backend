package opgg.backend.gmakersserver.application.config;

import static com.merakianalytics.orianna.types.common.Platform.*;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;

@Configuration
public class OriannaConfig {

	@Value("${riot.api.key}")
	public String riotAPIKey;

	@PostConstruct
	public void setApiKey() {
		Orianna.loadConfiguration("static/orianna-config.json");
		Orianna.setRiotAPIKey(riotAPIKey);
		Orianna.setDefaultPlatform(KOREA);
		Orianna.setDefaultRegion(Region.KOREA);

	}

}
