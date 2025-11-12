package pl.joboffers.domain.register;

import lombok.RequiredArgsConstructor;
import pl.joboffers.domain.register.dto.UserDto;
import pl.joboffers.domain.register.dto.UserRegisterResponseDto;

@RequiredArgsConstructor
public class LoginAndRegisterFacade {

    private final UserAdder userAdder;
    private final UserRetriever userRetriever;

    public UserDto findByUserName(String userName){
        return userRetriever.findByUserName(userName);
    }

    public UserRegisterResponseDto register(User user) {
        return userAdder.register(user);
    }
}
