package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.kafka.KafkaConsumerService;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaExtension implements SuiteExtension {

    private static KafkaConsumerService kafkaConsumerService = new KafkaConsumerService();
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void beforeAllTests(ExtensionContext extensionContext) {
        executor.execute(kafkaConsumerService);
        executor.shutdown();
    }

    @Override
    public void afterAllTests() {
        kafkaConsumerService.shutdown();
    }
}
