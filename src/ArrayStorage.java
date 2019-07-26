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
        for (int i = 0; i < countResume; i++) {
            if (storage[i].uuid == uuid) {
                return storage[i];

            }
        }
        return null;
    }

    void delete(String uuid) {
        int count;
        for (count = 0; count < countResume; count++) {
            if (storage[count].uuid == uuid)
                break;
                }
        for (int k = count; k < countResume; k++)
            storage[k] = storage[k + 1];

        countResume--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] resumes = new Resume[countResume];
        for (int i = 0; i < countResume; i++) {
            resumes[i] = storage[i];
        }
        return resumes;
    }

    int size() {
        return countResume;
    }
}
