package bank;

import bank.exceptions.TransactionAttributeException;

public class OutgoingTransfer extends Transfer {
    /**
     * Konstruktor für die Basisattribute {@link Transaction#Transaction(String, double, String)}
     *
     * @param date        {@link Transaction#getDate()}
     * @param amount      {@link Transaction#getAmount()}
     * @param description {@link Transaction#getDescription()}
     * @throws TransactionAttributeException if the validation check for certain attributes fail
     */
    public OutgoingTransfer(String date, double amount, String description) throws TransactionAttributeException {
        super(date, amount, description);
    }

    /**
     * Konstruktor
     *
     * @param transfer  {@link Transfer}
     * @param sender    {@link Transfer#getSender()}
     * @param recipient {@link Transfer#getRecipient()}
     * @throws TransactionAttributeException if the validation check for certain attributes fail
     */
    public OutgoingTransfer(Transfer transfer, String sender, String recipient) throws TransactionAttributeException {
        super(transfer, sender, recipient);
    }

    /**
     * überschreibt die Methode {@link Transfer#calculate()}
     *
     * @return double value des negativen Betrages
     */
    @Override
    public double calculate() {
        return -(super.calculate());
    }
}
