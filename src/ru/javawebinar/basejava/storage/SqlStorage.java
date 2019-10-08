package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ABlockOfCodeSql;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.connect((ABlockOfCodeSql<Object>) PreparedStatement::execute, "DELETE from resume", null);
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.connect(preparedStatement -> {
            preparedStatement.setString(1, resume.getUuid());
            preparedStatement.setString(2, resume.getFullName());
            preparedStatement.execute();
            return null;
        }, "INSERT INTO resume(uuid, full_name) VALUES (?, ?)", resume.getUuid());
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.connect(preparedStatement -> {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, resultSet.getString("full_name"));
        }, "SELECT * FROM resume r where r.uuid =?", uuid);
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.connect(preparedStatement -> {
            preparedStatement.setString(1, uuid);
            if (preparedStatement.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        }, "DELETE FROM resume WHERE uuid = ?", uuid);
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.connect(preparedStatement -> {
            List<Resume> resumes = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                resumes.add(new Resume(resultSet.getString("uuid"), resultSet.getString("full_name")));
            }
            return resumes;
        }, "SELECT * FROM resume", null);
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.connect(preparedStatement -> {
            preparedStatement.setString(1, resume.getFullName());
            preparedStatement.setString(2, resume.getUuid());
            if (preparedStatement.executeUpdate() == 0) {
                throw new NotExistStorageException(resume.getUuid());
            }
            return null;
        }, "UPDATE resume SET full_name = ? WHERE uuid = ?", resume.getUuid());
    }

    @Override
    public int size() {
        return sqlHelper.connect(preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }, "SELECT COUNT(*) FROM resume", null);
    }
}
