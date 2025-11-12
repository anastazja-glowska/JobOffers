package pl.joboffers.domain.register;

import lombok.RequiredArgsConstructor;
import pl.joboffers.domain.register.dto.UserDto;

@RequiredArgsConstructor
public class LoginAndRegisterFacade {

    private final UserAdder userAdder;
    private final UserRetriever userRetriever;

    public UserDto findByUserName(String userName){
        return userRetriever.findByUserName(userName);
    }

    public UserDto register(User user) {
        return userAdder.register(user);
    }
}
