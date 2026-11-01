package io.github.dmitrybarskov.myfancypdfinvoices_springboot.service;

import io.github.dmitrybarskov.myfancypdfinvoices_springboot.dto.InvoiceDto;
import io.github.dmitrybarskov.myfancypdfinvoices_springboot.model.Invoice;
import io.github.dmitrybarskov.myfancypdfinvoices_springboot.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class InvoiceService {
  private final UserService userService;
  private final JdbcTemplate jdbcTemplate;
  private final String cdnUrl;

  public InvoiceService(
      UserService userService,
      JdbcTemplate jdbcTemplate,
      @Value("${cdn.url}") String cdnUrl
  ) {
    this.userService = userService;
    this.jdbcTemplate = jdbcTemplate;
    this.cdnUrl = cdnUrl;
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

  @Transactional
  public List<Invoice> findAll() {
    printTransactionStatus();
    return jdbcTemplate.query("SELECT * FROM invoices;", (resultSet, _rowNum) -> {
      return newInvoiceFromResultSet(resultSet);
    });
  }

  @Transactional
  public Invoice create(String userId, Integer amount) {
    printTransactionStatus();
    User user = userService.findById(userId);
    if (user == null) {
      throw new IllegalStateException();
    }

    Invoice invoice = new Invoice(
        userId, amount, cdnUrl + "/pdf/sample-3pp.pdf"
    );

    // TODO real pdf creation and storing it on network server
    var keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      var statement = connection.prepareStatement(
          "INSERT INTO invoices (user_id, pdf_url, amount) VALUES (?, ?, ?);",
          Statement.RETURN_GENERATED_KEYS
      );
      statement.setString(1, invoice.userId());
      statement.setString(2, invoice.pdfUrl());
      statement.setInt(3, invoice.amount());
      return statement;
    }, keyHolder);

    return invoice.withId(keyHolder.getKeyAs(UUID.class).toString());
  }

  @Transactional
  public Invoice create(InvoiceDto invoiceDto) {
    return create(invoiceDto.userId(), invoiceDto.amount());
  }

  private Invoice newInvoiceFromResultSet(ResultSet resultSet) throws SQLException {
    return new Invoice(
        resultSet.getString("id"),
        resultSet.getString("user_id"),
        resultSet.getInt("amount"),
        resultSet.getString("pdf_url")
    );
  }

  private void printTransactionStatus() {
    IO.println(
        "Is a database transaction open? = " +
            TransactionSynchronizationManager.isActualTransactionActive()
    );
  }
}

