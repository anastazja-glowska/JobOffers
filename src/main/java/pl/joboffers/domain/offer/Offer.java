package pl.joboffers.domain.offer;

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
class Offer {

    private Long id;

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
