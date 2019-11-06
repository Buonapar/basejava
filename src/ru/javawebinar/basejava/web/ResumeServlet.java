package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResumeServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Storage storage = Config.get().getStorage();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String name = req.getParameter("name");
        resp.getWriter().write("<table border=\"1\" align=\"center\">\n" +
                                    "<caption>Hello Resumes</caption>\n" +
                                    "<tr align=\"center\">\n" +
                                     "<td>UUID</td>\n" +
                                     "<td>Name</td>\n" +
                                   "</tr>\n");
        for (Resume resume : storage.getAllSorted()) {
            resp.getWriter().write("<tr align=\"center\">\n" +
                                     "<td>" + resume.getUuid() + "</td>" +
                                     "<td>" + resume.getFullName() + "</td>" +
                                   "</tr>\n");
        }
        resp.getWriter().write("</table>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
