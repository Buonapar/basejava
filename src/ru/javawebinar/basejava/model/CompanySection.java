package ru.javawebinar.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class CompanySection extends Section {
    private static final long serialVersionUID = 1L;
    private List<Company> descriptions;

    public CompanySection() {
    }

    public CompanySection(Company... companies) {
        this(Arrays.asList(companies));
    }

    public CompanySection(List<Company> descriptions) {
        Objects.requireNonNull(descriptions, "descriptions must not be null");
        this.descriptions = descriptions;
    }

    public List<Company> get() {
        return descriptions;
    }

    @Override
    public String toPrintHtml() {
        StringBuilder stringBuilder = new StringBuilder();
        Collections.sort(descriptions);
        for (Company company : get()) {
            stringBuilder.append(company.toPrintHtml());
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "CompanySection{" +
                "descriptions=" + descriptions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanySection that = (CompanySection) o;
        return descriptions.equals(that.descriptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriptions);
    }
}
