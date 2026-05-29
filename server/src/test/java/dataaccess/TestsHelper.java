package dataaccess;

import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestsHelper {

    public boolean countZero(String sql, Connection connection) throws Exception {
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            System.out.println(count);
            if (count == 0) {
                return true;
            }
        }
        return false;
    }
}
