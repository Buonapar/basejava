package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ABlockOfCodeSql;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SqlStorage implements Storage {
    public SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.connect((ABlockOfCodeSql<Object>) PreparedStatement::execute, "DELETE from resume");
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO resume(uuid, full_name) VALUES (?, ?)")) {
                preparedStatement.setString(1, resume.getUuid());
                preparedStatement.setString(2, resume.getFullName());
                preparedStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO contact(resume_uuid, type, value) VALUES (?, ?, ?)")) {
                insertContact(preparedStatement, resume);
                preparedStatement.executeBatch();
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.connect(preparedStatement -> {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume resume = new Resume(uuid, resultSet.getString("full_name"));
            do {
                addContact(resultSet, resume);
            } while (resultSet.next());

            return resume;
        }, "        SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "       ON r.uuid = c.resume_uuid " +
                "    WHERE r.uuid =?");
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.connect(preparedStatement -> {
            deleteEntry(preparedStatement, uuid);
            return null;
        }, "DELETE FROM resume WHERE uuid = ?");
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.connect(preparedStatement -> {
            Map<String, Resume> resumes = new HashMap<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                if (resumes.containsKey(uuid)) {
                    addContact(resultSet, resumes.get(uuid));
                } else {
                    Resume resume = new Resume(uuid, resultSet.getString("full_name"));
                    addContact(resultSet, resume);
                    resumes.put(uuid, resume);
                }
            }

            List<Resume> resumesList = new ArrayList<>(resumes.values());
            Collections.sort(resumesList);
            return resumesList;
        }, "        SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "       ON r.uuid = c.resume_uuid " +
                " ORDER BY full_name, uuid");
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                preparedStatement.setString(1, resume.getFullName());
                preparedStatement.setString(2, resume.getUuid());
                if (preparedStatement.executeUpdate() == 0) {
                    throw new NotExistStorageException(resume.getUuid());
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
                deleteEntry(preparedStatement, resume.getUuid());
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO contact(resume_uuid, type, value) VALUES (?, ?, ?)")) {
                insertContact(preparedStatement, resume);
                preparedStatement.executeBatch();
            }
            return null;
        });

    }

    @Override
    public int size() {
        return sqlHelper.connect(preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }, "SELECT COUNT(*) FROM resume");
    }

    private void insertContact(PreparedStatement preparedStatement, Resume resume) throws SQLException {
        for (Map.Entry<ContactType, String> entry : resume.getContact().entrySet()) {
            preparedStatement.setString(1, resume.getUuid());
            preparedStatement.setString(2, entry.getKey().name());
            preparedStatement.setString(3, entry.getValue());
            preparedStatement.addBatch();
        }
    }

    private void deleteEntry(PreparedStatement preparedStatement, String uuid) throws SQLException {
        preparedStatement.setString(1, uuid);
        if (preparedStatement.executeUpdate() == 0) {
            throw new NotExistStorageException(uuid);
        }
    }

    private void addContact(ResultSet resultSet, Resume resume) throws SQLException {
        resume.addContact(ContactType.valueOf(resultSet.getString("type")), resultSet.getString("value"));
    }
}
