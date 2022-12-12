package bank;

import bank.exceptions.TransactionAttributeException;

public abstract class Transaction implements CalculateBill {
    /**
     * Datum der Ein- oder Auszahlung bzw. einer Überweisung {@link Transaction#getDate()}.
     */
    private String date;
    /**
     * Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung {@link Transaction#getAmount()}.
     */
    protected double amount;
    /**
     * Beschreibung des Vorgangs {@link Transaction#getDescription()}.
     */
    private String description;

    /**
     * Konstruktor für alle Basisattribute.
     *
     * @param date        das Datum der Ein- oder Auszahlung bzw. einer Überweisung
     * @param amount      Die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung
     * @param description die Beschreibung des Vorgangs
     */
    public Transaction(String date, double amount, String description) throws TransactionAttributeException {
        setDate(date);
        setAmount(amount);
        setDescription(description);
    }

    /**
     * Copy-Konstruktor für alle Basisattribute.
     *
     * @param transaction Das Objekt, das kopiert werden soll {@link Transaction}
     */
    public Transaction(Transaction transaction) throws TransactionAttributeException {
        this(transaction.getDate(), transaction.getAmount(), transaction.getDescription());
    }

    /**
     * Methode zum Setzen des Attributs {@link Transaction#date}.
     *
     * @param date {@link Transaction#date}
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * gibt das Attribut {@link Transaction#date} zurück.
     *
     * @return String {@link Transaction#date}
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Methode zum Setzen des Attributs {@link Transaction#amount}.
     *
     * @param amount {@link Transaction#amount}
     */
    public abstract void setAmount(double amount) throws TransactionAttributeException;

    /**
     * gibt das Attribut {@link Transaction#amount} zurück.
     *
     * @return double {@link Transaction#amount}
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * Methode zum Setzen des Attributs {@link Transaction#description}.
     *
     * @param description {@link Transaction#description}
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gibt das Attribut {@link Transaction#description} zurück.
     *
     * @return String {@link Transaction#description}
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Abstrakte Methode {@link CalculateBill#calculate()}, die zur implementation bereitgestellt wird.
     *
     * @return double value
     */
    public abstract double calculate();

    /**
     * Überschriebene toString() Methode der Klasse {@link Object#toString()}.
     * Gibt einen String zurück, der alle Basisattribute mit ihren Werten aufzeigt.
     *
     * @return String, der die Attribute der Klasse {@link Transaction} auflistet
     */
    @Override
    public String toString() {
        return "\nDatum: " + this.getDate() + "\n" + "Geldmenge: " +
                this.getAmount() + "\n" + "Beschreibung: " + this.getDescription() + "\n";
    }

    /**
     * Überschriebene equals() Methode der Klasse {@link Object#equals(Object)}.
     * Gibt true zurück, wenn das aktuelle Objekt auf Gleichheit mit dem übergebenem Objekt überprüft wurde und false,
     * wenn nicht.
     *
     * @param obj Objekt dessen Attribute auf Gleichheit zu denen des aktuellen Objekts zu prüfen ist
     * @return true, wenn alle Attribute der Klasse {@link Transaction} mit dem übergebenem Objekt übereinstimmen,
     * false, wenn die Attribute der Klasse {@link Transaction} mit dem übergebenem Objekt nicht übereinstimmen.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Transaction transaction) {
            return (this.getDate().equals(transaction.getDate())) && (this.getAmount() == transaction.getAmount()) &&
                    (this.getDescription().equals(transaction.getDescription()));
        }
        return false;
    }
}
