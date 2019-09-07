package ru.javawebinar.basejava.storage;

import static org.junit.Assert.*;
import static ru.javawebinar.basejava.storage.AbstractStorageTest.STORAGE_DIR;

public class ObjectStreamStorageTest extends AbstractStorageTest {
    public ObjectStreamStorageTest() {
        super(new ObjectStreamStorage(STORAGE_DIR));
    }
}