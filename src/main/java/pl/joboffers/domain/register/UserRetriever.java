package pl.joboffers.domain.register;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.joboffers.domain.register.dto.UserDto;

import java.util.List;

@RequiredArgsConstructor
@Service
class UserRetriever {

    private final UserRepository userRepository;

    UserDto findByUserName(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Username not found"));
        return UserMapper.mapFromUser(user);

    }


}
