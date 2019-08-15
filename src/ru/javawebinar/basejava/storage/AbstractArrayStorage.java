package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    protected abstract void insertionPoint(Resume resume, int index);

    protected abstract void fill(int index);

    protected abstract int getIndex(String uuid);

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index > -1) {
            return storage[index];
        } else {
            reportError(uuid);
            return null;
        }
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index < 0) {
            reportError(resume.getUuid());
        } else {
            storage[index] = resume;
        }
    }

    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index > 0) {
            System.out.println("Resume - " + resume.getUuid() + " уже существует!");
        } else if (size >= STORAGE_LIMIT) {
            System.out.println("База резюме переполнена!");
        } else {
            insertionPoint(resume, index);
            size++;
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            reportError(uuid);
        } else {
            fill(index);
            size--;
            storage[size] = null;

        }
    }

    public int size() {
        return size;
    }

    protected void reportError(String uuid) {
        System.out.println("Resume - " + uuid + " не найдено!");
    }
}
