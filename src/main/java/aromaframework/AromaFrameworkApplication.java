package aromaframework;

import aromaframework.config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by diego on 06/06/17.
 */
@EnableConfigurationProperties(ApplicationProperties.class)
@ComponentScan
@EnableAutoConfiguration
public class AromaFrameworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(AromaFrameworkApplication.class, args);
    }

}
