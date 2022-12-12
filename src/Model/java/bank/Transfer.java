package bank;

import bank.exceptions.TransactionAttributeException;

public class Transfer extends Transaction {
    /**
     * Die Person, die das Geld überweist.
     */
    private String sender;
    /**
     * Der Empfänger, der das Geld empfängt.
     */
    private String recipient;

    /**
     * Konstruktor für alle Basisattribute {@link Transaction#Transaction(String, double, String)}.
     *
     * @param date        {@link Transaction#getDate()}
     * @param amount      {@link Transaction#amount}
     * @param description {@link Transaction#getDescription()}
     */
    public Transfer(String date, double amount, String description) throws TransactionAttributeException {
        super(date, amount, description);
    }

    /**
     * Konstruktor für die vererbten und klassen eigenen Attribute.
     *
     * @param transfer  {@link Transfer#Transfer(String, double, String)}
     * @param sender    {@link Transfer#sender}
     * @param recipient {@link Transfer#recipient)
     */
    public Transfer(Transfer transfer, String sender, String recipient) throws TransactionAttributeException {
        this(transfer.getDate(), transfer.getAmount(), transfer.getDescription());
        setSender(sender);
        setRecipient(recipient);
    }

    /**
     * Copy-Konstruktor, der alle Attributwerte eines bestehenden Objekts in ein neues Objekt kopiert.
     *
     * @param transfer bestehendes Objekt {@link Transfer#Transfer(Transfer, String, String)}
     */
    public Transfer(Transfer transfer) throws TransactionAttributeException {
        super(transfer);
        setSender(transfer.getSender());
        setRecipient(transfer.getRecipient());
    }

    /**
     * Methode zum Setzen des Attributs {@link Transaction#amount}.
     *
     * @param amount {@link Transaction#amount}
     */
    public void setAmount(double amount) throws TransactionAttributeException {
        if (amount < 0) {
            throw new TransactionAttributeException("Exception thrown: Es können keine negativen Geldmengen überwiesen werden");
        }
        this.amount = amount;
    }

    /**
     * gibt das Attribut {@link Transfer#sender}.
     *
     * @return String {@link Transfer#sender}
     */
    public String getSender() {
        return this.sender;
    }

    /**
     * Methode zum Setzen des Attributs {@link Transfer#sender}.
     *
     * @param sender {@link Transfer#sender}
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * gibt das Attribut {@link Transfer#recipient}.
     *
     * @return String {@link Transfer#recipient}
     */
    public String getRecipient() {
        return this.recipient;
    }

    /**
     * Methode zum Setzen des Attributs {@link Transfer#recipient}.
     *
     * @param recipient {@link Transfer#recipient}
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * Implementiert calculate Methode des Interfaces {@link CalculateBill#calculate()}.
     *
     * @return double value des unveränderten, da keine ausgehenden oder eingehenden Zinsen berechnet werden müssen
     */
    @Override
    public double calculate() {
        return this.getAmount();
    }

    /**
     * Überschriebene toString() Methode der Klasse {@link Object#toString()}.
     * Gibt einen String zurück, der alle Basisattribute mit ihren Werten aufzeigt.
     *
     * @return String, der die Attribute der Klasse {@link Transfer} auflistet
     */
    @Override
    public String toString() {
        return (super.toString() + "Sender: " + this.getSender() +
                "\n" + "Empfänger: " + this.getRecipient() + "\n");
    }

    /**
     * Überschriebene equals() Methode der Klasse {@link Object#equals(Object)}.
     * Gibt true zurück, wenn das aktuelle Objekt auf Gleichheit mit dem übergebenem Objekt überprüft wurde und false,
     * wenn nicht.
     *
     * @param obj Objekt dessen Attribute auf Gleichheit zu denen des aktuellen Objekts zu prüfen ist
     * @return true, wenn alle Attribute der Klasse {@link Transfer} mit dem übergebenem Objekt übereinstimmen,
     * false, wenn die Attribute der Klasse {@link Transfer} mit dem übergebenem Objekt nicht übereinstimmen
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Transfer transfer) {
            return super.equals(transfer) && (this.getSender().equals(transfer.getSender())) &&
                    (this.getRecipient().equals(transfer.getRecipient()));
        }
        return false;
    }
}
