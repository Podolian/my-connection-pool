package com.andersen;

import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PooledDataSource extends PGSimpleDataSource {
    private static final int POOL_SIZE = 10;

    private Queue<Connection> connectionPool;

    @SneakyThrows
    public PooledDataSource(String url, String userName, String password) {
        super();
        setURL(url);
        setUser(userName);
        setPassword(password);
        initConnectionPool();
    }

    @Override
    public Connection getConnection() {
        return connectionPool.poll();
    }

    private void initConnectionPool() throws SQLException {
        this.connectionPool = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < POOL_SIZE; i++) {
            Connection physicalConnection = super.getConnection();
            Connection connectionProxy = new ConnectionProxy(physicalConnection, connectionPool);
            connectionPool.add(connectionProxy);
        }
    }
}
