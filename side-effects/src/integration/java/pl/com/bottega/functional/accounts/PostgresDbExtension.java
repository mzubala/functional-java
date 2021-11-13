package pl.com.bottega.functional.accounts;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresDbExtension implements BeforeAllCallback, BeforeEachCallback, ExtensionContext.Store.CloseableResource {
    private final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"));

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        startPostgresDB();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        cleanDB(extensionContext);
    }

    private void cleanDB(ExtensionContext extensionContext) {
        var context = SpringExtension.getApplicationContext(extensionContext);
        var template = context.getBean(R2dbcEntityTemplate.class);
        var dbClient = template.getDatabaseClient();
        dbClient.sql("SELECT * FROM pg_catalog.pg_tables WHERE schemaname != 'pg_catalog' AND schemaname != 'information_schema'")
            .map(row -> row.get("tablename")).all().cast(String.class)
            .flatMap((tablename) -> dbClient.sql("TRUNCATE TABLE " + tablename + " CASCADE").then())
            .then().block();
    }

    @Override
    public void close() throws Throwable {
        stopPostgresDB();
    }

    private void startPostgresDB() {
        if (!postgreSQLContainer.isRunning()) {
            postgreSQLContainer.withUsername("test");
            postgreSQLContainer.withPassword("test");
            postgreSQLContainer.withDatabaseName("accounts");
            postgreSQLContainer.start();
            System.setProperty("spring.r2dbc.url", postgreSQLContainer.getJdbcUrl().replace("jdbc", "r2dbc"));
            System.setProperty("spring.r2dbc.username", "test");
            System.setProperty("spring.r2dbc.password", "test");
        }
    }

    private void stopPostgresDB() {
        postgreSQLContainer.stop();
        postgreSQLContainer.close();
    }
}
