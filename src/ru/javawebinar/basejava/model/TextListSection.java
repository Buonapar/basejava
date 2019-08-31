package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextListSection implements Section<List<String>> {
    private List<String> descriptions = new ArrayList<>();

    public TextListSection(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public List<String> get() {
        return descriptions;
    }

    @Override
    public String toString() {
        return "TextListSection{" +
                "descriptions=" + descriptions +
                '}';
    }
//    public void addCompany(String text) {
//        if (text != null) {
//            descriptions.add(text);
//        }
//    }
}
