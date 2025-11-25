package pl.joboffers.domain.loginandregister;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.joboffers.domain.loginandregister.dto.UserDto;
import pl.joboffers.domain.loginandregister.dto.UserRegisterRequestDto;
import pl.joboffers.domain.loginandregister.dto.UserRegisterResponseDto;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

class LoginAndRegisterFacadeTest {


    private static final String TEST_EMAIL = "email@gmail.com";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_ID = "001";


    UserRepository userRepository = new UserRepositoryTestImpl();
    UserRetriever userRetriever = new UserRetriever(userRepository);
    UserAdder userAdder = new UserAdder(userRepository);

    LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterConfig()
            .formLoginAndRegisterFacade(userAdder, userRetriever);



    @Test
    @DisplayName("Should find user by username")
    void should_find_user_by_user_name(){
        //given
        User user = createUser();

        userRepository.save(user);

        //when
        UserDto result = loginAndRegisterFacade.findByUserName(user.getEmail());

        //then
        UserDto expectedUser = UserDto.builder()
                .id(TEST_ID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        assertAll(
                () ->  assertThat(result).isEqualTo(expectedUser),
                ()->  assertThat(result).isNotNull(),
                () -> assertThat(result.email()).isEqualTo(expectedUser.email()),
                () -> assertThat(result.password()).isEqualTo(expectedUser.password()),
                () -> assertThat(result.id()).isNotNull()

        );
    }

    @Test
    @DisplayName("Should throw exception when user with email already exists")
    void should_throw_exception_when_user_with_email_already_exists(){

        //given
        User user = createUser();

        userRepository.save(user);

        UserRegisterRequestDto build = createRegisterRequest();

        //when
        Throwable result = catchThrowable(() -> loginAndRegisterFacade.register(build));

        //then
        assertThat(result).isInstanceOf(DuplicateKeyException.class);
        assertThat(result.getMessage()).isEqualTo("User already exists!");
    }


    @Test
    @DisplayName("Should register user with correct data")
    void should_register_user_with_correct_data(){
        //given

        UserRegisterRequestDto build = createRegisterRequest();

        //when
        UserRegisterResponseDto result = loginAndRegisterFacade.register(build);

        //then
        UserRegisterResponseDto expectedUser = UserRegisterResponseDto.builder()
                .id(TEST_ID)
                .email(TEST_EMAIL)
                .isRegistered(true)
                .build();


        assertAll(
                () -> assertThat(result).isNotNull(),
                () ->   assertThat(result.email()).isEqualTo(expectedUser.email()),
                () -> assertThat(result.isRegistered()).isEqualTo(expectedUser.isRegistered()),
                () -> assertThat(result.id()).isNotNull()
        );
    }


    @Test
    @DisplayName("Should throw exception when user not found")
    void should_throw_exception_when_user_not_found(){

        //when
        Throwable result = catchThrowable(() -> loginAndRegisterFacade.findByUserName(TEST_EMAIL));

        //then
        assertThat(result).isInstanceOf(BadCredentialsException.class);
        assertThat(result.getMessage()).isEqualTo("User not found");
    }



    private static UserRegisterRequestDto createRegisterRequest() {
        return UserRegisterRequestDto.builder()
                .username(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

    }

    private static User createUser() {
       return new User(TEST_ID, TEST_EMAIL, TEST_PASSWORD);

    }

}