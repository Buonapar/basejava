package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class Company {
    private final Link homepage;
    private final List<Position> positions;

    public Company(String name, String url, List<Position> positions) {
        this.homepage = new Link(name, url);
        this.positions = positions;
    }

    public Link getHomepage() {
        return homepage;
    }

    public List<Position> getPositions() {
        return positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return homepage.equals(company.homepage) &&
                positions.equals(company.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homepage, positions);
    }

    @Override
    public String toString() {
        return "Company{" +
                "homepage=" + homepage +
                ", positions=" + positions +
                '}';
    }
}
