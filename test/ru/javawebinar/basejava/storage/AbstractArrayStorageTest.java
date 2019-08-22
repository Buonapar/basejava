package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest {
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private Storage storage;
    private Resume resumeUuid_1 = new Resume(UUID_1);
    private Resume resumeUuid_2 = new Resume(UUID_2);
    private Resume resumeUuid_3 = new Resume(UUID_3);
    private Resume resumeUuid_4 = new Resume(UUID_4);

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(resumeUuid_1);
        storage.save(resumeUuid_2);
        storage.save(resumeUuid_3);
    }

    @Test
    public void update() {
        storage.update(resumeUuid_2);
        Assert.assertEquals(3, storage.size());
        Assert.assertEquals(resumeUuid_2, storage.get(UUID_2));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume(UUID_4));
    }

    @Test
    public void save() {
        storage.save(resumeUuid_4);
        Assert.assertEquals(4, storage.size());
        Assert.assertEquals(resumeUuid_4, storage.get(UUID_4));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(resumeUuid_2);
    }

    @Test(expected = StorageException.class)
    public void saveStorageException() {
        try {
            storage.clear();
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assert.fail("Exception not thrown StorageException");
        }
        storage.save(new Resume());

    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_2);
        Assert.assertEquals(2, storage.size());
        storage.get(UUID_2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("uuid4");
    }

    @Test
    public void get() {
        Assert.assertEquals(resumeUuid_1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void getAll() {
        Resume[] expected = {resumeUuid_1, resumeUuid_2, resumeUuid_3};
        Assert.assertArrayEquals(expected, storage.getAll());
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }
}