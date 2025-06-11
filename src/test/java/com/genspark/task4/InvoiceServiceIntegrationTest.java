
package com.genspark.task4;

import static utils.TestUtils.businessTestFile;
import static utils.TestUtils.currentTest;
import static utils.TestUtils.yakshaAssert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class InvoiceServiceIntegrationTest {

    private static Connection conn;

    @BeforeAll
    public static void setupDatabase() throws Exception {
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE customers (customer_id INT PRIMARY KEY, name VARCHAR(255), email VARCHAR(255))");
        stmt.execute("CREATE TABLE invoices (id INT PRIMARY KEY AUTO_INCREMENT, customer_id INT, customer_email VARCHAR(255), issue_date DATE, amount DECIMAL,  FOREIGN KEY (customer_id) REFERENCES customers(customer_id))");
        stmt.execute("INSERT INTO customers (customer_id, name, email) VALUES (12345, 'John Doe','john@example.com')");
        for (int i = 0; i < 1000; i++) {
            stmt.execute("INSERT INTO invoices (customer_id, customer_email, issue_date, amount) " +
                         "VALUES (12345, 'john@example.com', DATEADD('DAY', -" + i + ", CURRENT_DATE), 100.00)");
        }
    }

    @AfterAll
    public static void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void testPerformanceOfFetchCustomerInvoices() throws Exception {
        InvoiceService service = new InvoiceService(conn);
        long start = System.nanoTime();
        ResultSet rs = service.fetchCustomerInvoices(12345, 1, 20, LocalDate.now().minusDays(365));
        long end = System.nanoTime();
        long durationMs = (end - start) / 1_000_000;

        
        int count = 0;
        while (rs.next()) count++;
        
        yakshaAssert(currentTest(), durationMs < 200 && count == 20, businessTestFile);
        
    }
}
