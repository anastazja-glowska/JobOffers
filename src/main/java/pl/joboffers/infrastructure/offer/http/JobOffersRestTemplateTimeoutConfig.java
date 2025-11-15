package pl.joboffers.infrastructure.offer.http;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@ConfigurationProperties(prefix = "job.offers.http.client")
public record JobOffersRestTemplateTimeoutConfig(long readTimeout, long connectionTimeout) {
}
