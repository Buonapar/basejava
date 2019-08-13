import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10_000];
    private int size = 0;
    private int index = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume resume) {
        if (size < storage.length) {
            if (!isExist(resume.getUuid())) {
                storage[size] = resume;
                size++;
            } else {
                System.out.println("Resume - " + resume.getUuid() + " уже существует!");
            }
        } else {
            System.out.println("База резюме переполнена!");
        }
    }

    public Resume get(String uuid) {
        if (isExist(uuid)) {
            return storage[index];
        } else {
            reportError(uuid);
            return null;
        }
    }

    public void delete(String uuid) {
        if (isExist(uuid)) {
            System.arraycopy(new Resume[]{storage[size - 1]}, 0, storage, index, 1);
            size--;
        } else {
            reportError(uuid);
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public void update(Resume resume) {
        if (isExist(resume.getUuid())) {
            storage[index] = resume;
        } else {
            reportError(resume.getUuid());
        }
    }

    public int size() {
        return size;
    }

    private boolean isExist(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                index = i;
                return true;
            }
        }
        return false;
    }

    private void reportError(String uuid) {
        System.out.println("Resume - " + uuid + " не найдено!");
    }
}
