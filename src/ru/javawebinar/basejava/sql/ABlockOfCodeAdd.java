package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.model.Resume;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ABlockOfCodeAdd {
    void add(ResultSet resultSet, Resume resume) throws SQLException;
}
