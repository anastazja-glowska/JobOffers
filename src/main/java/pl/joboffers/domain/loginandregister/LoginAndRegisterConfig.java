package pl.joboffers.domain.loginandregister;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoginAndRegisterConfig {

    @Bean
    LoginAndRegisterFacade formLoginAndRegisterFacade(UserAdder userAdder, UserRetriever userRetriever) {
        return new LoginAndRegisterFacade(userAdder, userRetriever);

    }
}
