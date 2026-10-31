package io.github.dmitrybarskov.myfancypdfinvoices_springboot.service;

import io.github.dmitrybarskov.myfancypdfinvoices_springboot.dto.InvoiceDto;
import io.github.dmitrybarskov.myfancypdfinvoices_springboot.model.Invoice;
import io.github.dmitrybarskov.myfancypdfinvoices_springboot.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvoiceService {
  private UserService userService;
  private List<Invoice> invoices;
  private String cdnUrl;

  private InvoiceService(
      UserService userService,
      CopyOnWriteArrayList<Invoice> invoices,
      @Value("${cdn.url}") String cdnUrl
  ) {
    this.userService = userService;
    this.invoices = invoices;
    this.cdnUrl = cdnUrl;
  }

  @Autowired
  public InvoiceService(
      UserService userService,
      @Value("${cdn.url}") String cdnUrl
  ) {
    this(userService, new CopyOnWriteArrayList<>(), cdnUrl);
  }

  @PostConstruct
  public void init() {
    System.out.println("Fetching PDF Template from S3...");
    // TODO download from s3 and save locally
  }

  @PreDestroy
  public void shutdown() {
    System.out.println("Deleting downloaded templates");
    // TODO actual deletion of PDFs
  }

  public List<Invoice> findAll() {
    return invoices;
  }

  public Invoice create(String userId, Integer amount) {
    User user = userService.findById(userId);
    if (user == null) {
      throw new IllegalStateException();
    }
    // TODO real pdf creation and storing it on network server
    Invoice invoice = new Invoice(userId, amount, cdnUrl + "pdf/sample-3pp.pdf");
    invoices.add(invoice);
    return invoice;
  }

  public Invoice create(InvoiceDto invoiceDto) {
    return create(invoiceDto.userId(), invoiceDto.amount());
  }
}

