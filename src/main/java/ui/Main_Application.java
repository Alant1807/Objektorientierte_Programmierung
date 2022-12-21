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
    private final PrivateBank privateBank;
    private Stage primaryStage;
    private ObservableList<String> accountList = FXCollections.observableArrayList();
    public String derAccount;

    /**
     * setzt die Privatebank und addet alle accounts beim Aufruf.
     *
     * @throws TransactionAttributeException
     */
    public Main_Application() throws TransactionAttributeException {
        this.privateBank = new PrivateBank("Bank", 0.01, 0.05,
                "Bankdirectory");
        accountList.addAll(privateBank.getAllAccounts());
    }

    /**
     * erstellt ein Payment-Objekt.
     *
     * @param date {@link Transaction#getDate()}
     * @param amount {@link Transaction#getAmount()}
     * @param desc {@link Transaction#getDescription()}
     * @return das erstellte Payment-Objekt
     * @throws TransactionAttributeException
     */
    public Payment initPayment(String date, double amount, String desc) throws TransactionAttributeException {
        return new Payment(date, amount, desc);
    }

    /**
     * erstellt ein Incoming-Objekt.
     *
     * @param date {@link Transaction#getDate()}
     * @param amount {@link Transaction#getAmount()}
     * @param desc {@link Transaction#getDescription()}
     * @param sender {@link Transfer#getSender()}
     * @param recipient {@link Transfer#getRecipient()}
     * @return das erstellte Incoming-Objekt
     * @throws TransactionAttributeException
     */
    public IncomingTransfer initIncoming(String date, double amount, String desc, String sender, String recipient)
            throws TransactionAttributeException {
        IncomingTransfer incomingTransfer = new IncomingTransfer(date, amount, desc);
        return new IncomingTransfer(incomingTransfer, sender, recipient);
    }

    /**
     * erstellt das Outgoing-Objekt.
     *
     * @param date {@link Transaction#getDate()}
     * @param amount {@link Transaction#getAmount()}
     * @param desc {@link Transaction#getDescription()}
     * @param sender {@link Transfer#getSender()}
     * @param recipient {@link Transfer#getRecipient()}
     * @return das erstellte Outgoing-Objekt
     * @throws TransactionAttributeException
     */
    public OutgoingTransfer initOutgoing(String date, double amount, String desc, String sender, String recipient)
            throws TransactionAttributeException {
        OutgoingTransfer outgoingTransfer = new OutgoingTransfer(date, amount, desc);
        return new OutgoingTransfer(outgoingTransfer, sender, recipient);
    }

    /**
     * Getter für die Bank.
     *
     * @return gibt die PrivateBank zurück
     */
    public PrivateBank getBank() {
        return this.privateBank;
    }

    /**
     * Getter für di Accounts die in der Listview sind.
     *
     * @return gibt die Accounts in der Listview zurück
     */
    public ObservableList<String> getaccountsList() {
        return this.accountList;
    }

    /**
     * Sucht in der Account Listview nach dem übergebenen Account.
     *
     * @param derAccount Gesuchter Account
     * @return gibt gefundenen Account zurück
     */
    public String getDerAccount(String derAccount) {
        for (String searchacc : accountList) {
            if (Objects.equals(searchacc, derAccount)) {
                return searchacc;
            }
        }
        return null;
    }

    /**
     * startet die MainView scene.
     *
     * @param primaryStage setzt und zeigt Szene
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle(privateBank.getName());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("MainView.fxml"));
            AnchorPane pane = fxmlLoader.load();
            MainViewController mainViewController = fxmlLoader.getController();
            mainViewController.setMain_application(this);
            primaryStage.setScene(new Scene(pane));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Startet die AccountView scene.
     *
     * @param acc Account, desse Accountview angezeigt werden soll
     */
    public void showAccountView(String acc) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("AccountView.fxml"));
            AnchorPane pane = fxmlLoader.load();
            AccountViewController controller = fxmlLoader.getController();
            derAccount = acc;
            controller.setMainApp(this);
            controller.setAccountNameLabel(derAccount);
            double balance;
            balance = this.privateBank.getAccountBalance(derAccount);
            controller.setAccountBalanceLabel(balance);
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Getter für die Mainview.
     *
     * @return gibt die primaryStage zurück.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
