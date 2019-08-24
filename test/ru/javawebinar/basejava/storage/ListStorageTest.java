package ru.javawebinar.basejava.storage;

import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;

public class ListStorageTest extends AbstractArrayStorageTest{

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Override
    @Test(expected = StorageException.class)
    public void saveStorageException() {
        throw new StorageException("", "");
    }
}