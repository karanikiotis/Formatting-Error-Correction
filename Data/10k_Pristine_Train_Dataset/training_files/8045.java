package com.app.mvc.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 动态数据源
 * Created by jimin on 16/03/22.
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    protected Object determineCurrentLookupKey() {
        return DataSourceKeyHolder.getCurrentKey();
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = super.getConnection();
        if (DataSourceKeyHolder.getCurrentKey() != null) {
            log.info("Datasource route to {}, key={}", connection, DataSourceKeyHolder.getCurrentKey());
        }
        return connection;
    }
}