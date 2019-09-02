package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Номер телефона"),
    MOBILE("Мобильный"),
    HOME_PHONE("Домашний тел."),
    EMAIL("Электронная почта"),
    SKYPE("Логин Skype"),
    LINKEDIN("Профиль в LinkedIn"),
    GITHUB("Профиль GitHub"),
    STATCKOVERFLOW("Профиль Stackoverflow"),
    HOME_PAGE("Домашняя страница");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
