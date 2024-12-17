package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

class gradeBook {
    class Session {
        class Subject {
            private String name;
            private int mark;

            Subject(String subjName, int subjMark) {
                name = subjName;
                mark = subjMark;
            }

            @Override
            public String toString() {
                return name + ": " + mark;
            }
        }

        Vector<Subject> subjects;

        Session() {
            subjects = new Vector<>();
        }

        Session(String subj, int mark) {
            subjects = new Vector<>();
            addSubj(subj, mark);
        }

        void addSubj(String subj, int mark) {
            subjects.add(new Subject(subj, mark));
        }

        @Override
        public String toString() {
            return subjects.toString();
        }
    }

    public int num;
    private String name;
    private String secondName;
    private String surname;
    private int year;
    private int group;
    private HashMap<Integer, Session> sessions;

    gradeBook() {
        sessions = new HashMap<>();
    }

    gradeBook(int Num, String studentSecondName, String studentName, String studentSurname, int studentYear, int studentGroup) {
        num = Num;
        name = studentName;
        secondName = studentSecondName;
        surname = studentSurname;
        year = studentYear;
        group = studentGroup;
        sessions = new HashMap<>();
    }

    int getNum() {
        return num;
    }

    void addSubj(int session, String subject, int mark) {
        if (sessions.containsKey(session)) {
            sessions.get(session).addSubj(subject, mark);
        } else {
            sessions.put(session, new Session(subject, mark));
        }
    }

    @Override
    public String toString() {
        return num + " " + secondName + " " + name + " " + surname + " " + year + " " + group + " " + sessions;
    }
}

public class Main {
    public static void main(String[] args) throws IOException, JAXBException {
        ArrayList<gradeBook> students = new ArrayList<>();
        Scanner cin = new Scanner(System.in);

        // Выбор формата ввода: JSON, XML или текст
        System.out.println("Выберите формат ввода данных (1 - JSON, 2 - XML, 3 - Текст): ");
        int choice = cin.nextInt();

        switch (choice) {
            case 1:
                // Чтение из JSON файла
                Gson gson = new Gson();
                try (Reader reader = Files.newBufferedReader(Paths.get("students.json"))) {
                    students = gson.fromJson(reader, new TypeToken<ArrayList<gradeBook>>(){}.getType());
                } catch (IOException e) {
                    System.out.println("Ошибка чтения JSON файла: " + e.getMessage());
                }
                break;

            case 2:
                // Чтение из XML файла
                File xmlFile = new File("students.xml");
                JAXBContext context = JAXBContext.newInstance(StudentsWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                StudentsWrapper wrapper = (StudentsWrapper) unmarshaller.unmarshal(xmlFile);
                students = wrapper.getStudents();
                break;

            case 3:
                // Чтение из текстового файла
                File studFile = new File("students.txt");
                if (!studFile.exists()) {
                    System.out.println("Ошибка: файл students.txt не найден.");
                    return;
                }
                Scanner in = new Scanner(studFile);
                while (in.hasNext()) {
                    students.add(new gradeBook(in.nextInt(), in.next(), in.next(), in.next(), in.nextInt(), in.nextInt()));
                }
                break;
        }

        // Вывод студентов на экран
        for (gradeBook gb : students) {
            System.out.println(gb);
        }

        // Добавление экзаменов как в вашем оригинальном коде
        // ...

        // Выбор формата вывода данных: JSON, XML или текст
        System.out.println("Выберите формат вывода данных (1 - JSON, 2 - XML, 3 - Текст): ");
        choice = cin.nextInt();

        switch (choice) {
            case 1:
                // Запись в JSON файл
                Gson gson = new Gson();
                try (FileWriter writer = new FileWriter("exams_results.json")) {
                    gson.toJson(students, writer);
                } catch (IOException e) {
                    System.out.println("Ошибка при записи в JSON файл: " + e.getMessage());
                }
                break;

            case 2:
                // Запись в XML файл
                JAXBContext context = JAXBContext.newInstance(StudentsWrapper.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                StudentsWrapper wrapper = new StudentsWrapper();
                wrapper.setStudents(students);
                marshaller.marshal(wrapper, new File("exams_results.xml"));
                break;

            case 3:
                // Запись в текстовый файл
                try (FileWriter writer = new FileWriter("exams_results.txt")) {
                    for (gradeBook gb : students) {
                        writer.write(gb.toString() + "\n");
                    }
                } catch (IOException e) {
                    System.out.println("Ошибка при записи в файл: " + e.getMessage());
                }
                break;
        }

        System.out.println("Данные успешно записаны.");
    }
}

// Обёртка для списка студентов для XML
@XmlRootElement(name = "students")
class StudentsWrapper {
    private ArrayList<gradeBook> students;

    @XmlElement(name = "student")
    public ArrayList<gradeBook> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<gradeBook> students) {
        this.students = students;
    }
}