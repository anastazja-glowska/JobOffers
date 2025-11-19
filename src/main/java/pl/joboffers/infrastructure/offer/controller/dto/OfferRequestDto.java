package pl.joboffers.infrastructure.offer.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OfferRequestDto(
        @NotNull(message = "{title.not.null}")
        @NotEmpty(message = "{title.not.empty}")
        String title,

        @NotNull(message = "{company.not.null}")
        @NotEmpty(message = "{company.not.empty}")
        String company,

        @NotNull(message = "{salary.not.null}")
        @NotEmpty(message = "{salary.not.empty}")
        String salary,

        @NotNull(message = "{offerUrl.not.null}")
        @NotEmpty(message = "{offerUrl.not.empty}")
        String offerUrl) {
}
