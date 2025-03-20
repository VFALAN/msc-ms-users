package base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
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
                        .withExposedService("msc-registry-server", 8761)
                        .withExposedService("rabbitmq", DEFAULT_PORT_RABBIT_MQ_CLIENT)
                        .withExposedService("rabbitmq", DEFAULT_PORT_RABBIT_MQ_API)
                        .withExposedService("msc-api-gateway", 8079)
                        .withExposedService("msc-auth", 8180)
                        .waitingFor("rabbitmq", Wait.forHttp("/").forPort(DEFAULT_PORT_RABBIT_MQ_API))
                        .withExposedService("loki", DEFAULT_LOKI_PORT)
                        .withPull(true);
        enviroment.withLogConsumer("msc-auth", new Slf4jLogConsumer(LoggerFactory.getLogger("msc")));
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
        final var eurekaServerHost = enviroment.getServiceHost("msc-registry-server", 8761);
        final var eurekaServerPort = enviroment.getServicePort("msc-registry-server", 8761);
        log.debug("@configuration -> eureka server host: {} port: {}", eurekaServerHost, eurekaServerPort);
        log.info("@configuration -> loki connection lokiUrl :{}", lokiUrl);
        //msc.services.authentication.url=http://localhost:7180/authentication/api
        final var apiGatewayHost = enviroment.getServiceHost("msc-api-gateway", 8079);
        final var apiGatewayPort = enviroment.getServicePort("msc-api-gateway", 8079);

        //spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
        //eureka.client.service-url.defaultZone: http://localhost:8761/eureka/
        registry.add("eureka.client.service-url.defaultZone", () -> "http://" + eurekaServerHost + ":" + eurekaServerPort + "/eureka/");
        registry.add("msc.services.authentication.url", () -> "http://" + apiGatewayHost + ":" + apiGatewayPort + "/authentication/api");
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
        enviroment.withLogConsumer("msc-auth", new Slf4jLogConsumer(LoggerFactory.getLogger("msc")));
        final var mOptionalService = enviroment.getContainerByServiceName("msc-auth");
        if (mOptionalService.isPresent()) {
            final var service = (ContainerState) mOptionalService.get();
            final var authLogs = service.getLogs();
            log.info(authLogs);
        }
    }

}
