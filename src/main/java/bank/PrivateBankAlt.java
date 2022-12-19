package bank;

import bank.exceptions.*;

import java.util.*;

public class PrivateBankAlt implements Bank {
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
    public Map<String, List<Transaction>> accountsToTransactions = new LinkedHashMap<>();

    /**
     * Setzt den Namen der Bank.
     *
     * @param name {@link PrivateBankAlt#name}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zugriffsmethode eines privaten Attributs name.
     *
     * @return String {@link PrivateBankAlt#name}
     */
    public String getName() {
        return this.name;
    }

    /**
     * Zugriffsmethode eines privaten Attributs incomingInterest.
     *
     * @return double {@link PrivateBankAlt#incomingInterest}
     */
    public double getIncomingInterest() {
        return this.incomingInterest;
    }

    /**
     * Setzt die anfallenden Zinsen bei einer Einzahlung.
     *
     * @param incomingInterest {@link PrivateBankAlt#incomingInterest}
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
     * @return double {@link PrivateBankAlt#outgoingInterest}
     */
    public double getOutgoingInterest() {
        return this.outgoingInterest;
    }

    /**
     * Setzt die anfallenden Zinsen bei einer Auszahlung.
     *
     * @param outgoingInterest {@link PrivateBankAlt#outgoingInterest}
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
     * @param name             {@link PrivateBankAlt#name}
     * @param incomingInterest {@link PrivateBankAlt#incomingInterest}
     * @param outgoingInterest {@link PrivateBankAlt#outgoingInterest}
     * @throws TransactionAttributeException if the validation check for certain attributes fail
     */
    public PrivateBankAlt(String name, double incomingInterest, double outgoingInterest)
            throws TransactionAttributeException {
        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }

    /**
     * Copy-Konstruktor
     *
     * @param privateBankAlt Instanz dessen Attributwerte übernommen werden sollen
     * @throws TransactionAttributeException if the validation check for certain attributes fail
     */
    public PrivateBankAlt(PrivateBankAlt privateBankAlt) throws TransactionAttributeException {
        this(privateBankAlt.getName(), privateBankAlt.getIncomingInterest(), privateBankAlt.getOutgoingInterest());
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
        if (obj instanceof PrivateBankAlt privateBankAlt) {
            return (this.getName().equals(privateBankAlt.getName())) &&
                    Double.compare(this.getIncomingInterest(), privateBankAlt.getIncomingInterest()) == 0 &&
                    Double.compare(this.getOutgoingInterest()
                            , privateBankAlt.getOutgoingInterest()) == 0;
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
            map_elemente.append(entry.getKey()).append(" ==> [");
            for (Transaction transaction : entry.getValue()) {
                map_elemente.append(transaction.getDate()).append(" ").append(transaction.getDescription()).append(" ").
                        append(transaction.getAmount()).append("Euro");
            }
            map_elemente.append("]\n");
        }
        return ("Name: " + this.getName() + "\n" + "incomingInterest: " + this.getIncomingInterest() + "\n"
                + "outgoingInterest: " + this.getOutgoingInterest() + "\n" + "Transaktionen:\n" + map_elemente);
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
        this.accountsToTransactions.put(account, new ArrayList<Transaction>());
    }

    /**
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
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
    }

    /**
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    @Override
/**
 * @param account     the account from which the transaction is removed
 * @param transaction the transaction which is removed from the specified account
 * @throws AccountDoesNotExistException     if the specified account does not exist
 * @throws TransactionDoesNotExistException if the transaction cannot be found
 */
    public void addTransaction(String account, Transaction transaction) throws
            TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        if (!this.accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException();
        } else if (containsTransaction(account, transaction)) {
            throw new TransactionAlreadyExistException();
        }
        if (transaction instanceof Payment payment) {
            setIncomingInterest(payment.getIncomingInterest());
            setOutgoingInterest(payment.getOutgoingInterest());
        }
        this.accountsToTransactions.get(account).add(transaction);
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
    }

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
                if (transfer.getSender().equals(account)) {
                    balance -= transfer.getAmount();
                } else {
                    balance += transfer.getAmount();
                }
            }
            if (t instanceof Payment payment) {
                balance += payment.getAmount();
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
        return new ArrayList<Transaction>(this.accountsToTransactions.get(account));
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
            transactions.removeIf((Transaction transaction) -> {
                return transaction.getAmount() > 0;
            });
        } else {
            transactions.removeIf((Transaction transaction) -> {
                return transaction.getAmount() < 0;
            });
        }
        return transactions;
    }
}