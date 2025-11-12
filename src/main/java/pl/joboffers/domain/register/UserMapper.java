package pl.joboffers.domain.register;

import pl.joboffers.domain.register.dto.UserDto;
import pl.joboffers.domain.register.dto.UserRegisterResponseDto;

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
