package bank.exceptions;

public class TransactionAlreadyExistException extends Exception {
    @Override
    public String getMessage() {
        return "Exception thrown: Transaktion existiert schon.";
    }
}
