package pl.joboffers.infrastructure.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.joboffers.domain.register.LoginAndRegisterFacade;
import pl.joboffers.domain.register.dto.UserDto;

import java.util.Collections;

@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final LoginAndRegisterFacade loginAndRegisterFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        UserDto user = loginAndRegisterFacade.findByUserName(username);

        return getUser(user);
    }

    private User getUser(UserDto userDto) {
        return new User(
                userDto.email(),
                userDto.password(),
                Collections.emptyList()
        );
    }
}
