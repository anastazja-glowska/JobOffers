package pl.joboffers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import pl.joboffers.infrastructure.offer.http.JobOffersRestTemplateTimeoutConfig;

@SpringBootApplication
@EnableConfigurationProperties(JobOffersRestTemplateTimeoutConfig.class)
public class JobOffersApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobOffersApplication.class, args);
    }

}
