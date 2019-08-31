package ru.javawebinar.basejava.model;

import java.time.YearMonth;
import java.util.Objects;

public class Company implements Comparable<Company>{
    private String name;
    private YearMonth startDate;
    private YearMonth endDate;
    private String position;
    private String description;

    public Company(String name, YearMonth startDate, YearMonth endDate, String position, String description) {
        this.name = name;
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
        return name.equals(company.name) &&
                startDate.equals(company.startDate) &&
                endDate.equals(company.endDate) &&
                position.equals(company.position) &&
                description.equals(company.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startDate, endDate, position, description);
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", position='" + position + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int compareTo(Company o) {
        return startDate.compareTo(o.startDate);
    }
}
