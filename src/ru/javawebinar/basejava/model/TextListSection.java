package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class TextListSection implements Section {
    private List<String> descriptions;

    public TextListSection(List<String> descriptions) {
        Objects.requireNonNull(descriptions, "descriptions must not be null");
        this.descriptions = descriptions;
    }

    public List<String> get() {
        return descriptions;
    }

    @Override
    public String toString() {
        return "TextListSection{" +
                "descriptions=" + descriptions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextListSection that = (TextListSection) o;
        return descriptions.equals(that.descriptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriptions);
    }
}
