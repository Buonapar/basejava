package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DataStreamSerializer implements StreamSerializer {

    private <T> void writeWithExeption(Collection<T> collect, DataOutputStream dataOutputStream, WriteWithExeption<T> action) throws IOException {
        Objects.requireNonNull(collect);
        Objects.requireNonNull(dataOutputStream);
        Objects.requireNonNull(action);
        dataOutputStream.writeInt(collect.size());
        for (T t : collect) {
            action.write(t);
        }
    }

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(os)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());

            writeWithExeption(resume.getContact().entrySet(), dataOutputStream, contact -> {
                dataOutputStream.writeUTF(contact.getKey().name());
                dataOutputStream.writeUTF(contact.getValue());
            });

            writeWithExeption(resume.getSection().entrySet(), dataOutputStream, sectionEntry -> {
                Section section = sectionEntry.getValue();
                SectionType sectionType = SectionType.valueOf(sectionEntry.getKey().name());
                dataOutputStream.writeUTF(sectionType.name());
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dataOutputStream.writeUTF(((TextSection) section).get());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        writeWithExeption(((TextListSection) section).get(), dataOutputStream, dataOutputStream::writeUTF);
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        writeWithExeption(((CompanySection) section).get(), dataOutputStream, company -> {
                            dataOutputStream.writeUTF(company.getHomepage().getName());
                            dataOutputStream.writeUTF(company.getHomepage().getUrl());
                            writeWithExeption(company.getPositions(), dataOutputStream, position -> {
                                dataOutputStream.writeUTF(position.getStartDate().toString());
                                dataOutputStream.writeUTF(position.getEndDate().toString());
                                dataOutputStream.writeUTF(position.getTitle());
                                dataOutputStream.writeUTF(position.getDescription());
                            });
                        });
                        break;
                }
            });
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
                List<Company> companies = new ArrayList<>();
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.addSection(sectionType, new TextSection(dataInputStream.readUTF()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        int countDescriptions = dataInputStream.readInt();
                        List<String> descriptions = new ArrayList<>();
                        for (int j = 0; j < countDescriptions; j++) {
                            descriptions.add(dataInputStream.readUTF());
                        }
                        resume.addSection(sectionType, new TextListSection(descriptions));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
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
                        break;
                }
            }
            return resume;
        }
    }

    @FunctionalInterface
    private interface WriteWithExeption<T> {
        void write(T t) throws IOException;
    }


}
