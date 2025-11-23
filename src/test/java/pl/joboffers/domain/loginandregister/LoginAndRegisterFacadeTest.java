package pl.joboffers.domain.loginandregister;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.joboffers.domain.loginandregister.dto.UserDto;
import pl.joboffers.domain.loginandregister.dto.UserRegisterRequestDto;
import pl.joboffers.domain.loginandregister.dto.UserRegisterResponseDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class LoginAndRegisterFacadeTest {

    UserRepository userRepository = new UserRepositoryTestImpl();
    UserRetriever userRetriever = new UserRetriever(userRepository);
    UserAdder userAdder = new UserAdder(userRepository);

    LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterConfig()
            .formLoginAndRegisterFacade(userAdder, userRetriever);



    @Test
    @DisplayName("Should find user by username")
    void should_find_user_by_user_name(){
        //given
        User user = new User("001", "email@gmail.com", "12345678");

        userRepository.save(user);

        //when
        UserDto result = loginAndRegisterFacade.findByUserName(user.getEmail());

        //then
        UserDto expectedUser = UserDto.builder()
                .id("001")
                .email("email@gmail.com")
                .password("12345678")
                .build();

        assertThat(result).isEqualTo(expectedUser);
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(expectedUser.email());
        assertThat(result.password()).isEqualTo(expectedUser.password());
    }

    @Test
    @DisplayName("Should throw exception when user with id already exists")
    void should_throw_exception_when_user_with_id_already_exists(){

        //given
        User user = new User("001", "email@gmail.com", "12345678");

        userRepository.save(user);

        UserRegisterRequestDto build = UserRegisterRequestDto.builder()
                .username("email@gmail.com")
                .password("12345678")
                .build();

        //when
        Throwable result = catchThrowable(() -> loginAndRegisterFacade.register(build));

        //then
        assertThat(result).isInstanceOf(UserAlreadyExistsException.class);
        assertThat(result.getMessage()).isEqualTo("User already exists");
    }

    @Test
    @DisplayName("Should register user")
    void should_register_user(){
        //given

        UserRegisterRequestDto build = UserRegisterRequestDto.builder()
                .username("email@gmail.com")
                .password("12345678")
                .build();

        //when
        UserRegisterResponseDto result = loginAndRegisterFacade.register(build);

        //then

        //then
        UserRegisterResponseDto expectedUser = UserRegisterResponseDto.builder()
                .id("001")
                .email("email@gmail.com")
                .isRegistered(true)
                .build();

        assertThat(result).isEqualTo(expectedUser);
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(expectedUser.email());

    }


    @Test
    @DisplayName("Should throw exception when user not found")
    void should_throw_exception_when_user_not_found(){

        //when
        Throwable result = catchThrowable(() -> loginAndRegisterFacade.findByUserName("email@gmail.com"));

        //then
        assertThat(result).isInstanceOf(UsernameNotFoundException.class);
        assertThat(result.getMessage()).isEqualTo("User not found");
    }

}