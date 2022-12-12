package bank;

import bank.exceptions.TransactionAttributeException;

public class Payment extends Transaction {
    /**
     * Gibt die Zinsen an, die bei einer Einzahlung anfallen.
     */
    private double incomingInterest;
    /**
     * Gibt die Zinsen an, die bei einer Auszahlung anfallen.
     */
    private double outgoingInterest;

    /**
     * Konstruktor für alle Basisattribute {@link Transaction#Transaction(String, double, String)}.
     *
     * @param date        {@link Transaction#getDate()}
     * @param amount      {@link Transaction#getAmount()}
     * @param description {@link Transaction#getDescription()}
     */
    public Payment(String date, double amount, String description) throws TransactionAttributeException {
        super(date, amount, description);
    }

    /**
     * Konstruktor für die vererbten und klassen eigenen Attribute.
     *
     * @param payment          {@link Payment#Payment(String, double, String)}
     * @param incomingInterest {@link Payment#incomingInterest}
     * @param outgoingInterest {@link Payment#outgoingInterest}
     */
    public Payment(Payment payment, double incomingInterest, double outgoingInterest) throws
            TransactionAttributeException {
        this(payment.getDate(), payment.getAmount(), payment.getDescription());
        setOutgoingInterest(outgoingInterest);
        setIncomingInterest(incomingInterest);
    }

    /**
     * Copy-Konstruktor, der alle Attributwerte eines bestehenden Objekts in ein neues Objekt kopiert.
     *
     * @param payment bestehendes Objekt {@link Payment#Payment(Payment, double, double)}, was kopiert werden soll
     */
    public Payment(Payment payment) throws TransactionAttributeException {
        super(payment);
        setIncomingInterest(payment.getIncomingInterest());
        setOutgoingInterest(payment.getOutgoingInterest());
    }

    /**
     * Methode zum Setzen des Attributs {@link Transaction#amount}.
     *
     * @param amount {@link Transaction#amount}
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * gibt das Attribut {@link Payment#incomingInterest} zurück.
     *
     * @return double {@link Payment#incomingInterest}
     */
    public double getIncomingInterest() {
        return this.incomingInterest;
    }

    /**
     * Methode zum Setzen des Attributs {@link Payment#incomingInterest}.
     *
     * @param incomingInterest {@link Payment#incomingInterest}
     */
    public void setIncomingInterest(double incomingInterest) throws TransactionAttributeException {
        if (incomingInterest < 0 || incomingInterest > 1) {
            throw new TransactionAttributeException("Exception thrown: Fehlerhafte Zinseingabe");
        }
        this.incomingInterest = incomingInterest;
    }

    /**
     * gibt das Attribut {@link Payment#outgoingInterest} zurück.
     *
     * @return double {@link Payment#outgoingInterest}
     */
    public double getOutgoingInterest() {
        return this.outgoingInterest;
    }

    /**
     * Methode zum Setzen des Attributs {@link Payment#outgoingInterest}.
     *
     * @param outgoingInterest {@link Payment#outgoingInterest}
     */
    public void setOutgoingInterest(double outgoingInterest) throws TransactionAttributeException {
        if (outgoingInterest < 0 || outgoingInterest > 1) {
            throw new TransactionAttributeException("Exception thrown: Fehlerhafte Zinseingabe");
        }
        this.outgoingInterest = outgoingInterest;
    }

    /**
     * Implementiert calculate Methode des Interfaces {@link CalculateBill#calculate()}. indem bei einem positiven
     * Betrag, der Wert des incomingInterest-Attributs prozentual abgezogen wird und bei einem negativen Betrag,
     * der Wert des outgoingInterest prozentual hinzuaddiert.
     *
     * @return double value des veränderten Betrages, wenn eingehende oder ausgehende Zinsen berechnet wurden
     */
    @Override
    public double calculate() {
        if (this.getAmount() > 0) {
            return this.getAmount() - (this.getAmount() * this.getIncomingInterest());
        } else if (this.getAmount() < 0) {
            return this.getAmount() + (this.getAmount() * this.getOutgoingInterest());
        }
        return this.getAmount();
    }

    /**
     * Überschriebene toString() Methode der Klasse {@link Object#toString()}.
     * Gibt einen String zurück, der alle Basisattribute mit ihren Werten aufzeigt.
     *
     * @return String, der die Attribute der Klasse {@link Payment} auflistet
     */
    @Override
    public String toString() {
        return (super.toString() + "Zinsen bei der Einzahlung: " + this.getIncomingInterest() +
                "%\n" + "Zinsen bei der Auszahlung: " + this.getOutgoingInterest() + "%\n");
    }

    /**
     * Überschriebene equals() Methode der Klasse {@link Object#equals(Object)}.
     * Gibt true zurück, wenn das aktuelle Objekt auf Gleichheit mit dem übergebenem Objekt überprüft wurde und false,
     * wenn nicht.
     *
     * @param obj Objekt dessen Attribute auf Gleichheit zu denen des aktuellen Objekts zu prüfen ist
     * @return true, wenn alle Attribute der Klasse {@link Payment} mit dem übergebenem Objekt übereinstimmen,
     * false, wenn die Attribute der Klasse {@link Payment} mit dem übergebenem Objekt nicht übereinstimmen
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Payment payment) {
            return super.equals(payment) && (this.getIncomingInterest() == payment.getIncomingInterest()) &&
                    (this.getOutgoingInterest() == payment.getOutgoingInterest());
        }
        return false;
    }
}
