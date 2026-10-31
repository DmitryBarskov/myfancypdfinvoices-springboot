package io.github.dmitrybarskov.myfancypdfinvoices_springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record InvoiceDto(
    @JsonProperty("user_id") @NotBlank String userId,
    @Min(1) @Max(99) Integer amount
) {
}
