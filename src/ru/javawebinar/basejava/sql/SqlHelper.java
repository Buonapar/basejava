package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    public final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T connect(ABlockOfCodeSql <T> aBlockOfCodeSql, String query){

        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            return aBlockOfCodeSql.execute(preparedStatement);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
