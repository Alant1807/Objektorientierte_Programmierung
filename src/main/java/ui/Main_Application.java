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

public class Main_Application extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    private PrivateBank privateBank;
    private Payment payment;
    private IncomingTransfer incomingTransfer;
    private OutgoingTransfer outgoingTransfer;
    private Stage primaryStage;
    private ObservableList<String> accountList = FXCollections.observableArrayList();
    private ObservableList<Transaction> selectedAccountTransactions;
    public String derAccount;

    public Main_Application() throws TransactionAttributeException {
        this.privateBank = new PrivateBank("Bank", 0.01, 0.05,
                "Bankdirectory");
        accountList.addAll(privateBank.getAllAccounts());
        this.payment = new Payment();
        this.incomingTransfer = new IncomingTransfer();
        this.outgoingTransfer = new OutgoingTransfer();
    }

    public PrivateBank getBank() {
        return this.privateBank;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public IncomingTransfer getIncomingTransfer() {
        return this.incomingTransfer;
    }

    public OutgoingTransfer getOutgoingTransfer() {
        return this.outgoingTransfer;
    }

    public ObservableList<String> getaccountsList() {
        return this.accountList;
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
            AccountViewController controller = fxmlLoader.getController();
            derAccount = acc;
            controller.setMainApp(this);
            controller.setAccountNameLabel(acc);
            double balance = 0.0;
            balance = this.privateBank.getAccountBalance(acc);
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
