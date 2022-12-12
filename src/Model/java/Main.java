import bank.*;
import bank.exceptions.*;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws TransactionAttributeException, AccountAlreadyExistsException,
            TransactionAlreadyExistException, AccountDoesNotExistException {
        //PrivateBank privateBank = new PrivateBank("Alan",0.05,0.1,"testfiles");
        PrivateBank privateBanktmp = new PrivateBank("test",1,1,"testfiles");
       /* String acc = "Alan";
        privateBank.createAccount(acc);
        OutgoingTransfer outgoingTransfer = new OutgoingTransfer("22.11.2022", 500, "Outgoing Transfer");
        OutgoingTransfer outgoingTransfer1 = new OutgoingTransfer(outgoingTransfer, "Alan", "Rekan");
        IncomingTransfer incomingTransfer = new IncomingTransfer("23.11.2022", 1500, "Incoming Transfer");
        IncomingTransfer incomingTransfer1 = new IncomingTransfer(incomingTransfer, "Yusuf", "Alan");
        Payment payment = new Payment("test", 500, "test");
        Payment payment1 = new Payment(payment, 1, 1);
        privateBank.addTransaction(acc, outgoingTransfer1);
        privateBank.addTransaction(acc, incomingTransfer1);
        privateBank.addTransaction(acc, payment1);*/
        System.out.println(privateBanktmp);
    }
}     /*TransactionAlreadyExistException, AccountDoesNotExistException, TransactionDoesNotExistException {
        PrivateBank privateBank = new PrivateBank("Bank1",0,1);
        //PrivateBank privateBank = new PrivateBank("Bank1",0,-3);
        PrivateBank privateBank1 = new PrivateBank(privateBank);
        System.out.println(privateBank1.equals(privateBank));
        privateBank1.setName("Bank2");
        System.out.println(privateBank1.equals(privateBank));
        String acc = "Account Alan";
        privateBank.createAccount(acc);
        //privateBank.createAccount(acc);
        OutgoingTransfer outgoingTransfer = new OutgoingTransfer("22.11.2022",500,"Überweisen");
        OutgoingTransfer outgoingTransfer1 = new OutgoingTransfer(outgoingTransfer,"Alan","Rekan");
        IncomingTransfer incomingTransfer = new IncomingTransfer("23.11.2022",1500,"Eingehend");
        IncomingTransfer incomingTransfer1 = new IncomingTransfer(incomingTransfer,"Yusuf","Alan");
        privateBank.addTransaction(acc,outgoingTransfer1);
        //privateBank.addTransaction(acc,outgoingTransfer1);
        privateBank.addTransaction(acc,incomingTransfer1);
        System.out.println(privateBank);
        List<Transaction> list = new ArrayList<Transaction>(Arrays.asList(outgoingTransfer1,incomingTransfer1));
        String acc2 = "Tom";
        PrivateBank privateBank2 = new PrivateBank("Bank3",0.5,0.04);
        privateBank2.createAccount(acc2,list);
        //privateBank.createAccount(acc2,list);
        System.out.println(privateBank2);
        privateBank2.removeTransaction(acc2,incomingTransfer1);
        //privateBank2.removeTransaction(acc2,incomingTransfer1);
        //privateBank2.removeTransaction("Account tmp",incomingTransfer1);
        System.out.println(privateBank2.containsTransaction(acc2,outgoingTransfer1));
        System.out.println(privateBank.getAccountBalance(acc));
        System.out.println(privateBank.getTransactions(acc));
        System.out.println(privateBank.getTransactionsSorted(acc,false));
        System.out.println(privateBank.getTransactionsByType(acc,false));*/

        /*PrivateBankAlt privateBankAlt = new PrivateBankAlt("Bank1",0,1);
        PrivateBankAlt privateBankAlt1 = new PrivateBankAlt(privateBankAlt);
        System.out.println(privateBankAlt1.equals(privateBankAlt));
        privateBankAlt1.setName("Bank2");
        System.out.println(privateBankAlt1.equals(privateBankAlt));
        String acc = "Account Alan";
        privateBankAlt.createAccount(acc);
        Transfer outgoingTransfer = new Transfer("22.11.2022",500,"Überweisen");
        Transfer outgoingTransfer1 = new Transfer(outgoingTransfer,acc,"Account Rekan");
        Transfer incomingTransfer = new Transfer("23.11.2022",1500,"Eingehend");
        Transfer incomingTransfer1 = new Transfer(incomingTransfer,"Account Yusuf",acc);
        privateBankAlt.addTransaction(acc,outgoingTransfer1);
        privateBankAlt.addTransaction(acc,incomingTransfer1);
        System.out.println(privateBankAlt);
        List<Transaction> list = new ArrayList<Transaction>(Arrays.asList(outgoingTransfer1,incomingTransfer1));
        String acc2 = "Account Tom";
        PrivateBankAlt privateBankAlt2 = new PrivateBankAlt("Bank3",0.5,0.04);
        privateBankAlt2.createAccount(acc2,list);
        System.out.println(privateBankAlt2);
        privateBankAlt2.removeTransaction(acc2,incomingTransfer1);
        System.out.println(privateBankAlt2.containsTransaction(acc2,outgoingTransfer1));
        System.out.println(privateBankAlt.getAccountBalance(acc));
        System.out.println(privateBankAlt.getTransactions(acc));
        System.out.println(privateBankAlt.getTransactionsSorted(acc,false));
        System.out.println(privateBankAlt.getTransactionsByType(acc,false));*/
