package pl.joboffers.domain.loginandregister;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.joboffers.domain.loginandregister.dto.UserRegisterRequestDto;
import pl.joboffers.domain.loginandregister.dto.UserRegisterResponseDto;

@RequiredArgsConstructor
@Service
class UserAdder {


    private final UserRepository userRepository;

    UserRegisterResponseDto register(UserRegisterRequestDto user) {


        User userToSave = new User(user.username(), user.password());

        User saved = userRepository.save(userToSave);

        return UserMapper.mapFromUserToUserRegisterDto(saved);

    }
}
