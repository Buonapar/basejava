import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int countResume = 0;

    void clear() {
        for (int i = 0; i < countResume; i++) {
            storage[i] = null;

        }
        countResume = 0;
    }

    void save(Resume r) {
        storage[countResume] = r;
        countResume++;
    }

    Resume get(String uuid) {
        Resume result = null;
        for (int i = 0; i < countResume; i++) {
            if (storage[i].uuid == uuid) {
                result = storage[i];
                break;
            }
        }
        return result;
    }

    void delete(String uuid) {
        Resume[] result = new Resume[10000];
        int count = 0;
        for (int i = 0; i < countResume; i++) {
            if (storage[i].uuid != uuid) {
                result[count] = storage[i];
            } else {
                countResume--;
            }
            count++;
        }
        storage = result;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] result = new Resume[countResume];
        for (int i = 0; i < countResume; i++) {
            result[i] = storage[i];
        }
        return result;
    }

    int size() {
        return countResume;
    }
}
