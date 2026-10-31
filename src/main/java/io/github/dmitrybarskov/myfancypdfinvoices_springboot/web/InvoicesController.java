package io.github.dmitrybarskov.myfancypdfinvoices_springboot.web;

import io.github.dmitrybarskov.myfancypdfinvoices_springboot.dto.InvoiceDto;
import io.github.dmitrybarskov.myfancypdfinvoices_springboot.model.Invoice;
import io.github.dmitrybarskov.myfancypdfinvoices_springboot.service.InvoiceService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoicesController {
  private final InvoiceService invoiceService;

  public InvoicesController(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @GetMapping("/invoices")
  public List<Invoice> index() {
    return invoiceService.findAll();
  }

  @PostMapping("/invoices")
  public Invoice create(@Valid @RequestBody InvoiceDto invoiceDto) {
    return invoiceService.create(invoiceDto);
  }
}
