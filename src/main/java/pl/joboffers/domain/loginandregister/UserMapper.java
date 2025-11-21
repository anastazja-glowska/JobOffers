package pl.joboffers.domain.loginandregister;

import pl.joboffers.domain.loginandregister.dto.UserDto;
import pl.joboffers.domain.loginandregister.dto.UserRegisterResponseDto;

class UserMapper {


    static UserDto mapFromUser(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getPassword());
    }

    static UserRegisterResponseDto mapFromUserToUserRegisterDto(User user) {
        return UserRegisterResponseDto.builder()
                .isRegistered(true)
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}
