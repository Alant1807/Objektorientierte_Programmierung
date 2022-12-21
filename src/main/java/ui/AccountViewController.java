package ui;

import bank.Transaction;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import bank.exceptions.TransactionAttributeException;
import bank.exceptions.TransactionDoesNotExistException;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AccountViewController implements EventHandler<ActionEvent> {
    Main_Application main_application;
    public String selectedAccount;
    //public String recipientAccount;
    private ObservableList<Transaction> selectedAccountTransactions;
    @FXML
    private ListView<Transaction> transactionListView = new ListView<>();
    @FXML
    protected Button sortAsc = new Button();
    @FXML
    protected Button sortDesc = new Button();
    @FXML
    protected Button sortPos = new Button();
    @FXML
    protected Button sortNeg = new Button();
    @FXML
    protected Button goBack = new Button();
    @FXML
    protected ContextMenu contextMenu = new ContextMenu();
    @FXML
    protected MenuItem menuItemLoeschen = new MenuItem("Löschen");
    @FXML
    protected Label AccountNameLabel = new Label();
    @FXML
    private Label AccountBalanceLabel = new Label();
    @FXML
    private Button newTransactionButton = new Button();
    public void setAccountNameLabel(String acc) {
        AccountNameLabel.setText("Account: " + acc);
    }

    /**
     * Setzt das Label der AccountBalance.
     *
     * @param amount übergebene Geldmenge
     */
    public void setAccountBalanceLabel(double amount) {
        String balance = String.valueOf(amount);
        AccountBalanceLabel.setText("Kontostand: " + balance + "€");
    }

    /**
     * Methode zum aufsteigenedem Sortieren der Listview.
     */
    private void handleSortAsc() {
        transactionListView.getItems().clear();
        selectedAccountTransactions = FXCollections.observableArrayList(
                main_application.getBank().getTransactionsSorted(selectedAccount, true)
        );
        transactionListView.setItems(selectedAccountTransactions);
    }

    /**
     * Methode zum absteigendem Sortieren der Listview.
     */
    private void handleSortDesc() {
        transactionListView.getItems().clear();
        selectedAccountTransactions = FXCollections.observableArrayList(
                main_application.getBank().getTransactionsSorted(selectedAccount, false)
        );
        transactionListView.setItems(selectedAccountTransactions);
    }

    /**
     * Methode zum Sortieren der Listview nach positiven Transaktionen.
     */
    private void handleSortPos() {
        transactionListView.getItems().clear();
        selectedAccountTransactions = FXCollections.observableArrayList(
                main_application.getBank().getTransactionsByType(selectedAccount, true)
        );
        transactionListView.setItems(selectedAccountTransactions);
    }

    /**
     * Methode zum Sortieren der Listview nach negativen Transaktionen.
     */
    private void handleSortNeg() {
        transactionListView.getItems().clear();
        selectedAccountTransactions = FXCollections.observableArrayList(
                main_application.getBank().getTransactionsByType(selectedAccount, false)
        );
        transactionListView.setItems(selectedAccountTransactions);
    }

    /**
     * Methode um zur Mainview zu wechseln.
     */
    private void goBack() {
        this.main_application.start(main_application.getPrimaryStage());
    }

    /**
     * setzt die AccountView bei jedem Aufruf.
     *
     * @param main_application setzt das Objekt auf die main_application
     */
    @FXML
    public void setMainApp(Main_Application main_application) {
        this.main_application = main_application;
        this.selectedAccount = main_application.derAccount;
        //this.recipientAccount = main_application.getDerAccount(recipientAccount);
        selectedAccountTransactions = FXCollections.observableArrayList(
                main_application.getBank().getTransactions(this.selectedAccount));
        transactionListView.setItems(selectedAccountTransactions);
        contextMenu.getItems().add(menuItemLoeschen);
        transactionListView.setContextMenu(contextMenu);
    }

    /**
     * Methode die alle Events abfängt und behandelt.
     *
     * @param actionEvent trigger event as a Parameter
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == menuItemLoeschen) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Transaktion löschen");
            alert.setHeaderText("Achtung, Transaktion löschen !!!");
            alert.setContentText("Sind Sie sich sicher ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    handleDelete();
                } catch (AccountDoesNotExistException | TransactionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                alert.close();
            }
        } else if (actionEvent.getSource() == newTransactionButton) {
            List<String> choices = new ArrayList<>();
            choices.add("Payment");
            choices.add("Transfer");
            ChoiceDialog<String> dialog = new ChoiceDialog<>("Payment", choices);
            dialog.setTitle("Transaktion durchführen");
            dialog.setContentText("Wählen sie ihre Art von Transaktion:");
            Optional<String> result = dialog.showAndWait();
            if (result.get().equals(choices.get(0))) {
                try {
                    createPaymentDialog();
                } catch (TransactionAttributeException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    createTransferDialog();
                } catch (TransactionAlreadyExistException e) {
                    alertTransactionExistAlready();
                } catch (AccountDoesNotExistException e) {
                    alertAccountdoesnotexist();
                } catch (TransactionAttributeException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (actionEvent.getSource() == sortAsc) {
            handleSortAsc();
        } else if (actionEvent.getSource() == sortDesc) {
            handleSortDesc();
        } else if (actionEvent.getSource() == sortPos) {
            handleSortPos();
        } else if (actionEvent.getSource() == sortNeg) {
            handleSortNeg();
        } else if (actionEvent.getSource() == goBack) {
            goBack();
        }
    }

    /**
     * Löscht die ausgewählte Transaktion.
     *
     * @throws AccountDoesNotExistException
     * @throws TransactionDoesNotExistException
     */
    private void handleDelete() throws AccountDoesNotExistException, TransactionDoesNotExistException {
        Transaction toDeleteTransaction = transactionListView.getSelectionModel().getSelectedItem();
        transactionListView.getItems().remove(toDeleteTransaction);
        main_application.getBank().removeTransaction(selectedAccount, toDeleteTransaction);
    }

    /**
     * Erstellt Transaktion
     */
    public void handleCreation() {
        transactionListView.getItems().clear();
        transactionListView.setItems(FXCollections.observableArrayList
                (main_application.getBank().getTransactions(selectedAccount))
        );
        double amount = main_application.getBank().getAccountBalance(selectedAccount);
        setAccountBalanceLabel(amount);
    }

    /*public void handleCreationRecipient() {
        transactionListView.getItems().clear();
        transactionListView.setItems(FXCollections.observableArrayList
                (main_application.getBank().getTransactions(recipientAccount))
        );
        double amount = main_application.getBank().getAccountBalance(recipientAccount);
        setAccountBalanceLabel(amount);
    }*/

    /**
     * erstellt Dialog um Payment-Werte zusetzen
     *
     * @throws TransactionAttributeException
     */
    public void createPaymentDialog() throws TransactionAttributeException {
        Dialog<ButtonType> paymentDialog = new Dialog<>();
        paymentDialog.setTitle("Payment durchführen");
        paymentDialog.setHeaderText("Wählen sie ihre Daten");
        paymentDialog.setResizable(true);
        paymentDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        paymentDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        Spinner<Integer> amountSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> spinner =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(-3000, 3000, 0);
        spinner.setWrapAround(false);
        amountSpinner.setValueFactory(spinner);
        //TextField amounttextfield = new TextField();
        TextField descriptiontextfiled = new TextField();
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Geldmenge:"), 1, 1);
        gridPane.add(amountSpinner, 2, 1);
        //gridPane.add(amounttextfield, 2, 1);
        gridPane.add(new Label("Beschreibung:"), 1, 2);
        gridPane.add(descriptiontextfiled, 2, 2);
        paymentDialog.getDialogPane().setContent(gridPane);
        ChangeListener<String> Listener = (((observableValue, s, t1) ->
                paymentDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable((
                        descriptiontextfiled.getText() == null || descriptiontextfiled.getText().trim().isEmpty()))));
        amountSpinner.valueProperty().addListener(((observableValue, integer, t1) ->
                paymentDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(amountSpinner.getValue() == 0)));
        /*ChangeListener<String> amountListener = (observableValue, s, t1) -> {
            for (int i = 0; i < amounttextfield.getLength(); i++){
                int asciivalue = amounttextfield.getText().charAt(i);
                paymentDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
            }
        };*/
        descriptiontextfiled.textProperty().addListener(Listener);
        //amounttextfield.textProperty().addListener(Listener);
        Optional<ButtonType> result = paymentDialog.showAndWait();
        double amount = Double.parseDouble(String.valueOf(amountSpinner.getValue()));
        String description = descriptiontextfiled.getText();
        //double amount = Double.parseDouble(amounttextfield.getText());
        String localDate = java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (result.get() == ButtonType.OK) {
            try {
                main_application.getBank().addTransaction(main_application.derAccount,
                        main_application.initPayment(localDate, amount, description));
                handleCreation();
            }
            catch (TransactionAttributeException | TransactionAlreadyExistException | AccountDoesNotExistException e){
                throw new RuntimeException(e);
            }
        } else {
            paymentDialog.close();
        }
    }

    /**
     * erstellt Transfer Dialog um Transfer-Werte zusetzen
     *
     * @throws TransactionAlreadyExistException
     * @throws AccountDoesNotExistException
     * @throws TransactionAttributeException
     */
    public void createTransferDialog() throws TransactionAlreadyExistException, AccountDoesNotExistException,
            TransactionAttributeException {
        Dialog<ButtonType> TransferDialog = new Dialog<>();
        TransferDialog.setTitle("Transfer durchführen");
        TransferDialog.setHeaderText("Wählen sie ihre Daten");
        TransferDialog.setResizable(true);
        TransferDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TransferDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        TextField amountTextfield = new TextField();
        TextField descriptionTextfield = new TextField();
        TextField senderTextfield = new TextField();
        TextField recipientTextfield = new TextField();
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Geldmenge:"), 1, 1);
        gridPane.add(amountTextfield, 2, 1);
        gridPane.add(new Label("Beschreibung:"), 1, 2);
        gridPane.add(descriptionTextfield, 2, 2);
        gridPane.add(new Label("Sender:"), 1, 3);
        gridPane.add(senderTextfield, 2, 3);
        gridPane.add(new Label("Empfänger:"), 1, 4);
        gridPane.add(recipientTextfield, 2, 4);
        TransferDialog.getDialogPane().setContent(gridPane);
        ChangeListener<String> Listener = (((observableValue, s, t1) ->
                TransferDialog.getDialogPane().lookupButton(ButtonType.OK).
                        setDisable((amountTextfield.getText() == null ||
                                amountTextfield.getText().trim().isEmpty() || Double.parseDouble(amountTextfield.getText()) < 0 ||
                                descriptionTextfield.getText() == null ||
                                descriptionTextfield.getText().trim().isEmpty() || senderTextfield.getText() == null ||
                                senderTextfield.getText().trim().isEmpty() || recipientTextfield.getText() == null ||
                                recipientTextfield.getText().trim().isEmpty()))));
        amountTextfield.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                amountTextfield.setText(t1.replaceAll("\\D", ""));
            }
        });
        amountTextfield.textProperty().addListener(Listener);
        descriptionTextfield.textProperty().addListener(Listener);
        senderTextfield.textProperty().addListener(Listener);
        recipientTextfield.textProperty().addListener(Listener);
        Optional<ButtonType> result = TransferDialog.showAndWait();
        String localdate = java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        double amount = Double.parseDouble(amountTextfield.getText());
        String description = descriptionTextfield.getText();
        String sender = senderTextfield.getText();
        String recipient = recipientTextfield.getText();
        if (result.get() == ButtonType.OK) {
            if (!Objects.equals(sender, main_application.derAccount) ||
                    !Objects.equals(recipient, main_application.getDerAccount(recipient))) {
                alertAccountdoesnotexist();
            }
            if (Objects.equals(sender, main_application.derAccount) &&
                    Objects.equals(recipient, main_application.getDerAccount(recipient))) {
                main_application.getBank().addTransaction(main_application.derAccount, main_application.initOutgoing(
                        localdate, amount, description, sender, recipient));
                main_application.getBank().addTransaction(recipient, main_application.initIncoming(localdate, amount,
                        description, sender, recipient));
                handleCreation();
                //handleCreationRecipient();
            }
        } else {
            TransferDialog.close();
        }
    }

    /**
     * Fehlermeldung Dialog, wenn account nicht existiert.
     */
    public void alertAccountdoesnotexist() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Es ist ein Fehler aufgetreten");
        alert.setContentText("Account existiert nicht");
        alert.showAndWait();
    }

    /**
     * Fehlermeldung Dialog, wenn Transaktion existiert.
     */
    public void alertTransactionExistAlready() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Es ist ein Fehler aufgetreten");
        alert.setContentText("Transaktion existiert schon");
        alert.showAndWait();
    }

    public void alertTransactionAttribute(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Es ist ein Fehler aufgetreten");
        alert.setContentText("Bitte eine Zahl eingeben");
        alert.showAndWait();
    }
}