package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString
@Document(collection = "offers")
public class Offer {

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("company")
    private String company;

    @Field("salary")
    private String salary;

    @Indexed(unique = true)
    @Field("url")
    private String offerUrl;


    public Offer(String title, String company, String salary, String offerUrl) {
        this.title = title;
        this.company = company;
        this.salary = salary;
        this.offerUrl = offerUrl;
    }
}
