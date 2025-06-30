package stress;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ApiStressTestBase extends Simulation{

    protected static HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080") // 测试目标地址
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .shareConnections(); // 共享连接提高性能

    protected static ChainBuilder createTransaction =
            exec(http("Create Transaction")
                    .post("/api/transactions")
                    .body(StringBody(
                            session -> {
                                int randomAmount = 100 + new java.util.Random().nextInt(500);
                                boolean isCredit = new java.util.Random().nextBoolean();
                                return
                                        "{ " +
                                                "\"description\": \"Load Test Transaction " + System.currentTimeMillis() + "\", " +
                                                "\"amount\": " + randomAmount + ".00, " +
                                                "\"type\": \"" + (isCredit ? "CREDIT" : "DEBIT") + "\", " +
                                                "\"category\": \"LoadTest\" " +
                                                "}";
                            }
                    ))
                    .check(status().is(201))
                    .check(jsonPath("$.id").saveAs("transactionId")));

    protected static ChainBuilder getTransaction =
            exec(http("Get Transaction")
                    .get("/api/transactions/#{transactionId}")
                    .check(status().is(200)));

    protected static ChainBuilder getAllTransactions =
            exec(http("Get All Transactions")
                    .get("/api/transactions")
                    .queryParam("page", "0")
                    .queryParam("size", "20")
                    .check(status().is(200)));

    protected static ChainBuilder getTransactionsByType =
            exec(session -> {
                boolean isCredit = new java.util.Random().nextBoolean();
                return session.set("transactionType", isCredit ? "CREDIT" : "DEBIT");
            })
                    .exec(http("Get Transactions by Type")
                            .get("/api/transactions/type/#{transactionType}")
                            .check(status().is(200))
                    );
}