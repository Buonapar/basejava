package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.ABlockOfCodeSql;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private SqlHelper sqlHelper;

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
            insertContact(connection, resume);
            insertSection(connection, resume);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(connection -> {
            Resume resume;
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM resume r where r.uuid=?")) {
                preparedStatement.setString(1, uuid);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, resultSet.getString("full_name"));
            }

            tableSelectByUuid(connection, "contact", resume, this::addContact);
            tableSelectByUuid(connection, "section", resume, this::addSection);
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.connect(preparedStatement -> {
            preparedStatement.setString(1, uuid);
            if (preparedStatement.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        }, "DELETE FROM resume WHERE uuid = ?");
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(connection -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();

            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM resume")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String uuid = resultSet.getString("uuid");
                    String fullName = resultSet.getString("full_name");
                    resumes.put(uuid, new Resume(uuid, fullName));
                }
            }

            tableSelect(connection, "contact", resumes, this::addContact);
            tableSelect(connection, "section", resumes, this::addSection);
            return new ArrayList<>(resumes.values());
        });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(connection -> {
            String uuid = resume.getUuid();
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                preparedStatement.setString(1, resume.getFullName());
                preparedStatement.setString(2, uuid);
                if (preparedStatement.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
            }

            tableDelete(connection, "contact", uuid);
            tableDelete(connection, "section", uuid);
            insertContact(connection, resume);
            insertSection(connection, resume);
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

    private void insertContact(Connection connection, Resume resume) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO contact(resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> entry : resume.getContact().entrySet()) {
                preparedStatement.setString(1, resume.getUuid());
                preparedStatement.setString(2, entry.getKey().name());
                preparedStatement.setString(3, entry.getValue());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    private void insertSection(Connection connection, Resume resume) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO section(resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> entry : resume.getSection().entrySet()) {
                preparedStatement.setString(1, resume.getUuid());
                SectionType sectionType = entry.getKey();
                preparedStatement.setString(2, sectionType.name());
                Section section = entry.getValue();
                String value = "";
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        value = ((TextSection) section).get();
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        value = String.join("\n", ((TextListSection) section).get());
                        break;
                }
                preparedStatement.setString(3, value);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    private void addContact(ResultSet resultSet, Resume resume) throws SQLException {
        String type = resultSet.getString("type");
        if (type != null) {
            resume.addContact(ContactType.valueOf(type), resultSet.getString("value"));
        }
    }

    private void addSection(ResultSet resultSet, Resume resume) throws SQLException {
        String type = resultSet.getString("type");
        String value = resultSet.getString("value");
        Section section = null;

        if (type != null) {
            SectionType sectionType = SectionType.valueOf(type);
            switch (sectionType) {
                case OBJECTIVE:
                case PERSONAL:
                    section = new TextSection(value);
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    section = new TextListSection(value.split("\n"));
                    break;
            }
            resume.addSection(sectionType, section);
        }
    }

    private void tableDelete(Connection connection, String tableName, String uuid) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE resume_uuid = ?")) {
            preparedStatement.setString(1, uuid);
            preparedStatement.execute();
        }
    }

    private void tableSelect(Connection connection, String tableName, Map<String, Resume> resumes, ABlockOfCodeAdd block) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                block.add(resultSet, resumes.get(resultSet.getString("resume_uuid")));
            }
        }
    }

    private void tableSelectByUuid(Connection connection, String tableName, Resume resume, ABlockOfCodeAdd block) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE resume_uuid=?")) {
            preparedStatement.setString(1, resume.getUuid());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                block.add(resultSet, resume);
            }
        }
    }

    private interface ABlockOfCodeAdd {
        void add(ResultSet resultSet, Resume resume) throws SQLException;
    }
}
