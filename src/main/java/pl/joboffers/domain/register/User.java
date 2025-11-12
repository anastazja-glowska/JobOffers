package pl.joboffers.domain.register;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.testcontainers.shaded.org.checkerframework.common.aliasing.qual.Unique;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
class User {

    @Id
    private String id;

    @Unique
    private String email;

    @NotNull
    private String password;

    User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
