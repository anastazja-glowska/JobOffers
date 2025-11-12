package pl.joboffers.domain.register;

import lombok.RequiredArgsConstructor;
import pl.joboffers.domain.register.dto.UserDto;

@RequiredArgsConstructor
class UserAdder {


    private final UserRepository userRepository;
    UserDto register(User user) {
        User saved = userRepository.save(user);
        return UserMapper.mapFromUser(saved);

    }
}
