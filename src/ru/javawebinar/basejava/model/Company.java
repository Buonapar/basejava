package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.YearMonthAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Company implements Serializable, Comparable<Company> {
    private static final long serialVersionUID = 1L;
    private Link homepage;
    private List<Position> positions;

    public Company() {
    }

    public Company(String name, String url, Position... positions) {
        this(new Link(name, url), Arrays.asList(positions));
    }

    public Company(Link homepage, List<Position> positions) {
        this.homepage = homepage;
        this.positions = positions;
    }

    public Link getHomepage() {
        return homepage;
    }

    public List<Position> getPositions() {
        return positions;
    }

    private Position getPosition(String title) {
        Position position = null;
        for (Position p : positions) {
            if (p.title.equals(title)) {
                position = p;
            }
        }
        return position;
    }

    public String getName() {
        return homepage.getName();
    }

    private YearMonth getDate() {
        return getPositions().get(0).endDate;
    }

    String toPrintHtml() {
        StringBuilder result = new StringBuilder();
        result.append("<table cellpadding=\"8\">").
                append("<tr><td colspan=\"2\"><b>").
                append(toLink(homepage)).
                append("</b></td></tr>");
        for (Position position : getPositions()) {
            result.append(position.toPrintHtml());
        }
        result.append("</table>");
        return result.toString();
    }

    private static String toLink(Link homepage) {
        return homepage.getUrl().isEmpty() ? homepage.getName() :
                "<a href='" + homepage.getUrl() + "'>" + homepage.getName() + "</a>";
    }

    void deletePosition(String positionTitle) {
        positions.remove(getPosition(positionTitle));
    }

    void addPosition(Position position) {
        positions.add(position);
    }

    @Override
    public int compareTo(Company o) {
        return o.getDate().compareTo(this.getDate());
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

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable, Comparable<Position>{
        @XmlJavaTypeAdapter(YearMonthAdapter.class)
        private YearMonth startDate;
        @XmlJavaTypeAdapter(YearMonthAdapter.class)
        private YearMonth endDate;
        private String title;
        private String description;

        public Position() {
        }

        public Position(YearMonth startDate, String title, String description) {
            this(startDate, YearMonth.now(), title, description);
        }

        public Position(YearMonth startDate, YearMonth endDate, String title, String description) {
            Objects.requireNonNull(startDate, "startDate must not be null");
            Objects.requireNonNull(endDate, "endDate must not be null");
            Objects.requireNonNull(title, "title must not be null");
            this.startDate = startDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description == null ? "" : description;
        }

        public YearMonth getStartDate() {
            return startDate;
        }

        public YearMonth getEndDate() {
            return endDate;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        String toPrintHtml(){
            return "<tr><td width=\"130\" style=\"vertical-align: top\">" +
                    formatDate(startDate) +
                    " - " +
                    formatDate(endDate) +
                    "</td><td><b> " +
                    title +
                    "</b><br>" +
                    description +
                    "</td></tr>";
        }
        private String formatDate(YearMonth yearMonth) {
            return yearMonth.getMonth().getValue() + "/" + yearMonth.getYear();
        }

        @Override
        public int compareTo(Position o) {
            return o.endDate.compareTo(this.endDate);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return startDate.equals(position.startDate) &&
                    endDate.equals(position.endDate) &&
                    title.equals(position.title) &&
                    Objects.equals(description, position.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startDate, endDate, title, description);
        }

        @Override
        public String toString() {
            return "Position{" +
                    "startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}
