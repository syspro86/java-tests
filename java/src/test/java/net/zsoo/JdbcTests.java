package net.zsoo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JdbcTests {

    @BeforeEach
    public void setUp() throws Exception {
        try (Connection conn = JdbcTestUtil.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS table_main");
            stmt.execute("""
                    CREATE TABLE table_main (
                    column1 varchar NOT NULL,
                    column2 varchar NOT NULL,
                    column3 varchar NULL,
                    CONSTRAINT table_main_pk PRIMARY KEY (column1, column2)
                    )
                    """);
        }
    }

    @AfterAll
    public static void clean() throws Exception {
        try (Connection conn = JdbcTestUtil.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS table_main");
        }
    }

    @Test
    public void testDuplicatePrimaryKeyViolation() throws Exception {
        assertThrows(org.postgresql.util.PSQLException.class, () -> {
            try (Connection conn = JdbcTestUtil.getConnection()) {
                conn.setAutoCommit(false);

                try (PreparedStatement pstmt = conn
                        .prepareStatement("INSERT INTO table_main (column1, column2, column3) VALUES (?, ?, ?)")) {
                    pstmt.setString(1, "pk1");
                    pstmt.setString(2, "pk2");
                    pstmt.setString(3, "pk3");
                    pstmt.executeUpdate();

                    pstmt.setString(1, "pk1");
                    pstmt.setString(2, "pk2");
                    pstmt.setString(3, "pk4");
                    pstmt.executeUpdate();
                }
            }
        });
    }

    @Test
    public void testBatchInsert() throws Exception {
        try (Connection conn = JdbcTestUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO table_main (column1, column2, column3) VALUES (?, ?, ?)")) {
                pstmt.setString(1, "pk1");
                pstmt.setString(2, "pk2");
                pstmt.setString(3, "pk3");
                pstmt.addBatch();

                pstmt.setString(1, "pk1");
                pstmt.setString(2, "pk3");
                pstmt.setString(3, "pk4");
                pstmt.addBatch();

                pstmt.executeBatch();
            }

            try (PreparedStatement pstmt = conn
                    .prepareStatement("SELECT COUNT(1) FROM table_main");
                    ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(2, rs.getInt(1));
            }
        }
    }

    @Test
    public void testRollbackDuringBatchInsert() throws Exception {
        try (Connection conn = JdbcTestUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO table_main (column1, column2, column3) VALUES (?, ?, ?)")) {
                pstmt.setString(1, "pk1");
                pstmt.setString(2, "pk2");
                pstmt.setString(3, "pk3");
                pstmt.addBatch();

                conn.rollback();

                pstmt.setString(1, "pk1");
                pstmt.setString(2, "pk3");
                pstmt.setString(3, "pk4");
                pstmt.addBatch();

                pstmt.executeBatch();
            }

            try (PreparedStatement pstmt = conn
                    .prepareStatement("SELECT COUNT(1) FROM table_main");
                    ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(2, rs.getInt(1));
            }
        }
    }

    @Test
    public void testClearBatch() throws Exception {
        try (Connection conn = JdbcTestUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO table_main (column1, column2, column3) VALUES (?, ?, ?)")) {
                pstmt.setString(1, "pk1");
                pstmt.setString(2, "pk2");
                pstmt.setString(3, "pk3");
                pstmt.addBatch();

                pstmt.clearBatch();
                conn.rollback();

                pstmt.setString(1, "pk1");
                pstmt.setString(2, "pk3");
                pstmt.setString(3, "pk4");
                pstmt.addBatch();

                pstmt.executeBatch();
            }

            try (PreparedStatement pstmt = conn
                    .prepareStatement("SELECT COUNT(1) FROM table_main");
                    ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(1, rs.getInt(1));
            }
        }
    }

    @Test
    public void testEmptyExecuteBatch() throws Exception {
        try (Connection conn = JdbcTestUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO table_main (column1, column2, column3) VALUES (?, ?, ?)")) {
                pstmt.executeBatch();

                pstmt.setString(1, "pk1");
                pstmt.setString(2, "pk2");
                pstmt.setString(3, "pk3");
                pstmt.addBatch();

                pstmt.clearBatch();
                pstmt.executeBatch();
            }

            try (PreparedStatement pstmt = conn
                    .prepareStatement("SELECT COUNT(1) FROM table_main");
                    ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(0, rs.getInt(1));
            }
        }
    }

    @Test
    public void testQueryTimeout() throws Exception {
        try (Connection conn = JdbcTestUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO table_main (column1, column2, column3) VALUES (?, ?, ?)")) {
                for (int i = 0; i < 100; i++) {
                    pstmt.setString(1, "pk" + i);
                    pstmt.setString(2, "pk" + i);
                    pstmt.setString(3, "pk" + i);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            try (PreparedStatement pstmt = conn
                    .prepareStatement("SELECT * FROM table_main");
                    ResultSet rs = pstmt.executeQuery()) {
                pstmt.setQueryTimeout(1);
                // rs.next 도 queryTimeout에 포함되는가? 안됨
                while (rs.next()) {
                    rs.getString(1);
                    rs.getString(2);
                    rs.getString(3);
                    Thread.sleep(100);
                }
            }
        }
    }
}
