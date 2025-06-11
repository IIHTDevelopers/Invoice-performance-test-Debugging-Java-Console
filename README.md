![image](https://github.com/user-attachments/assets/c2d17d8c-5e4d-488c-9688-05df650cfae5)
# Invoice (Query) Performance Test

This Maven project tests the performance of a SQL query using an embedded H2 database.


## Test Criteria

- Fetches 20 invoices using a paginated query.
- Test Passes if the query completes in under 200ms.

#### Steps to Reproduce
 
1. Compile with `mvn clean package`.
2. Run unit tests to observe failure using `mvn test`
3. Final objective is to make test case pass.
4. You can run test cases many times and refactor your code.
5. Make sure you push the code to git before the final submission using shortcut : Cntrl + Shift + B
