package ru.javawebinar.basejava.model;

import java.util.List;

public class CompanySection implements Section<List<Company>> {
    private List<Company> descriptions;

    public CompanySection(List<Company> descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public List<Company> get() {
        return descriptions;
    }

    @Override
    public String toString() {
        return "CompanySection{" +
                "descriptions=" + descriptions +
                '}';
    }
}
