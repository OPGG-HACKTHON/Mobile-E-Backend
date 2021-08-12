package opgg.backend.gmakersserver.application.config;

import static com.merakianalytics.orianna.types.common.Platform.*;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.merakianalytics.orianna.Orianna;

@Configuration
public class OriannaConfig {

	@Value("${riot.api.key}")
	public String riotAPIKey;

	@PostConstruct
	public void setApiKey() {
		Orianna.setRiotAPIKey(riotAPIKey);
		Orianna.setDefaultPlatform(KOREA);
	}

}
