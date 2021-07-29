package opgg.backend.gmakersserver;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.ulisesbocchio.jasyptspringboot.properties.JasyptEncryptorConfigurationProperties;

@SpringBootTest
class GMakersServerApplicationTests {

	@Test
	public void contextLoads() {
	}
}


