package bank.exceptions;

public class TransactionAttributeException extends Exception{
    /*@Override
    public String getMessage() {
        return "Exception thrown: Fehlerhafte Zinseingabe.";
    }*/
    public TransactionAttributeException(String ausgabe){
        super(ausgabe);
    }
}
