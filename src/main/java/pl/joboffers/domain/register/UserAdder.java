package pl.joboffers.domain.register;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.joboffers.domain.register.dto.UserDto;

@RequiredArgsConstructor
@Service
class UserAdder {


    private final UserRepository userRepository;
    UserDto register(User user) {
        boolean existsById = userRepository.existsById(user.getId());

        if(existsById){
            throw new UserAlreadyExistsException("User already exists");
        }

        User saved = userRepository.save(user);
        return UserMapper.mapFromUser(saved);

    }
}
