package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume resume1 = new Resume("uuid1", "Petr Ivanov");
        List<String> achievement = new ArrayList<>();
        achievement.add("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");
        achievement.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio," +
                " DuoSecurity, Google Authenticator, Jira, Zendesk.");
        List<String> qualifications = new ArrayList<>();
        qualifications.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualifications.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        List<Company> experience = new ArrayList<>();
        experience.add(new Company("Luxoft (Deutsche Bank)",
                YearMonth.of(2010, Month.DECEMBER), YearMonth.of(2012, Month.FEBRUARY),
                "Ведущий программист",
                "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, Oracle)." +
                            " Реализация клиентской и серверной части CRM. Реализация RIA-приложения для администрирования, мониторинга" +
                            " и анализа результатов в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5."));
        experience.add(new Company("Java Online Projects",
                YearMonth.of(2013, Month.OCTOBER), YearMonth.of(2019, Month.AUGUST),
                "Автор проекта.",
                "Создание, организация и проведение Java онлайн проектов и стажировок."));
        Collections.sort(experience);
        Collections.reverse(experience);
        resume1.addContact(ContactType.PHONE, "+7(921) 855-0482");
        resume1.addContact(ContactType.EMAIL, "gkislin@yandex.ru");
        resume1.addContact(ContactType.SKYPE, "grigory.kislin");
        resume1.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        resume1.addSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        resume1.addSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));
        resume1.addSection(SectionType.ACHIEVEMENT, new TextListSection(achievement));
        resume1.addSection(SectionType.QUALIFICATIONS, new TextListSection(qualifications));
        resume1.addSection(SectionType.EXPERIENCE, new CompanySection(experience));
        System.out.println(resume1);
    }
}
