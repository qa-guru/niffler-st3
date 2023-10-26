package guru.qa.niffler.test.kafka;

import guru.qa.niffler.api.AuthServiceClient;
import guru.qa.niffler.kafka.KafkaConsumerService;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.util.FakerUtils;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class AuthKafkaTest extends BaseKafkaTest {

    private AuthServiceClient authServiceClient = new AuthServiceClient();


    @AllureId("23536")
    @Test
    void authShouldProduceMessageAfterRegistration() throws IOException {
        final String username = FakerUtils.generateRandomUsername();
        final String password = FakerUtils.generateRandomPassword();

        int code = authServiceClient.doRegister(username, password);

        Assertions.assertEquals(201, code);

        UserJson message = KafkaConsumerService.getMessage(username);

        Assertions.assertNotNull(message);
    }
}
