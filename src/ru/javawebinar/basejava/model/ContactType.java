package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Номер телефона"),
    EMAIL("Электронная почта"),
    SKYPE("Логин Skype"),
    LINKEDIN("Профиль в LinkedIn");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
