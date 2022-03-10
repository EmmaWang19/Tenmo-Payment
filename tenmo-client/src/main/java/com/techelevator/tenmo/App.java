package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TenmoService;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final TenmoService tenmoService = new TenmoService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        tenmoService.setAuthToken(currentUser.getToken());
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        BigDecimal balance = tenmoService.getBalance();
        System.out.println("Your current account balance is: $"+balance);
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		Transfer[] transfers = tenmoService.listHistory();
        System.out.println("\n --------------------------------------------"+
                "\n Transfers"+
                "\n ID          From/To             Amount"+
                "\n --------------------------------------------");
        for(Transfer transfer: transfers){
            System.out.println(transfer.toTransferFormat());
        }
        System.out.println("---------------------");
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
        System.out.println("-------------------------------------------\n" +
                "Pending Transfers\n" +
                "ID          To                     Amount\n" +
                "-------------------------------------------");
		Transfer[] transfers = tenmoService.listPending();
        for(Transfer transfer: transfers){
            System.out.println(transfer.toPendingFormat());
        }
        System.out.println("---------");
        int id = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
        consoleService.printApproveMenu();
        int approve = consoleService.promptForInt("Please choose an option: ");
        tenmoService.updatePending(id, approve);
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        showUsers();
		int id = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");
        Transfer transfer = tenmoService.sendBucks(id, amount);
        //System.out.println(transfer.toString());
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
        showUsers();
		int id = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel): ");
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");
        Transfer transfer = tenmoService.requestBucks(id, amount);
        //System.out.println(transfer.toString());
	}

    private void showUsers() {
        System.out.println("-------------------------------------------\n" +
                "Users\n" +
                "ID          Name\n" +
                "-------------------------------------------");
        User[] users = tenmoService.getUsers();
        for(User user: users){
            System.out.println(user.formatUser());
        }
        System.out.println("---------");
        System.out.println();
    }

}
