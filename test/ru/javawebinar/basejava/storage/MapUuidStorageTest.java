package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;

public class MapUuidStorageTest extends AbstractStorageTest{

    public MapUuidStorageTest() {
        super(new MapUuidStorage());
    }

    @Override
    @Test
    public void getAll() {
        Assert.assertEquals(3, storage.size());
    }
}