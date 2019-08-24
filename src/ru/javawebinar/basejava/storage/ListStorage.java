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

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    protected int getIndex(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void storageSave(Resume resume, int index) {
        storage.add(resume);
    }

    @Override
    protected Resume storageGet(String uuid, int index) {
        return storage.get(index);
    }

    @Override
    protected void storageDelete(String uuid, int index) {
        storage.remove(index);
    }

    @Override
    protected void storageUpdate(Resume resume, int index) {
        storage.set(index, resume);
    }
}
