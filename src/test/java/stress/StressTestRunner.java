package stress;

import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;

public class StressTestRunner {
    public static void main(String[] args) {
        GatlingPropertiesBuilder props = new GatlingPropertiesBuilder()
                .simulationClass(TransactionApiStressTest.class.getName())
                .resultsDirectory("target/gatling-results");

        Gatling.fromMap(props.build());
    }
}