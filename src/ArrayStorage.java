import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;
    private int index = 0;

    void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    void save(Resume r) {
        if (size < storage.length) {
            if (!isExist(r.uuid)) {
                storage[size] = r;
                size++;
            } else {
                System.out.println("Resume - " + r.uuid + " уже существует!");
            }
        } else {
            System.out.println("База резюме переполнена!");
        }
    }

    Resume get(String uuid) {
        if (isExist(uuid)) {
            return storage[index];
        } else {
            message(uuid);
            return null;
        }
    }

    void delete(String uuid) {
        int count;
        if (isExist(uuid)) {
            for (count = 0; count < size; count++) {
                if (storage[count].uuid == uuid)
                    break;
            }
            for (int k = count; k < size; k++) {
                storage[k] = storage[k + 1];
            }
            size--;
        } else {
            message(uuid);
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    void update (Resume resume) {
        if (isExist(resume.uuid)) {
            storage[index] = resume;
        } else {
            message(resume.uuid);
        }
    }

    int size() {
        return size;
    }

    private boolean isExist(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid == uuid) {
                index = i;
                return true;
            }
        }
        return false;
    }

    private void message(String uuid) {
        System.out.println("Resume - " + uuid + " не найдено!");
    }
}
