package pl.joboffers.domain.offer;

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
class Offer {

    @Id
    private String id;

    private String title;

    private String company;

    private String salary;

    private String offerUrl;


    Offer(String title, String company, String salary, String offerUrl) {
        this.title = title;
        this.company = company;
        this.salary = salary;
        this.offerUrl = offerUrl;
    }
}
