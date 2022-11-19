package pl.com.bottega.functional.accounts;

import lombok.extern.java.Log;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

@Log
public class DbExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        cleanDB(extensionContext);
    }

    private void cleanDB(ExtensionContext extensionContext) throws SQLException {
        var context = SpringExtension.getApplicationContext(extensionContext);
        var template = context.getBean(R2dbcEntityTemplate.class);
        var dbClient = template.getDatabaseClient();
        dbClient.sql("SHOW TABLES")
            .map(row -> row.get("TABLE_NAME")).all().cast(String.class)
            .flatMap((tablename) -> dbClient.sql("DELETE FROM \"" + tablename + "\"").then())
            .then().block();
    }
}
