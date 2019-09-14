package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(os)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());
            Map<ContactType, String> contacts = resume.getContact();
            dataOutputStream.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dataOutputStream.writeUTF(entry.getKey().name());
                dataOutputStream.writeUTF(entry.getValue());
            }
            Map<SectionType, Section> sections = resume.getSection();
            dataOutputStream.writeInt(sections.size());
            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                dataOutputStream.writeUTF(entry.getKey().name());
                if (entry.getKey().equals(SectionType.OBJECTIVE) || entry.getKey().equals(SectionType.PERSONAL)) {
                    dataOutputStream.writeUTF(((TextSection) entry.getValue()).get());
                } else if (entry.getKey().equals(SectionType.ACHIEVEMENT) || entry.getKey().equals(SectionType.QUALIFICATIONS)) {
                    List<String> descriptions = ((TextListSection) entry.getValue()).get();
                    dataOutputStream.writeInt(descriptions.size());
                    for (String description : descriptions) {
                        dataOutputStream.writeUTF(description);
                    }
                } else if (entry.getKey().equals(SectionType.EXPERIENCE)) {
                    List<Company> companies = ((CompanySection) entry.getValue()).get();
                    dataOutputStream.writeInt(companies.size());
                    for (Company company : companies) {
                        dataOutputStream.writeUTF(company.getHomepage().getName());
                        dataOutputStream.writeUTF(company.getHomepage().getUrl());
                        List<Company.Position> positions = company.getPositions();
                        dataOutputStream.writeInt(positions.size());
                        for (Company.Position position : positions) {
                            dataOutputStream.writeUTF(position.getStartDate().toString());
                            dataOutputStream.writeUTF(position.getEndDate().toString());
                            dataOutputStream.writeUTF(position.getTitle());
                            dataOutputStream.writeUTF(position.getDescription());
                        }

                    }
                }
            }

        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(is)) {
            String uuid = dataInputStream.readUTF();
            String fullName = dataInputStream.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int size = dataInputStream.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dataInputStream.readUTF()), dataInputStream.readUTF());
            }
            size = dataInputStream.readInt();
            for (int i = 0; i < size; i++) {
                SectionType sectionType = SectionType.valueOf(dataInputStream.readUTF());
                if (sectionType.equals(SectionType.OBJECTIVE) || sectionType.equals(SectionType.PERSONAL)) {
                    resume.addSection(sectionType, new TextSection(dataInputStream.readUTF()));
                } else if (sectionType.equals(SectionType.ACHIEVEMENT) || sectionType.equals(SectionType.QUALIFICATIONS)) {
                    int countDescriptions = dataInputStream.readInt();
                    List<String> descriptions = new ArrayList<>();
                    for (int j = 0; j < countDescriptions; j++) {
                        descriptions.add(dataInputStream.readUTF());
                    }
                    resume.addSection(sectionType, new TextListSection(descriptions));
                } else if (sectionType.equals(SectionType.EXPERIENCE)) {
                    List<Company> companies = new ArrayList<>();
                    int countCompanies = dataInputStream.readInt();
                    for (int j = 0; j < countCompanies; j++) {
                        Link link = new Link(dataInputStream.readUTF(), dataInputStream.readUTF());
                        List<Company.Position> positions = new ArrayList<>();
                        int countPositions = dataInputStream.readInt();
                        for (int k = 0; k < countPositions; k++) {
                            Company.Position position = new Company.Position(YearMonth.parse(dataInputStream.readUTF())
                                                                        , YearMonth.parse(dataInputStream.readUTF())
                                                                        , dataInputStream.readUTF(), dataInputStream.readUTF());
                            positions.add(position);
                        }
                        companies.add(new Company(link, positions));
                    }
                    resume.addSection(sectionType, new CompanySection(companies));
                }
            }
            return resume;
        }
    }
}
