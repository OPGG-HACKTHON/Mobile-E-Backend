package opgg.backend.gmakersserver;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.ulisesbocchio.jasyptspringboot.properties.JasyptEncryptorConfigurationProperties;

@SpringBootTest
class GMakersServerApplicationTests {

	@Value("${jasypt.encryptor.password}")
	private String jasyptEncryptorPassword;

	@Test
	public void contextLoads() throws Exception {
		JasyptEncryptorConfigurationProperties properties = new JasyptEncryptorConfigurationProperties();
		SimpleStringPBEConfig simpleStringPBEConfig = new SimpleStringPBEConfig();
		simpleStringPBEConfig.setPassword(jasyptEncryptorPassword);
		simpleStringPBEConfig.setAlgorithm(properties.getAlgorithm());
		simpleStringPBEConfig.setKeyObtentionIterations(properties.getKeyObtentionIterations());
		simpleStringPBEConfig.setPoolSize(properties.getPoolSize());
		simpleStringPBEConfig.setSaltGeneratorClassName(properties.getSaltGeneratorClassname());
		simpleStringPBEConfig.setIvGeneratorClassName(properties.getIvGeneratorClassname());
		simpleStringPBEConfig.setStringOutputType(properties.getStringOutputType());
		PooledPBEStringEncryptor pooledPBEStringEncryptor = new PooledPBEStringEncryptor();
		pooledPBEStringEncryptor.setConfig(simpleStringPBEConfig);
	}


}
