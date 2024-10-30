package base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

@SpringBootTest
@Testcontainers
@Slf4j
public abstract class BaseTestConfiguration {
    private static final int DEFAULT_PORT_MYSQL = 3306;
    private static final int DEFAULT_PORT_RABBIT_MQ_CLIENT = 5672;
    private static final int DEFAULT_PORT_RABBIT_MQ_API = 15672;
    private static final int DEFAULT_LOKI_PORT = 3100;
    static final DockerComposeContainer enviroment;

    static {
        enviroment =
                new DockerComposeContainer(
                        new File("src/test/resources/docker-compose.yaml")
                ).withExposedService("db", DEFAULT_PORT_MYSQL, Wait.forListeningPort().withStartupTimeout(Duration.ofMinutes(2)))

                        .withExposedService("rabbitmq", DEFAULT_PORT_RABBIT_MQ_CLIENT)
                        .withExposedService("rabbitmq", DEFAULT_PORT_RABBIT_MQ_API)
                        .waitingFor("rabbitmq", Wait.forHttp("/").forPort(DEFAULT_PORT_RABBIT_MQ_API))
                        .withExposedService("loki", DEFAULT_LOKI_PORT);
        enviroment.start();
    }


    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry) {
        final var mySqlUsername = "root";
        final var mySqlPassword = "admin";
        final var mySqlHost = enviroment.getServiceHost("db", DEFAULT_PORT_MYSQL);
        final var mySqlPort = enviroment.getServicePort("db", DEFAULT_PORT_MYSQL) != null ? enviroment.getServicePort("db", DEFAULT_PORT_MYSQL).toString() : "No Port Found";
        final var jdbcUrl = "jdbc:mysql://" + mySqlHost + ":" + mySqlPort + "/msc_development";
        log.info("@Configuration -> mySQL connection : mysqlHost: {}, port: {},username: {}, password: {} jdbcUrl: {}", mySqlHost, mySqlPort, mySqlUsername, mySqlPassword, jdbcUrl);
        final var rabbitHost = enviroment.getServiceHost("rabbitmq", DEFAULT_PORT_RABBIT_MQ_CLIENT);
        final var rabbitMappedPort = enviroment.getServicePort("rabbitmq", DEFAULT_PORT_RABBIT_MQ_CLIENT);
        final var rabbitUsername = "VIFA-951002";
        final var rabbitPassword = "VIFA-951002-PASS";
        log.info("@configuration -> rabbit connection mySqlHost: {} mySqlPort: {}, username: {}, password:{} .", rabbitHost, rabbitMappedPort, rabbitUsername, rabbitPassword);
        final var lokiPort = enviroment.getServicePort("loki", DEFAULT_LOKI_PORT);
        final var lokiHost = enviroment.getServiceHost("loki", DEFAULT_LOKI_PORT);
        final var lokiUrl = "http://" + lokiHost + ":" + lokiPort + "/loki/api/v1/push";
        log.info("@configuration -> loki connnection lokiUrl :{}", lokiUrl);
        //spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
        registry.add("loki.url", () -> lokiUrl);
        registry.add("spring.rabbitmq.username", () -> rabbitUsername);
        registry.add("spring.rabbitmq.password", () -> rabbitPassword);
        registry.add("spring.rabbitmq.host", () -> rabbitHost);
        registry.add("spring.rabbitmq.port", () -> rabbitMappedPort);
        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.password", () -> mySqlPassword);
        registry.add("spring.datasource.username", () -> mySqlUsername);
        registry.add("spring.datasource.driverClassName", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQL8Dialect");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.generate-ddl", () -> "false");
        registry.add("server.servlet.context-path", () -> "/ms-authentication");
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("loki.enabled", () -> "false");
        registry.add("loki.url", () -> "http://localhost:3100/loki/api/v1/push");
    }

}
