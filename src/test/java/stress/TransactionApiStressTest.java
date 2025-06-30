package stress;

import io.gatling.javaapi.core.ScenarioBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;

public class TransactionApiStressTest extends ApiStressTestBase {

    private static ScenarioBuilder normalLoadScenario =
            scenario("Normal Load Scenario")
                    .exec(createTransaction)
                    .pause(1)  // 1秒思考时间
                    .exec(getTransaction)
                    .pause(1)
                    .exec(getAllTransactions)
                    .pause(2)
                    .exec(getTransactionsByType);

    private static ScenarioBuilder peakLoadScenario =
            scenario("Peak Load Scenario")
                    .exec(createTransaction)
                    .exec(getTransaction)
                    .exec(getAllTransactions)
                    .exec(getTransactionsByType);

    {
        setUp(
                // 100 users for 5 mins
                normalLoadScenario.injectOpen(
                        rampUsers(100).during(Duration.ofMinutes(5))),

                // 500 users for 2 mins
                peakLoadScenario.injectOpen(
                        nothingFor(Duration.ofSeconds(30)),
                        rampUsers(500).during(Duration.ofMinutes(2)))
        ).protocols(httpProtocol)
                .assertions(
                        // 99.99% should be successful
                        global().successfulRequests().percent().gt(99.99),
                        // 95% response time should be shorter than 800ms
                        global().responseTime().percentile(95).lt(800),
                        //max response time is smaller than 2s
                        global().responseTime().max().lt(2000)
                );
    }
}