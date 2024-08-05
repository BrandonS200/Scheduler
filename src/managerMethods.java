import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Arrays;

public class managerMethods {

    public void pageManager() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Display Schedule");
            System.out.println("2. Display Time Requests");
            System.out.println("3. Clear Time Requests");
            System.out.println("4. Modify Schedule");
            System.out.println("5. Create New Date");
            System.out.println("6. Remove Date");
            System.out.println("7. Back");
            System.out.print("Enter your choice: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
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
                    displayTimeRequests();
                    break;    
                case 3:
                    clearTimeRequests();
                    break;
                case 4:
                    modifyEmployeeTime();
                    break;
                case 5:
                    createNewDate();
                    break;
                case 6:
                    removeDate();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                    System.out.println(); // Add a new line after invalid choice
            }
        }
    }

    public void pageWorker() {
        System.out.println("1. View Times");
        System.out.println("2. Request Time Change");
        System.out.println("3. Back");
    }

    public void setTimes() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the new time to set:");
        String newTime = scanner.nextLine();
        System.out.println(); // Add a new line after user input

        try {
            // Read existing times from schedule.txt
            List<String> times = Files.readAllLines(Paths.get("src/schedule.txt"));

            // Add the new time to the list
            times.add(newTime);

            // Write the updated list back to schedule.txt
            Files.write(Paths.get("src/schedule.txt"), times, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Time set successfully.");
        } catch (IOException e) {
            System.err.println("Error setting time: " + e.getMessage());
        }
    }

    public void displaySchedule() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/schedule.txt"));
            for (String line : lines) {
                System.out.println(line);
            }
            System.out.println(); // Add a new line after displaying schedule
        } catch (IOException e) {
            System.err.println("Error reading schedule: " + e.getMessage());
        }
    }

    private void displayTimeRequests() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/timeRequests.txt"));
            if (lines.isEmpty()) {
                System.out.println("No time requests available.");
                System.out.println(); // Add a new line after no time requests message
            } else {
                System.out.println("Time Requests:");
                for (String line : lines) {
                    System.out.println(line);
                }
                System.out.println(); // Add a new line after displaying the time requests
            }
        } catch (IOException e) {
            System.out.println("Error reading time requests file.");
            e.printStackTrace();
            System.out.println(); // Add a new line after error message
        }
    }
    
    public void clearTimeRequests() {
        try {
            Files.write(Paths.get("src/timeRequests.txt"), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Time requests have been cleared.");
        } catch (IOException e) {
            System.out.println("Error clearing time requests file.");
            e.printStackTrace();
        }
    }    

    public void modifyEmployeeTime() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the date (YYYY-MM-DD) or type 'cancel' to go back:");
        String date = scanner.nextLine();
        if (date.equalsIgnoreCase("cancel")) {
            System.out.println("Operation cancelled.");
            System.out.println(); // Add a new line after cancelling
            return;
        }
        System.out.println(); // Add a new line after user input

        try {
            // Read existing schedule from schedule.txt
            List<String> lines = Files.readAllLines(Paths.get("src/schedule.txt"));

            // Check if the date exists
            boolean dateExists = lines.stream().anyMatch(line -> line.startsWith(date + ":"));
            if (!dateExists) {
                System.out.println("Date does not exist in the schedule.");
                return;
            }

            // Find the line for the specified date
            String dateLine = lines.stream()
                    .filter(line -> line.startsWith(date + ":"))
                    .findFirst()
                    .orElse(date + ":");

            // Get the current schedule for the date
            String currentSchedule = dateLine.substring(date.length() + 1).trim();

            System.out.println("Current schedule for " + date + ": " + currentSchedule);
            System.out.println("Do you want to add or remove an employee's time? (add/remove) or type 'cancel' to go back:");
            String action = scanner.nextLine();
            if (action.equalsIgnoreCase("cancel")) {
                System.out.println("Operation cancelled.");
                System.out.println(); // Add a new line after cancelling
                return;
            }
            System.out.println(); // Add a new line after user input

            if (action.equalsIgnoreCase("add")) {
                System.out.println("Enter the employee's name and time (e.g., JohnDoe 09:00-17:00) or type 'cancel' to go back:");
                String newEntry = scanner.nextLine();
                if (newEntry.equalsIgnoreCase("cancel")) {
                    System.out.println("Operation cancelled.");
                    System.out.println(); // Add a new line after cancelling
                    return;
                }
                System.out.println(); // Add a new line after user input
                currentSchedule = currentSchedule.isEmpty() ? newEntry : currentSchedule + ", " + newEntry;
            } else if (action.equalsIgnoreCase("remove")) {
                System.out.println("Enter the employee's name to remove or type 'cancel' to go back:");
                String employeeName = scanner.nextLine();
                if (employeeName.equalsIgnoreCase("cancel")) {
                    System.out.println("Operation cancelled.");
                    System.out.println(); // Add a new line after cancelling
                    return;
                }
                System.out.println(); // Add a new line after user input
                currentSchedule = removeEmployeeTime(currentSchedule, employeeName);
            } else {
                System.out.println("Invalid action.");
                return;
            }

            // Update the line for the specified date
            String updatedLine = date + ": " + currentSchedule;
            lines = lines.stream()
                    .map(line -> line.startsWith(date + ":") ? updatedLine : line)
                    .collect(Collectors.toList());

            // Write the updated schedule back to schedule.txt
            Files.write(Paths.get("src/schedule.txt"), lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Schedule updated successfully.");
            System.out.println(); // Add a new line after updating schedule
        } catch (IOException e) {
            System.err.println("Error modifying schedule: " + e.getMessage());
        }
    }

    public void createNewDate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the new date (YYYY-MM-DD) or type 'cancel' to go back:");
        String newDate = scanner.nextLine();
        if (newDate.equalsIgnoreCase("cancel")) {
            System.out.println("Operation cancelled.");
            System.out.println(); // Add a new line after cancelling
            return;
        }
        System.out.println(); // Add a new line after user input
        System.out.println("Enter the employee's name and time (e.g., JohnDoe 09:00-17:00) or type 'cancel' to go back:");
        String newEntry = scanner.nextLine();
        if (newEntry.equalsIgnoreCase("cancel")) {
            System.out.println("Operation cancelled.");
            System.out.println(); // Add a new line after cancelling
            return;
        }
        System.out.println(); // Add a new line after user input

        try {
            // Read existing schedule from schedule.txt
            List<String> lines = Files.readAllLines(Paths.get("src/schedule.txt"));

            // Create the new date entry
            String newDateLine = newDate + ": " + newEntry;
            lines.add(newDateLine);

            // Write the updated schedule back to schedule.txt
            Files.write(Paths.get("src/schedule.txt"), lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("New date added successfully.");
            System.out.println(); // Add a new line after adding new date
        } catch (IOException e) {
            System.err.println("Error adding new date: " + e.getMessage());
        }
    }

    public void removeDate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the date to remove (YYYY-MM-DD) or type 'cancel' to go back:");
        String date = scanner.nextLine();
        if (date.equalsIgnoreCase("cancel")) {
            System.out.println("Operation cancelled.");
            System.out.println(); // Add a new line after cancelling
            return;
        }
        System.out.println(); // Add a new line after user input

        try {
            // Read existing schedule from schedule.txt
            List<String> lines = Files.readAllLines(Paths.get("src/schedule.txt"));

            // Check if the date exists
            boolean dateExists = lines.stream().anyMatch(line -> line.startsWith(date + ":"));
            if (!dateExists) {
                System.out.println("Date does not exist in the schedule.");
                return;
            }

            // Remove the line for the specified date
            lines = lines.stream()
                    .filter(line -> !line.startsWith(date + ":"))
                    .collect(Collectors.toList());

            // Write the updated schedule back to schedule.txt
            Files.write(Paths.get("src/schedule.txt"), lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Date removed successfully.");
            System.out.println(); // Add a new line after removing date
        } catch (IOException e) {
            System.err.println("Error removing date: " + e.getMessage());
        }
    }

    private String removeEmployeeTime(String schedule, String employeeName) {
        String[] entries = schedule.split(", ");
        String updatedSchedule = Arrays.stream(entries)
                .filter(entry -> !entry.startsWith(employeeName + " "))
                .collect(Collectors.joining(", "));
        return updatedSchedule;
    }



}