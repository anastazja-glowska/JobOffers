package pl.joboffers.domain.register;

import pl.joboffers.domain.register.dto.UserDto;

class UserMapper {


    static UserDto mapFromUser(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getPassword());
    }
}
