package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(os)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());

            writeWithException(resume.getContact().entrySet(), dataOutputStream, contact -> {
                dataOutputStream.writeUTF(contact.getKey().name());
                dataOutputStream.writeUTF(contact.getValue());
            });

            writeWithException(resume.getSections().entrySet(), dataOutputStream, sectionEntry -> {
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
                        writeWithException(((TextListSection) section).get(), dataOutputStream, dataOutputStream::writeUTF);
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        writeWithException(((CompanySection) section).get(), dataOutputStream, company -> {
                            dataOutputStream.writeUTF(company.getHomepage().getName());
                            dataOutputStream.writeUTF(company.getHomepage().getUrl());
                            writeWithException(company.getPositions(), dataOutputStream, position -> {
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
            listWalk(dataInputStream, ()-> resume.addContact(ContactType.valueOf(dataInputStream.readUTF()), dataInputStream.readUTF()));
            listWalk(dataInputStream, ()-> {
                SectionType sectionType = SectionType.valueOf(dataInputStream.readUTF());
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.addSection(sectionType, new TextSection(dataInputStream.readUTF()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        resume.addSection(sectionType, new TextListSection(readWithException(dataInputStream, dataInputStream::readUTF)));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        resume.addSection(sectionType, new CompanySection(
                                readWithException(dataInputStream, () -> new Company(
                                        new Link(dataInputStream.readUTF(), dataInputStream.readUTF())
                                        , readWithException(dataInputStream, () -> new Company.Position(
                                        parseDate(dataInputStream)
                                        , parseDate(dataInputStream)
                                        , dataInputStream.readUTF()
                                        , dataInputStream.readUTF()))))));
                        break;
                }
            });
            return resume;
        }
    }

    private <T> void writeWithException(Collection<T> collect, DataOutputStream dataOutputStream, ConsumerWrite<T> action) throws IOException {
        Objects.requireNonNull(collect);
        Objects.requireNonNull(dataOutputStream);
        Objects.requireNonNull(action);
        dataOutputStream.writeInt(collect.size());
        for (T t : collect) {
            action.write(t);
        }
    }

    private <T> List<T> readWithException(DataInputStream dataInputStream, ConsumerRead<T> action) throws IOException {
        Objects.requireNonNull(dataInputStream);
        Objects.requireNonNull(action);
        List<T> consumerList = new ArrayList<>();
        int size = dataInputStream.readInt();
        for (int i = 0; i < size; i++) {
            consumerList.add(action.read());
        }
        return consumerList;
    }

    private void listWalk(DataInputStream dataInputStream, ConsumerAdd action) throws IOException {
        Objects.requireNonNull(dataInputStream);
        Objects.requireNonNull(action);
        int size = dataInputStream.readInt();
        for (int i = 0; i < size; i++) {
            action.add();
        }
    }

    private YearMonth parseDate(DataInputStream dataInputStream) throws IOException {
        return YearMonth.parse(dataInputStream.readUTF());
    }

    @FunctionalInterface
    private interface ConsumerWrite<T> {

        void write(T t) throws IOException;
    }

    @FunctionalInterface
    private interface ConsumerRead<T> {
        T read() throws IOException;
    }

    @FunctionalInterface
    private interface ConsumerAdd {
        void add() throws IOException;
    }


}
