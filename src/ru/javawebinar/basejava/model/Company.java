package ru.javawebinar.basejava.model;

import java.time.YearMonth;
import java.util.Objects;

public class Company implements Comparable<Company>{
    private final Link homepage;
    private final YearMonth startDate;
    private final YearMonth endDate;
    private final String position;
    private final String description;

    public Company(String name, String url, YearMonth startDate, YearMonth endDate, String position, String description) {
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(endDate, "endDate must not be null");
        Objects.requireNonNull(position, "title must not be null");
        this.homepage = new Link(name, url);
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return homepage.equals(company.homepage) &&
                startDate.equals(company.startDate) &&
                endDate.equals(company.endDate) &&
                position.equals(company.position) &&
                Objects.equals(description, company.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homepage, startDate, endDate, position, description);
    }

    @Override
    public int compareTo(Company o) {
        return startDate.compareTo(o.startDate);
    }
}
