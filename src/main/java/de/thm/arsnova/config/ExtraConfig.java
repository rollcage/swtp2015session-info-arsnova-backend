package de.thm.arsnova.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import de.thm.arsnova.connector.client.ConnectorClient;
import de.thm.arsnova.connector.client.ConnectorClientImpl;

@Configuration
public class ExtraConfig {

	@Autowired
	private Environment env;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		configurer.setLocations(new Resource[] {
				new ClassPathResource("arsnova.properties.example"),
				new FileSystemResource("file:///etc/arsnova/arsnova.properties"),
		});
		configurer.setIgnoreResourceNotFound(true);
		configurer.setIgnoreUnresolvablePlaceholders(false);
		return configurer;
	}

	@Bean(name = "connectorClient")
	public ConnectorClient connectorClient() {
		if (! "true".equals(env.getProperty("connector.enable"))) {
			return null;
		}

		ConnectorClientImpl connectorClient = new ConnectorClientImpl();
		connectorClient.setServiceLocation(env.getProperty("connector.uri"));
		connectorClient.setUsername(env.getProperty("connector.username"));
		connectorClient.setPassword(env.getProperty("connector.password"));
		return connectorClient;
	}
}