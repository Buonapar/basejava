package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.basejava.model.SectionType.EDUCATION;
import static ru.javawebinar.basejava.model.SectionType.EXPERIENCE;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init() throws ServletException {
        super.init();
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("uuid");
        String action = req.getParameter("action");
        if (action == null) {
            req.setAttribute("resumes", storage.getAllSorted());
            req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, resp);
            return;
        }
        Resume resume = null;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                resp.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                resume = storage.get(uuid);
                break;
            case "add":
                resume = new Resume();
                for (ContactType type : ContactType.values()) {
                    resume.addContact(type, "");
                }
                for (SectionType type : SectionType.values()) {
                    Section section = null;
                    switch (type) {
                        case OBJECTIVE:
                        case PERSONAL:
                            section = new TextSection("");
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            section = new TextListSection(Collections.singletonList(""));
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            section = new CompanySection(Collections.singletonList(new Company("", "", new Company.Position(YearMonth.parse("2000-01"), YearMonth.parse("2000-01"), "", ""))));
                            break;
                    }
                    resume.addSection(type, section);
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        req.setAttribute("resume", resume);
        req.getRequestDispatcher("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uuid = req.getParameter("uuid");
        String fullName = req.getParameter("fullName");
        Resume resume;
        if (uuid.isEmpty()) {
            resume = new Resume(fullName);
        } else {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
        }
        for (ContactType type : ContactType.values()) {
            String value = req.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContact().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = req.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.addSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        resume.addSection(type, new TextListSection(value.split("\n")));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        String[] companies = req.getParameterValues(type.name());
                        String[] positions = req.getParameterValues(type.name() + "numberPosition");
                        String[] startDateList = req.getParameterValues(type.name() + "startDate");
                        String[] endDateList = req.getParameterValues(type.name() + "endDate");
                        String[] titleList = req.getParameterValues(type.name() + "title");
                        String[] descriptionList = req.getParameterValues(type.name() + "description");
                        List<Company> company = new ArrayList<>();
                        int count = 0;
                        for (int i = 0; i < companies.length; i++) {
                            String name = req.getParameterValues(type.name())[i];
                            String url = req.getParameterValues(type.name() + "url")[i];
                            List<Company.Position> positionList = new ArrayList<>();
                            for (int j = 0; j < Integer.parseInt(positions[i]); j++) {
                                String startDate = startDateList[count];
                                String endDate = endDateList[count];
                                String title = titleList[count];
                                String description = descriptionList[count];
                                positionList.add(new Company.Position(YearMonth.parse(startDate), YearMonth.parse(endDate), title, description));
                                count++;
                            }
                            company.add(new Company(new Link(name, url), positionList));
                        }
                        resume.addSection(type, new CompanySection(company));
                        break;
            }
            } else if (type != EDUCATION && type != EXPERIENCE) {
                resume.getSections().remove(type);
            }
        }
        if (uuid.isEmpty()) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        resp.sendRedirect("resume");
    }
}
