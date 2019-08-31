package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompanySection implements Section<List<Company>> {
//    private Company company;
    private List<Company> descriptions = new ArrayList<>();

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
//    public void addObject(Company company) {
//        if (company != null) {
//            descriptions.add(company);
//            Collections.sort(descriptions);
//        }
//    }
}
