package pl.joboffers.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.JobOffersApplication;
import pl.joboffers.domain.offer.RemoteOfferFetcher;
import pl.joboffers.infrastructure.offer.http.JobOffersRestTemplate;
import pl.joboffers.infrastructure.offer.http.OfferClientConfig;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest(properties = "scheduling.enabled=true")
//@Import(OfferClientConfig.class)
public class HttpOffersSchedulerTest extends BaseIntegrationTest {


//    @SpyBean
//    RemoteOfferFetcher remoteOfferFetcher;


//    @Test
//    void should_run_http_client_offers_getting_exactly_given_times(){
//        await()
//                .atMost(Duration.ofSeconds(2))
//                .untilAsserted(() -> verify(remoteOfferFetcher, times(1)).fetchOffersFromServer());
//    }
}
