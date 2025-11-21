package pl.joboffers.domain.loginandregister;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.loginandregister.dto.UserDto;
import pl.joboffers.domain.loginandregister.dto.UserRegisterRequestDto;
import pl.joboffers.domain.loginandregister.dto.UserRegisterResponseDto;

@RequiredArgsConstructor
@Component
public class LoginAndRegisterFacade {

    private final UserAdder userAdder;
    private final UserRetriever userRetriever;

    public UserDto findByUserName(String userName){
        return userRetriever.findByUserName(userName);
    }

    public UserRegisterResponseDto register(UserRegisterRequestDto user) {
        return userAdder.register(user);
    }
}
