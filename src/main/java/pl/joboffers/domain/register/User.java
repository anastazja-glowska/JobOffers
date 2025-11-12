package pl.joboffers.domain.register;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
class User {

    @Id
    private String id;

    private String email;

    private String password;

    User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
