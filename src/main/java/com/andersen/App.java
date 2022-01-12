package com.andersen;

import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;

public class App {
    private static DataSource dataSource;

    @SneakyThrows
    public static void main(String[] args) {
        dataSource = new PooledDataSource(
                "jdbc:postgresql://localhost:5432/postgres",
                "dp",
                "andersen"
        );

        final long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            try (Connection connection = dataSource.getConnection()) {
                // noop
            }
        }
        final long finish = System.nanoTime();
        System.out.println("It took " + (finish - start) / 1_000_000 + " milliseconds");
    }
}
