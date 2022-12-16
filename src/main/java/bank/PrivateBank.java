package bank;

import bank.exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PrivateBank implements Bank {
    /**
     * Dieses Attribut soll den Namen der privaten Bank repräsentieren.
     */
    private String name;
    /**
     * Gibt die Zinsen an, die bei einer Einzahlung anfallen.
     */
    private double incomingInterest;
    /**
     * Gibt die Zinsen an, die bei einer Auszahlung anfallen.
     */
    private double outgoingInterest;
    /**
     * Verknüpfung der Konten mit Transaktionen.
     * Der Schlüssel steht für den Namen des Kontos und die Values für die verknüpften Transaktionen.
     */
    public LinkedHashMap<String, List<Transaction>> accountsToTransactions = new LinkedHashMap<>();

    private String directoryName;

    /**
     * Setzt den Namen der Bank.
     *
     * @param name {@link PrivateBank#name}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zugriffsmethode eines privaten Attributs name.
     *
     * @return String {@link PrivateBank#name}
     */
    public String getName() {
        return this.name;
    }

    /**
     * Zugriffsmethode eines privaten Attributs incomingInterest.
     *
     * @return double {@link PrivateBank#incomingInterest}
     */
    public double getIncomingInterest() {
        return this.incomingInterest;
    }

    /**
     * Setzt die anfallenden Zinsen bei einer Einzahlung.
     *
     * @param incomingInterest {@link PrivateBank#incomingInterest}
     * @throws TransactionAttributeException if the validation check for certain attributes fail
     */
    public void setIncomingInterest(double incomingInterest) throws TransactionAttributeException {
        if (incomingInterest < 0 || incomingInterest > 1) {
            throw new TransactionAttributeException("Exception thrown: Fehlerhafte Zinseingabe");
        }
        this.incomingInterest = incomingInterest;
    }

    /**
     * Zugriffsmethode eines privaten Attributs outgoingInterest.
     *
     * @return double {@link PrivateBank#outgoingInterest}
     */
    public double getOutgoingInterest() {
        return this.outgoingInterest;
    }

    /**
     * Setzt die anfallenden Zinsen bei einer Auszahlung.
     *
     * @param outgoingInterest {@link PrivateBank#outgoingInterest}
     * @throws TransactionAttributeException if the validation check for certain attributes fail
     */
    public void setOutgoingInterest(double outgoingInterest) throws TransactionAttributeException {
        if (outgoingInterest < 0 || outgoingInterest > 1) {
            throw new TransactionAttributeException("Exception thrown: Fehlerhafte Zinseingabe");
        }
        this.outgoingInterest = outgoingInterest;
    }

    /**
     * Allgemeiner Konstruktor
     *
     * @param name             {@link PrivateBank#name}
     * @param incomingInterest {@link PrivateBank#incomingInterest}
     * @param outgoingInterest {@link PrivateBank#outgoingInterest}
     * @throws TransactionAttributeException if the validation check for certain attributes fail
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest, String directoryName) throws
            TransactionAttributeException {
        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
        this.directoryName = directoryName;
        String path = "C:\\Users\\alant\\IdeaProjects\\OOS_Praktikum\\JsonFiles\\" + this.directoryName;
        try {
            Path p = Paths.get(path);
            Files.createDirectory(p);
            System.out.println("Verzeichnis ist erstellt !!!");
        } catch (IOException e) {
            System.err.println("Fehler beim erstellen des Verzeichnisses " + e.getMessage());
        }
        try {
            readAccounts();
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der Datei: " + e.getMessage());
        }
    }

    /**
     * Copy-Konstruktor
     *
     * @param privateBank Instanz dessen Attributwerte übernommen werden sollen
     * @throws TransactionAttributeException if the validation check for certain attributes fail
     */
    public PrivateBank(PrivateBank privateBank) throws TransactionAttributeException {
        this(privateBank.getName(), privateBank.getIncomingInterest(), privateBank.getOutgoingInterest(),
                privateBank.directoryName);
    }

    /**
     * Überschriebene equals() Methode der Klasse Object {@link Object#toString()}
     * gibt true zurück, wenn das aktuelle Objekt auf Gleichheit mit dem übergebenem Objekt überprüft wurde und false,
     * wenn nicht.
     *
     * @param obj Objekt dessen Attribute auf Gleichheit zu denen des aktuellen Objekts zu prüfen ist
     * @return true, wenn alle Attribute der Klasse {@link Transfer} mit dem übergebenem Objekt übereinstimmen,
     * false, wenn die Attribute der Klasse {@link Transfer} mit dem übergebenem Objekt nicht übereinstimmen.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof PrivateBank privateBank) {
            return (this.getName().equals(privateBank.getName())) &&
                    Double.compare(this.getIncomingInterest(), privateBank.getIncomingInterest()) == 0 &&
                    Double.compare(this.getOutgoingInterest()
                            , privateBank.getOutgoingInterest()) == 0 && Objects.equals(this.directoryName,
                    privateBank.directoryName);
        }
        return false;
    }

    /**
     * Überschriebene toString() Methode der Klasse Object {@link Object#toString()}
     *
     * @return String, der die Attribute der Klasse PrivateBank auflistet.
     */
    @Override
    public String toString() {
        StringBuilder map_elemente = new StringBuilder();
        for (Map.Entry<String, List<Transaction>> entry : accountsToTransactions.entrySet()) {
            map_elemente.append(entry.getKey()).append(" ==> [ ");
            for (Transaction transaction : entry.getValue()) {
                map_elemente.append(transaction.getDate()).append(", ").append(transaction.getDescription()).append(", ").
                        append(transaction.getAmount()).append("€ ");
            }
            map_elemente.append("]\n");
        }
        return ("Name: " + this.getName() + "\n" + "incomingInterest: " + this.getIncomingInterest() + "\n"
                + "outgoingInterest: " + this.getOutgoingInterest() + "\n" + "Transaktionen:\n" + map_elemente);
    }

    public String getAccountName(String acc) {
        for (Map.Entry<String, List<Transaction>> entry : accountsToTransactions.entrySet()) {
            if (Objects.equals(entry.getKey(), acc)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * @param account the account to be added.
     * @throws AccountAlreadyExistsException if the account already exists
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException {
        if (this.accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException();
        }
        this.accountsToTransactions.put(account, new ArrayList<>());
        try {
            writeAccount(account);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions) throws
            AccountAlreadyExistsException, TransactionAlreadyExistException {
        if (this.accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException();
        } else if (this.accountsToTransactions.containsValue(transactions)) {
            throw new TransactionAlreadyExistException();
        /*else if ((this.getOutgoingInterest() < 0 || this.getOutgoingInterest() > 1)
                || this.getIncomingInterest() < 0 || this.getIncomingInterest() > 1) {
            throw new TransactionAttributeException("Exception thrown: Fehlerhafte Zinseingabe");*/
        }
        this.accountsToTransactions.put(account, transactions);
        try {
            writeAccount(account);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    @Override
    public void addTransaction(String account, Transaction transaction) throws
            TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        if (!this.accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException();
        } else if (containsTransaction(account, transaction)) {
            throw new TransactionAlreadyExistException();
        }
        if (transaction instanceof Payment payment) {
            payment.setIncomingInterest(this.getIncomingInterest());
            payment.setOutgoingInterest(this.getOutgoingInterest());
        }
        this.accountsToTransactions.get(account).add(transaction);
        try {
            writeAccount(account);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     */
    @Override
    public void removeTransaction(String account, Transaction transaction) throws
            AccountDoesNotExistException, TransactionDoesNotExistException {
        if (!this.accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException();
        } else if (!containsTransaction(account, transaction)) {
            throw new TransactionDoesNotExistException();
        }
        this.accountsToTransactions.get(account).remove(transaction);
        try {
            writeAccount(account);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     * @return boolean true if {@link PrivateBank#accountsToTransactions} contains transaction
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        return this.accountsToTransactions.get(account).contains(transaction);
    }

    /**
     * @param account the selected account
     * @return double value of the current account balance
     */
    @Override
    public double getAccountBalance(String account) {
        List<Transaction> transactions = this.getTransactions(account);
        double balance = 0;
        for (Transaction t : transactions) {
            if (t instanceof Transfer transfer) {
                balance += transfer.calculate();
            }
        }
        return balance;
    }

    /**
     * @param account the selected account
     * @return List of Transactions per key account
     */
    @Override
    public List<Transaction> getTransactions(String account) {
        return new ArrayList<>(this.accountsToTransactions.get(account));
    }

    /**
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return sorted List.
     */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {
        List<Transaction> transactions = this.getTransactions(account);
        if (asc) {
            transactions.sort(Comparator.comparingDouble(Transaction::getAmount));
        } else {
            Collections.reverse(transactions);
        }
        return transactions;
    }

    /**
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return List of Transaction per Transaction type
     */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        List<Transaction> transactions = this.getTransactions(account);
        if (positive) {
            transactions.removeIf((Transaction transaction) -> transaction.getAmount() < 0);
        } else {
            transactions.removeIf((Transaction transaction) -> transaction.getAmount() > 0);
        }
        return transactions;
    }

    /**
     * Diese Methode soll alle vorhandenen Konten vom Dateisystem lesen und im PrivateBank-Objekt
     * (genauer: im Klassenattribut accountsToTransactions) zur Verfügung stellen
     *
     * @throws IOException
     * @throws TransactionAttributeException
     */
    void readAccounts() throws IOException, TransactionAttributeException {
        String path = "C:\\Users\\alant\\IdeaProjects\\OOS_Praktikum\\JsonFiles\\" + this.directoryName;
        if (new File(path).listFiles() == null) {
            return;
        }
        Gson gsonBuilder = new GsonBuilder().registerTypeAdapter(Transaction.class, new JsonCustom()).
                setPrettyPrinting().create();
        for (File file : Objects.requireNonNull(new File(path).listFiles())) {
            String account = file.getName().substring(0, file.getName().length() - 5);
            if (!accountsToTransactions.containsKey(account)) {
                accountsToTransactions.put(account, new ArrayList<>());
            }
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(currentLine).append("\n");
            }
            JsonArray jsonElements = new Gson().fromJson(stringBuilder.toString(), JsonArray.class);
            for (JsonElement jsonElement : jsonElements) {
                Transaction transaction = gsonBuilder.fromJson(jsonElement.toString(), Transaction.class);
                if (accountsToTransactions.containsKey(account) && !accountsToTransactions.get(account).contains(transaction)) {
                    accountsToTransactions.get(account).add(transaction);
                    if (transaction instanceof Payment payment) {
                        payment.setIncomingInterest(this.getIncomingInterest());
                        payment.setOutgoingInterest(this.getOutgoingInterest());
                    }
                }
            }
        }
    }

    /**
     * Diese Methode soll das angegebene Konto im Dateisystem persistieren.
     *
     * @param account - Account dessen Konto Daten persistiert werden sollen
     * @throws IOException
     */
    void writeAccount(String account) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(IncomingTransfer.class, new JsonCustom()).
                registerTypeAdapter(OutgoingTransfer.class, new JsonCustom()).
                registerTypeAdapter(Payment.class, new JsonCustom()).setPrettyPrinting().create();
        List<Transaction> transactionList = getTransactions(account);
        String jsonfile = gson.toJson(transactionList);
        FileWriter fileWriter = new FileWriter("C:\\Users\\alant\\IdeaProjects\\OOS_Praktikum\\JsonFiles\\" +
                this.directoryName + "\\" + account + ".json");
        fileWriter.write(jsonfile);
        fileWriter.close();
    }

    public void deleteAccount(String account)
            throws AccountDoesNotExistException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException();
        }
        accountsToTransactions.remove(account);
        File file = new File("C:\\Users\\alant\\IdeaProjects\\OOS_Praktikum\\JsonFiles\\" + this.directoryName + "\\" + account + ".json");
        if (file.delete()) {
            System.out.println("Datei wurde erfolgreich entfernt");
        }
    }

    public List<String> getAllAccounts(){
        List<String> accounts = new ArrayList<String>(this.accountsToTransactions.keySet());
        return accounts;
    }
}

