package ru.javawebinar.basejava.model;

import java.util.Objects;

public class TextSection implements Section {
    private static final long serialVersionUID = 1L;
    private String content;

    public TextSection(String content) {
        Objects.requireNonNull(content, "content must not be null");
        this.content = content;
    }

    public String get() {
        return content;
    }

    @Override
    public String toString() {
        return "TextSection{" +
                "content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextSection that = (TextSection) o;
        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
