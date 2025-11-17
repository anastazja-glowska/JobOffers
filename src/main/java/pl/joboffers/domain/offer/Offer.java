package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "offers")
class Offer {

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


    Offer(String title, String company, String salary, String offerUrl) {
        this.title = title;
        this.company = company;
        this.salary = salary;
        this.offerUrl = offerUrl;
    }
}
