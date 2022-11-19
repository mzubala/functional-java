package pl.com.bottega.functional.accounts;

import lombok.extern.java.Log;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@Log
public class DbExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        cleanDB(extensionContext);
    }

    private void cleanDB(ExtensionContext extensionContext) throws SQLException {
        var context = SpringExtension.getApplicationContext(extensionContext);
        var connection = context.getBean(JdbcTemplate.class).getDataSource().getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            connection.prepareStatement("DELETE FROM \"" + tableName + "\"").executeUpdate();
        }
    }
}
