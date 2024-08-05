import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Account {
    private String accountId;
    private String password;
    private String role;
    private String name;

    Account(String accountId, String password, String role, String name) {
        this.accountId = accountId;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    // Getters and setters
    public String getAccountId() {
        return accountId;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class LoginSystem {

    public List<Account> loadAccounts(String filePath) {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String accountId = parts[0].trim();
                    String password = parts[1].trim();
                    String role = parts[2].trim();
                    String name = parts[3].trim();
                    accounts.add(new Account(accountId, password, role, name));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public Account validateCredentials(List<Account> accounts, String accountId, String password) {
        for (Account account : accounts) {
            if (account.getAccountId().equals(accountId) && account.getPassword().equals(password)) {
                return account;
            }
        }
        return null;
    }
}