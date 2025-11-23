package pl.joboffers.domain.loginandregister;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import pl.joboffers.domain.loginandregister.dto.UserDto;

@RequiredArgsConstructor
@Service
class UserRetriever {

    private static final String USER_NOT_FOUND = "User not found";

    private final UserRepository userRepository;

    UserDto findByUserName(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException(USER_NOT_FOUND));
        return UserMapper.mapFromUser(user);

    }


}
