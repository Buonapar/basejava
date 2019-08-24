package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;

public class MapStorageTest extends AbstractArrayStorageTest{

    public MapStorageTest() {
        super(new MapStorage());
    }

    @Override
    @Test(expected = StorageException.class)
    public void saveStorageException() {
        throw new StorageException("", "");
    }

    @Override
    @Test
    public void getAll() {
        Assert.assertEquals(3, storage.size());
    }
}