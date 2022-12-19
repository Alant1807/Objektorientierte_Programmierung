package ui;

import bank.*;
import bank.exceptions.TransactionAttributeException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main_Application extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private PrivateBank privateBank;
    private Stage primaryStage;
    private ObservableList<String> accountList = FXCollections.observableArrayList();
    private ObservableList<Transaction> selectedAccountTransactions;
    public String derAccount;

    public Main_Application() throws TransactionAttributeException {
        this.privateBank = new PrivateBank("Bank", 0.01, 0.05,
                "Bankdirectory");
        accountList.addAll(privateBank.getAllAccounts());
    }

    public Payment initPayment(String date, double amount, String desc) throws TransactionAttributeException {
        return new Payment(date, amount, desc);
    }

    public IncomingTransfer initIncoming(String date, double amount, String desc, String sender, String recipient)
            throws TransactionAttributeException {
        IncomingTransfer incomingTransfer = new IncomingTransfer(date, amount, desc);
        return new IncomingTransfer(incomingTransfer, sender, recipient);
    }

    public OutgoingTransfer initOutgoing(String date, double amount, String desc, String sender, String recipient)
            throws TransactionAttributeException {
        OutgoingTransfer outgoingTransfer = new OutgoingTransfer(date, amount, desc);
        return new OutgoingTransfer(outgoingTransfer, sender, recipient);
    }

    public PrivateBank getBank() {
        return this.privateBank;
    }

    public ObservableList<String> getaccountsList() {
        return this.accountList;
    }

    public String getDerAccount(String derAccount) {
        for (String searchacc : accountList) {
            if (Objects.equals(searchacc, derAccount)) {
                return searchacc;
            }
        }
        return null;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle(privateBank.getName());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("MainView.fxml"));
            AnchorPane pane = (AnchorPane) fxmlLoader.load();
            MainViewController mainViewController = (MainViewController) fxmlLoader.getController();
            mainViewController.setMain_application(this);
            primaryStage.setScene(new Scene(pane));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAccountView(String acc) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("AccountView.fxml"));
            AnchorPane pane = (AnchorPane) fxmlLoader.load();
            AccountViewController controller = (AccountViewController) fxmlLoader.getController();
            derAccount = acc;
            controller.setMainApp(this);
            controller.setAccountNameLabel(derAccount);
            double balance = 0.0;
            balance = this.privateBank.getAccountBalance(derAccount);
            controller.setAccountBalanceLabel(balance);
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
