
package com.genspark.task4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class InvoiceService {

    private final Connection conn;

    public InvoiceService(Connection conn) {
        this.conn = conn;
    }

    public ResultSet fetchCustomerInvoices(int customerId, int pageNumber, int pageSize, LocalDate fromDate) throws Exception {
         
    	/*Structure of Tables:Already created at backend
    	"CREATE TABLE customers (customer_id INT PRIMARY KEY, name VARCHAR(255), email VARCHAR(255))"
        "CREATE TABLE invoices (id INT PRIMARY KEY AUTO_INCREMENT, customer_id INT, customer_email VARCHAR(255), issue_date DATE, amount DECIMAL,  FOREIGN KEY (customer_id) REFERENCES customers(customer_id))"
        */
    	
    		
    	int offset = (pageNumber - 1) * pageSize;

         // BUG: in-efficient query 
         String sql = "SELECT inv.*, cust.name " +
                      "FROM invoices inv " +
                      "JOIN customers cust ON LOWER(cust.email) = LOWER(inv.customer_email) " +
                      "WHERE TO_CHAR(inv.issue_date, 'YYYY-MM-DD') >= '" + fromDate.toString() + "' " +
                      "AND CAST(inv.customer_id AS TEXT) LIKE '%" + customerId + "%' " +
                      "ORDER BY inv.issue_date DESC " +
                      "LIMIT " + pageSize + " OFFSET " + offset;

         PreparedStatement ps = conn.prepareStatement(sql);
         return ps.executeQuery();
    }
}
