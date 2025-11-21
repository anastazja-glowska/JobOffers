package pl.joboffers.infrastructure.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.joboffers.domain.register.LoginAndRegisterFacade;
import pl.joboffers.domain.register.dto.UserDto;

@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final LoginAndRegisterFacade loginAndRegisterFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = loginAndRegisterFacade.findByUserName(username);

        return null;
    }
}
