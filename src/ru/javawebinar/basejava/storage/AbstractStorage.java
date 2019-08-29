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

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumeList = storageGetAll();
        Collections.sort(resumeList);
        return resumeList;
    }

    @Override
    public void save(Resume resume) {
        Object searchKey = retrieveNotExistedSearchKey(resume.getUuid());
        storageSave(resume, searchKey);
    }

    @Override
    public Resume get(String uuid) {
        Object searchKey = retrieveExistedSearchKey(uuid);
        return storageGet(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = retrieveExistedSearchKey(uuid);
        storageDelete(searchKey);
    }

    @Override
    public void update(Resume resume) {
        Object searchKey = retrieveExistedSearchKey(resume.getUuid());
        storageUpdate(resume, searchKey);
    }

    private Object retrieveNotExistedSearchKey(String uuid) {
        Object searchKey = searchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        } else {
            return searchKey;
        }
    }

    private Object retrieveExistedSearchKey(String uuid) {
        Object searchKey = searchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        } else {
            return searchKey;
        }
    }
}
