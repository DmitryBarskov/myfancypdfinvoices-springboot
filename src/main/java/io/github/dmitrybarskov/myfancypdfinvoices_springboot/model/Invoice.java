package io.github.dmitrybarskov.myfancypdfinvoices_springboot.model;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Invoice(
    String id,
    @JsonProperty("user_id") String userId,
    Integer amount,
    @JsonProperty("pdf_url") String pdfUrl
) {
  public Invoice(String userId, Integer amount, String pdfUrl) {
    this(UUID.randomUUID().toString(), userId, amount, pdfUrl);
  }
}
