package opgg.backend.gmakersserver;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
public class GMakersServerApplication {
	private static final String APPLICATION =
			"spring.config.location=" +
					"classpath:/application.yaml," +
					"classpath:/application-aws-rds.yaml," +
					"classpath:/application-aws-s3.yaml"
			;

	public static void main(String[] args) {
		new SpringApplicationBuilder(GMakersServerApplication.class).properties(APPLICATION).run(args);
	}

}
