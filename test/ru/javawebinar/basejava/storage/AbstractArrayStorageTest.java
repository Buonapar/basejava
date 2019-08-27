package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest{

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void saveStorageException() {
        try {
            storage.clear();
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume("Федя" + i));
            }
        } catch (StorageException e) {
            Assert.fail("Exception not thrown StorageException");
        }
        storage.save(new Resume("Федя"));

    }
}