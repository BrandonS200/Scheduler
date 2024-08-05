import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class WorkerMethods {

    public void pageWorker(String employeeId, String name) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Display Schedule");
            System.out.println("2. Schedule Change Request");
            System.out.println("3. Back");
            System.out.print("Enter your choice: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                scanner.next(); // Consume invalid input
                System.out.println(); // Add a new line after invalid input
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.println(); // Add a new line after valid input

            switch (choice) {
                case 1:
                    displaySchedule();
                    break;
                case 2:
                    scheduleChangeRequest(employeeId, name);
                    break;
                case 3:
                    return; // Exit the loop and go back
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                    System.out.println(); // Add a new line after invalid choice message
            }
        }
    }

    private void displaySchedule() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/schedule.txt"));
            if (lines.isEmpty()) {
                System.out.println("No schedule available.");
                System.out.println(); // Add a new line after no schedule message
            } else {
                System.out.println("Schedule:");
                for (String line : lines) {
                    System.out.println(line);
                }
                System.out.println(); // Add a new line after displaying the schedule
            }
        } catch (IOException e) {
            System.out.println("Error reading schedule file.");
            e.printStackTrace();
            System.out.println(); // Add a new line after error message
        }
    }

    private void scheduleChangeRequest(String employeeId, String name) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Date of Request (or type 'cancel' to go back): ");
        String dateOfRequest = scanner.nextLine().trim();
        if (dateOfRequest.equalsIgnoreCase("cancel")) {
            System.out.println("Request cancelled.");
            System.out.println(); // Add a new line after cancelling
            return;
        }

        System.out.print("Enter the Requested Change (or type 'cancel' to go back): ");
        String requestedChange = scanner.nextLine().trim();
        if (requestedChange.equalsIgnoreCase("cancel")) {
            System.out.println("Request cancelled.");
            System.out.println(); // Add a new line after cancelling
            return;
        }

        System.out.print("Enter the Reason for the Request (or type 'cancel' to go back): ");
        String reason = scanner.nextLine().trim();
        if (reason.equalsIgnoreCase("cancel")) {
            System.out.println("Request cancelled.");
            System.out.println(); // Add a new line after cancelling
            return;
        }

        String request = String.format(
            "Employee ID: %s%nName: %s%nDate of Request: %s%nRequested Change: %s%nReason: %s%n%n",
            employeeId, name, dateOfRequest, requestedChange, reason
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/timeRequests.txt", true))) {
            writer.write(request);
            writer.newLine(); // Add a new line after writing the request
            System.out.println("Your request has been submitted.");
            System.out.println(); // Add a new line after submitting the request
        } catch (IOException e) {
            System.out.println("Error writing to timeRequests.txt.");
            e.printStackTrace();
            System.out.println(); // Add a new line after error message
        }
    }
}