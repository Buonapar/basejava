package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class ResumeTestData {
    public static Resume fillResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);
        List<String> achievement = new ArrayList<>();
        achievement.add("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");
        achievement.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio," +
                " DuoSecurity, Google Authenticator, Jira, Zendesk.");
        List<String> qualifications = new ArrayList<>();
        qualifications.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualifications.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        List<Company> experience = new ArrayList<>();
        experience.add(new Company("Luxoft (Deutsche Bank)", null,
                new Company.Position(YearMonth.of(2010, Month.DECEMBER), YearMonth.of(2012, Month.FEBRUARY),
                        "Ведущий программист",
                        "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, Oracle)." +
                                " Реализация клиентской и серверной части CRM. Реализация RIA-приложения для администрирования, мониторинга" +
                                " и анализа результатов в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5.")));
        experience.add(new Company("Java Online Projects", "",
                new Company.Position(YearMonth.of(2013, Month.OCTOBER), YearMonth.of(2019, Month.AUGUST),
                        "Автор проекта.",
                        "Создание, организация и проведение Java онлайн проектов и стажировок.")));
        experience.add(new Company("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", ""
                , new Company.Position(YearMonth.of(1993, Month.SEPTEMBER), YearMonth.of(1996, Month.JULY)
                        , "Аспирантура"
                        , "программист С, С++")
                , new Company.Position(YearMonth.of(1987, Month.SEPTEMBER), YearMonth.of(1993, Month.JULY)
                        , "Инженер"
                        , "программист Fortran, C")));
        resume.addContact(ContactType.PHONE, "+7(921) 855-0482");
        resume.addContact(ContactType.EMAIL, "gkislin@yandex.ru");
        resume.addContact(ContactType.SKYPE, "grigory.kislin");
        resume.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        resume.addSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        resume.addSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));
        resume.addSection(SectionType.ACHIEVEMENT, new TextListSection(achievement));
        resume.addSection(SectionType.QUALIFICATIONS, new TextListSection(qualifications));
        resume.addSection(SectionType.EXPERIENCE, new CompanySection(experience));
        return resume;
    }
}
