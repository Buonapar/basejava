package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Collections;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    protected abstract Object searchKey(String uuid);

    protected abstract boolean isExist(Object searchKey);

    protected abstract void storageSave(Resume resume, Object searchKey);

    protected abstract Resume storageGet(Object searchKey);

    protected abstract void storageDelete(Object searchKey);

    protected abstract void storageUpdate(Resume resume, Object searchKey);

    protected abstract List<Resume> storageGetAll();

    public List<Resume> getAllSorted() {
        List<Resume> resumeList = storageGetAll();
        Collections.sort(resumeList);
        return resumeList;
    }

    @Override
    public void save(Resume resume) {
        Object searchKey = searchKey(resume.getUuid());
        if (isExist(searchKey)) {
            throw new ExistStorageException(resume.getUuid());
        } else {
            storageSave(resume, searchKey);
        }
    }

    @Override
    public Resume get(String uuid) {
        Object searchKey = searchKey(uuid);
        if (isExist(searchKey)) {
            return storageGet(searchKey);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = searchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        } else {
            storageDelete(searchKey);
        }
    }

    @Override
    public void update(Resume resume) {
        Object searchKey = searchKey(resume.getUuid());
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(resume.getUuid());
        } else {
            storageUpdate(resume, searchKey);
        }
    }
}
