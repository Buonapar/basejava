package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }

    protected Object searchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        int index = (int) searchKey;
        return index >= 0;
    }

    @Override
    protected void storageSave(Resume resume, Object searchKey) {
        storage.add(resume);
    }

    @Override
    protected Resume storageGet(Object searchKey) {
        return storage.get((int)searchKey);
    }

    @Override
    protected void storageDelete(Object searchKey) {
        storage.remove((int)searchKey);
    }

    @Override
    protected void storageUpdate(Resume resume, Object searchKey) {
        storage.set((int)searchKey, resume);
    }

    @Override
    protected List<Resume> storageGetAll() {
        return storage;
    }
}
