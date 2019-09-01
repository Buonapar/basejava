package ru.javawebinar.basejava.model;

import java.util.Objects;

public class TextSection implements Section {
    private String text;

    public TextSection(String text) {
        this.text = text;
    }

    public String get() {
        return text;
    }

    @Override
    public String toString() {
        return "TextSection{" +
                "text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextSection that = (TextSection) o;
        return text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
