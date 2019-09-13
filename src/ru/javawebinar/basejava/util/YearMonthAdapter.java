package ru.javawebinar.basejava.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.YearMonth;

public class YearMonthAdapter extends XmlAdapter<String, YearMonth> {
    @Override
    public YearMonth unmarshal(String date) throws Exception {
        return YearMonth.parse(date);
    }

    @Override
    public String marshal(YearMonth date) throws Exception {
        return date.toString();
    }
}
