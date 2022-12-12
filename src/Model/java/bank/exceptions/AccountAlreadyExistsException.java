package bank.exceptions;

public class AccountAlreadyExistsException extends Exception{
    @Override
    public String getMessage() {
        return "Exception thrown: Account existiert schon.";
    }
}
