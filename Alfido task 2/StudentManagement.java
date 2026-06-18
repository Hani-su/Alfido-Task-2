// A simple console-based student management system in Java
// file name: StudentManagement.java
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Student {
    private int id;
    private String name;
    private int age;
    private double marks; 

    public Student(int id, String name, int age, double marks) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.marks = marks;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getMarks() { return marks; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setMarks(double marks) { this.marks = marks; }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Age: %d | Marks: %.2f", id, name, age, marks);
    }
}

public class StudentManagement {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Student> students = new ArrayList<>();
    private static int nextId = 1;

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1: addStudent(); break;
                case 2: listStudents(); break;
                case 3: searchStudent(); break;
                case 4: updateStudent(); break;
                case 5: deleteStudent(); break;
                case 6: showStatistics(); break;
                case 7: exit = confirmExit(); break;
                default:
                    System.out.println("Invalid choice. Please choose 1-7.");
            }
            System.out.println(); // blank line between operations
        }
        System.out.println("Exiting Student Management. Goodbye!");
    }

    private static void printMenu() {
        System.out.println("=== Student Management ===");
        System.out.println("1. Add student");
        System.out.println("2. List all students");
        System.out.println("3. Search student by ID");
        System.out.println("4. Update student by ID");
        System.out.println("5. Delete student by ID");
        System.out.println("6. Show statistics (average, highest, lowest)");
        System.out.println("7. Exit");
    }

    private static void addStudent() {
        System.out.println("-- Add Student --");
        String name = readNonEmptyString("Name: ");
        int age = readIntInRange("Age: ", 3, 120);
        double marks = readDoubleInRange("Marks (0-100): ", 0.0, 100.0);
        Student s = new Student(nextId++, name, age, marks);
        students.add(s);
        System.out.println("Student added: " + s);
    }

    private static void listStudents() {
        System.out.println("-- All Students --");
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        for (Student s : students) {
            System.out.println(s);
        }
    }

    private static void searchStudent() {
        System.out.println("-- Search Student --");
        int id = readInt("Enter student ID: ");
        Student s = findById(id);
        if (s == null) {
            System.out.println("Student with ID " + id + " not found.");
        } else {
            System.out.println("Found: " + s);
        }
    }

    private static void updateStudent() {
        System.out.println("-- Update Student --");
        int id = readInt("Enter student ID to update: ");
        Student s = findById(id);
        if (s == null) {
            System.out.println("Student with ID " + id + " not found.");
            return;
        }
        System.out.println("Current: " + s);
        String name = readNonEmptyString("New name (leave blank to keep): ", true);
        if (!name.isEmpty()) s.setName(name);
        String ageInput = readString("New age (leave blank to keep): ");
        if (!ageInput.trim().isEmpty()) {
            int age = parseIntWithDefault(ageInput, s.getAge());
            if (age < 3 || age > 120) {
                System.out.println("Age out of range; keeping existing age.");
            } else {
                s.setAge(age);
            }
        }
        String marksInput = readString("New marks (leave blank to keep): ");
        if (!marksInput.trim().isEmpty()) {
            double m = parseDoubleWithDefault(marksInput, s.getMarks());
            if (m < 0 || m > 100) {
                System.out.println("Marks out of range; keeping existing marks.");
            } else {
                s.setMarks(m);
            }
        }
        System.out.println("Updated: " + s);
    }

    private static void deleteStudent() {
        System.out.println("-- Delete Student --");
        int id = readInt("Enter student ID to delete: ");
        Student s = findById(id);
        if (s == null) {
            System.out.println("Student with ID " + id + " not found.");
            return;
        }
        System.out.print("Are you sure you want to delete (y/n)? ");
        String ans = scanner.nextLine().trim().toLowerCase();
        if (ans.equals("y") || ans.equals("yes")) {
            students.remove(s);
            System.out.println("Student deleted.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private static void showStatistics() {
        System.out.println("-- Statistics --");
        if (students.isEmpty()) {
            System.out.println("No students to compute statistics.");
            return;
        }
        double sum = 0;
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        for (Student s : students) {
            double m = s.getMarks();
            sum += m;
            if (m > max) max = m;
            if (m < min) min = m;
        }
        double avg = sum / students.size();
        System.out.printf("Count: %d | Average: %.2f | Highest: %.2f | Lowest: %.2f%n",
                students.size(), avg, max, min);
    }

    private static Student findById(int id) {
        for (Student s : students) if (s.getId() == id) return s;
        return null;
    }

    // Input helper methods
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (Exception e) {
                System.out.println("Invalid integer, try again.");
            }
        }
    }

    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int v = readInt(prompt);
            if (v < min || v > max) {
                System.out.println("Please enter a value between " + min + " and " + max + ".");
            } else return v;
        }
    }

    private static double readDoubleInRange(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                double v = Double.parseDouble(line.trim());
                if (v < min || v > max) {
                    System.out.println("Please enter a value between " + min + " and " + max + ".");
                } else return v;
            } catch (Exception e) {
                System.out.println("Invalid number, try again.");
            }
        }
    }

    private static String readNonEmptyString(String prompt) {
        return readNonEmptyString(prompt, false);
    }

    private static String readNonEmptyString(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (allowEmpty) return line;
            if (line.trim().isEmpty()) {
                System.out.println("Value cannot be empty.");
            } else return line.trim();
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int parseIntWithDefault(String s, int def) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; }
    }

    private static double parseDoubleWithDefault(String s, double def) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return def; }
    }

    private static boolean confirmExit() {
        System.out.print("Are you sure you want to exit (y/n)? ");
        String ans = scanner.nextLine().trim().toLowerCase();
        return ans.equals("y") || ans.equals("yes");
    }
}