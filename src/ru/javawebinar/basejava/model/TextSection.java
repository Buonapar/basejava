package ru.javawebinar.basejava.model;

public class TextSection implements Section<String> {
    private String text;

    public TextSection(String text) {
        this.text = text;
    }

    @Override
    public String get() {
        return text;
    }

    @Override
    public String toString() {
        return "TextSection{" +
                "text='" + text + '\'' +
                '}';
    }
}
