package pl.joboffers.domain.register;

import lombok.RequiredArgsConstructor;
import pl.joboffers.domain.register.dto.UserDto;

import java.util.List;

@RequiredArgsConstructor
class UserRetriever {

    private final UserRepository userRepository;

    UserDto findByUserName(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserMapper.mapFromUser(user);

    }


}
