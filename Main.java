
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class Task {
    int id;
    String title;
    String priority;
    String status;

    Task(int id, String title, String priority, String status) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = status;
    }

    void display() {
        System.out.println("\nTask ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Priority: " + priority);
        System.out.println("Status: " + status);
    }
}

public class Main {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Task> tasks = new ArrayList<>();

    static final String FILE_NAME = "tasks.txt";

    static void loadTasks() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split("\\|", -1);

                if (data.length == 4) {
                    try {
                        tasks.add(new Task(
                            Integer.parseInt(data[0]),
                            data[1],
                            data[2],
                            data[3]
                        ));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid task record.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks.");
        }
    }

    static void saveTasks() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (Task task : tasks) {
                writer.write(
                    task.id + "|" + task.title + "|"
                    + task.priority + "|" + task.status + "\n"
                );
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks.");
        }
    }

    static void addTask() {
        System.out.print("\nEnter task title: ");
        String title = sc.nextLine().trim();

        if (title.isEmpty() || title.contains("|")) {
            System.out.println("Invalid task title.");
            return;
        }

        System.out.println("Select Priority:");
        System.out.println("1. High");
        System.out.println("2. Medium");
        System.out.println("3. Low");

        System.out.print("Enter choice: ");
        String choice = sc.nextLine();

        String priority;

        switch (choice) {
            case "1":
                priority = "High";
                break;
            case "2":
                priority = "Medium";
                break;
            case "3":
                priority = "Low";
                break;
            default:
                System.out.println("Invalid priority.");
                return;
        }

        int id = 1001;

        for (Task task : tasks) {
            if (task.id >= id) {
                id = task.id + 1;
            }
        }

        tasks.add(new Task(id, title, priority, "Pending"));
        saveTasks();

        System.out.println("Task added successfully! Task ID: " + id);
    }

    static void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks available.");
            return;
        }

        System.out.println("\n===== ALL TASKS =====");

        for (Task task : tasks) {
            task.display();
        }
    }

    static void searchTask() {
        System.out.print("\nEnter Task ID: ");

        if (!sc.hasNextInt()) {
            System.out.println("Invalid task ID.");
            sc.nextLine();
            return;
        }

        int id = sc.nextInt();
        sc.nextLine();

        for (Task task : tasks) {
            if (task.id == id) {
                System.out.println("\nTask Found!");
                task.display();
                return;
            }
        }

        System.out.println("Task not found.");
    }

    static void updateTask() {
        System.out.print("\nEnter Task ID: ");

        if (!sc.hasNextInt()) {
            System.out.println("Invalid task ID.");
            sc.nextLine();
            return;
        }

        int id = sc.nextInt();
        sc.nextLine();

        for (Task task : tasks) {
            if (task.id == id) {
                System.out.println("1. Pending");
                System.out.println("2. In Progress");
                System.out.println("3. Completed");

                System.out.print("Select new status: ");
                String choice = sc.nextLine();

                switch (choice) {
                    case "1":
                        task.status = "Pending";
                        break;
                    case "2":
                        task.status = "In Progress";
                        break;
                    case "3":
                        task.status = "Completed";
                        break;
                    default:
                        System.out.println("Invalid status.");
                        return;
                }

                saveTasks();
                System.out.println("Task updated successfully.");
                return;
            }
        }

        System.out.println("Task not found.");
    }

    static void deleteTask() {
        System.out.print("\nEnter Task ID to delete: ");

        if (!sc.hasNextInt()) {
            System.out.println("Invalid task ID.");
            sc.nextLine();
            return;
        }

        int id = sc.nextInt();
        sc.nextLine();

        for (Task task : tasks) {
            if (task.id == id) {
                tasks.remove(task);
                saveTasks();
                System.out.println("Task deleted successfully.");
                return;
            }
        }

        System.out.println("Task not found.");
    }

    public static void main(String[] args) {
        loadTasks();

        while (true) {
            System.out.println("\n===== TASK MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Search Task");
            System.out.println("4. Update Task Status");
            System.out.println("5. Delete Task");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    addTask();
                    break;
                case "2":
                    viewTasks();
                    break;
                case "3":
                    searchTask();
                    break;
                case "4":
                    updateTask();
                    break;
                case "5":
                    deleteTask();
                    break;
                case "6":
                    System.out.println("Task manager closed.");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
