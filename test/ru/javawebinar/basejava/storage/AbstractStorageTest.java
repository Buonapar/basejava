package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.ResumeTestData;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = Config.get().getStorageDir();
    private static final String UUID_1 = UUID.randomUUID().toString();
    private static final String UUID_2 = UUID.randomUUID().toString();
    private static final String UUID_3 = UUID.randomUUID().toString();
    private static final String UUID_4 = UUID.randomUUID().toString();
    protected Storage storage;
    private static final Resume resumeUuid_1;
    private static final Resume resumeUuid_2;
    private static final Resume resumeUuid_3;
    private static final Resume resumeUuid_4;

    static {
        resumeUuid_1 = ResumeTestData.fillResume(UUID_1, "Федя1");
        resumeUuid_2 = ResumeTestData.fillResume(UUID_2, "Федя2");
        resumeUuid_3 = ResumeTestData.fillResume(UUID_3, "Федя3");
        resumeUuid_4 = ResumeTestData.fillResume(UUID_4, "Федя4");
    }

    public AbstractStorageTest(Storage storage) {
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
        List<Resume> expected = Arrays.asList(resumeUuid_1, resumeUuid_2, resumeUuid_3);
        Assert.assertEquals(expected, storage.getAllSorted());
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }

}