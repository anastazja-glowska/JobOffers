package pl.joboffers.domain.register;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class User {

    private Long id;

    private String email;

    private String password;

    User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
