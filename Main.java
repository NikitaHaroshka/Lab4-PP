package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.*;

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
    public static void main(String[] args) throws IOException {
        ArrayList<gradeBook> students = new ArrayList<>();
        Scanner cin = new Scanner(System.in);

        // Обрабатываем только текстовые файлы
        File studFile = new File("E:/CODEE/1-ProPro/Book/src/main/java/org/example/students.txt");
        if (!studFile.exists()) {
            System.out.println("Ошибка: файл students.txt не найден.");
            return;
        }

        // Считывание студентов из файла
        Scanner in = new Scanner(studFile);
        while (in.hasNext()) {
            students.add(new gradeBook(in.nextInt(), in.next(), in.next(), in.next(),
                    in.nextInt(), in.nextInt()));
        }

        // Вывод студентов на экран
        for (gradeBook gb : students) {
            System.out.println(gb);
        }

        // Добавляем экзамены из других файлов
        String file = "E:/CODEE/1-ProPro/Book/src/main/java/org/example/exams.txt";  // Указываем жестко заданное имя файла
        File examFile = new File(file);
        if (!examFile.exists()) {
            System.out.println("Ошибка: файл " + file + " не найден.");
            return;
        }

        Scanner examIn = new Scanner(examFile);
        String subj;
        int N, session;

        while (examIn.hasNext()) {
            subj = examIn.nextLine().trim(); // Считываем имя предмета
            if (subj.isEmpty()) continue; // Если строка пуста, пропускаем ее

            session = examIn.nextInt(); // Считываем номер сессии
            examIn.nextLine(); // Переход к следующей строке после считывания номера сессии

            // Для каждого студента добавляем оценку по текущему предмету и сессии
            while (examIn.hasNext()) {
                N = examIn.nextInt(); // Считываем номер зачетки студента
                int mark = examIn.nextInt(); // Считываем оценку

                for (gradeBook gb : students) {
                    if (gb.getNum() == N) {
                        gb.addSubj(session, subj, mark); // Добавляем предмет и оценку студенту
                        break;
                    }
                }
                if (!examIn.hasNextInt()) break; // Если в строке нет номера зачетки, выходим из цикла
            }
        }

        // Вывод студентов после добавления экзаменов
        for (gradeBook gb : students) {
            System.out.println(gb);
        }

        // Запись в текстовый файл students.txt
        try (FileWriter writer = new FileWriter("E:/CODEE/1-ProPro/Book/src/main/java/org/example/exams_results.txt")) {
            for (gradeBook gb : students) {
                writer.write(gb.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }

        System.out.println("Данные записаны в файл exams_results.txt.");
    }
}
