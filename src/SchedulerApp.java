import java.util.List;
import java.util.Scanner;

public class SchedulerApp {

    public static void main(String[] args) {
        LoginSystem loginSystem = new LoginSystem(); // Create an instance of LoginSystem
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            System.out.println(); // Add a new line after choosing an option

            if ("1".equals(choice)) {
                List<Account> accounts = loginSystem.loadAccounts("src/accounts.txt"); // Call loadAccounts method

                System.out.print("Enter account ID: ");
                String accountId = scanner.nextLine().trim();
                System.out.print("Enter password: ");
                String password = scanner.nextLine().trim();
                System.out.println(); // Add a new line after entering credentials

                Account account = loginSystem.validateCredentials(accounts, accountId, password); // Call validateCredentials method
                if (account != null) {
                    System.out.println("Welcome, " + account.getName() + "! You are logged in as a " + account.getRole() + ".");
                    System.out.println(); // Add a new line after welcome message
                    if ("manager".equalsIgnoreCase(account.getRole())) {
                        managerMethods managerMethods = new managerMethods();
                        managerMethods.pageManager();
                    } else if ("worker".equalsIgnoreCase(account.getRole())) {
                        WorkerMethods workerMethods = new WorkerMethods();
                        workerMethods.pageWorker(accountId, account.getName());
                    }
                } else {
                    System.out.println("Invalid credentials.");
                    System.out.println(); // Add a new line after invalid credentials message
                }
            } else if ("2".equals(choice)) {
                System.out.println("Exiting the application.");
                System.out.println(); // Add a new line after exiting message
                break;
            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
                System.out.println(); // Add a new line after invalid choice message
            }
        }
        scanner.close();
    }
}