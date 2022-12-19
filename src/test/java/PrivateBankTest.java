import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrivateBankTest {
    private PrivateBank privateBank;
    private PrivateBank copyprivateBank;
    private Payment payment;
    private Payment payment1;
    private IncomingTransfer incomingTransfer;
    private IncomingTransfer incomingTransfer1;
    private OutgoingTransfer outgoingTransfer;
    private OutgoingTransfer outgoingTransfer1;
    private OutgoingTransfer outgoingTransfer3;
    private OutgoingTransfer outgoingTransfer4;
    List<Transaction> list;
    String acc;

    @BeforeEach
    public void init() throws TransactionAttributeException {
        privateBank = new PrivateBank("test", 0.05, 1, "testfiles");
        copyprivateBank = privateBank;
        acc = "testacc";
        outgoingTransfer = new OutgoingTransfer("22.11.2022", 500, "Outgoing Transfer");
        outgoingTransfer1 = new OutgoingTransfer(outgoingTransfer, "Alan", "Rekan");
        outgoingTransfer3 = new OutgoingTransfer("22.11.2022",600,"ifif");
        outgoingTransfer4 = new OutgoingTransfer(outgoingTransfer3,"Alan","Rekan");
        list = new ArrayList<Transaction>(Arrays.asList(outgoingTransfer1, outgoingTransfer4));
    }

    @Test
    public void TestCreateAccount() throws AccountAlreadyExistsException {
        assertAll(acc, () -> {
            privateBank.createAccount(acc);
            privateBank.getAccountName(acc);
        });
        Exception exception = assertThrows(AccountAlreadyExistsException.class, () -> {
            privateBank.createAccount(acc);
        });
        assertEquals("Exception thrown: Account existiert schon.", exception.getMessage());
    }

    @Test
    public void TestCreateAccountWithTransactions() throws TransactionAlreadyExistException,
            AccountAlreadyExistsException {
        privateBank.createAccount(acc, list);
        assertEquals(acc, privateBank.getAccountName(acc));
        assertEquals(list, privateBank.getTransactions(acc));
        Exception exception = assertThrows(TransactionAlreadyExistException.class, () -> {
            privateBank.createAccount("tmpnew", list);
        });
        assertEquals("Exception thrown: Transaktion existiert schon.", exception.getMessage());
        Exception exception1 = assertThrows(AccountAlreadyExistsException.class, () -> {
            IncomingTransfer incomingTransfer2 = new IncomingTransfer("test", 500, "test");
            IncomingTransfer incomingTransfer3 = new IncomingTransfer(incomingTransfer2, "test", "test");
            List<Transaction> listtmp = new ArrayList<Transaction>(List.of(incomingTransfer3));
            privateBank.createAccount(acc, listtmp);
        });
        assertEquals("Exception thrown: Account existiert schon.", exception1.getMessage());
    }

    @Test
    public void TestAddTransactions() throws TransactionAlreadyExistException,
            AccountDoesNotExistException, TransactionAttributeException, AccountAlreadyExistsException {
        privateBank.createAccount(acc);
        privateBank.addTransaction(acc, outgoingTransfer1);
        privateBank.addTransaction(acc, outgoingTransfer4);
        assertTrue(privateBank.containsTransaction(acc, outgoingTransfer4));
        Exception exception = assertThrows(TransactionAlreadyExistException.class, () -> {
            privateBank.addTransaction(acc, outgoingTransfer4);
        });
        assertEquals("Exception thrown: Transaktion existiert schon.", exception.getMessage());
        Exception exception1 = assertThrows(AccountDoesNotExistException.class, () -> {
            privateBank.addTransaction("tmpacc", outgoingTransfer4);
        });
        assertEquals("Exception thrown: Account existiert nicht.", exception1.getMessage());
        Exception exception2 = assertThrows(TransactionAttributeException.class, () -> {
            IncomingTransfer incomingTransfer2 = new IncomingTransfer("test", -100, "test");
            IncomingTransfer incomingTransfer3 = new IncomingTransfer(incomingTransfer2, "test", "test");
            privateBank.addTransaction(acc, incomingTransfer3);
        });
        assertEquals("Exception thrown: Es können keine negativen Geldmengen überwiesen werden", exception2.getLocalizedMessage());
    }

    @Test
    public void TestRemoveTransaction() throws AccountDoesNotExistException, TransactionDoesNotExistException,
            AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {
        privateBank.createAccount(acc);
        privateBank.addTransaction(acc, outgoingTransfer1);
        privateBank.addTransaction(acc, outgoingTransfer4);
        privateBank.removeTransaction(acc, outgoingTransfer1);
        privateBank.removeTransaction(acc, outgoingTransfer4);
        assertFalse(privateBank.containsTransaction(acc, outgoingTransfer1));
        assertFalse(privateBank.containsTransaction(acc, outgoingTransfer4));
        Exception exception = assertThrows(AccountDoesNotExistException.class, () -> {
            privateBank.removeTransaction("newacc", outgoingTransfer1);
        });
        assertEquals("Exception thrown: Account existiert nicht.", exception.getMessage());
        Exception exception1 = assertThrows(TransactionDoesNotExistException.class, () -> {
            privateBank.removeTransaction(acc, outgoingTransfer4);
        });
        assertEquals("Exception thrown: Transaktion existiert nicht.", exception1.getMessage());
    }

    @Test
    public void TestContainsTransaction() throws TransactionAlreadyExistException, AccountAlreadyExistsException {
        privateBank.createAccount(acc, list);
        assertTrue(privateBank.containsTransaction(acc, outgoingTransfer1));
        assertTrue(privateBank.containsTransaction(acc, outgoingTransfer4));
    }

    @Test
    public void TestEquals() throws TransactionAttributeException, AccountAlreadyExistsException {
        PrivateBank privateBank1 = new PrivateBank(privateBank);
        PrivateBank privateBank1kopie = new PrivateBank(privateBank);
        PrivateBank privateBank2 = new PrivateBank("test", 0.03, 0.5, "testtmp");
        PrivateBank privateBank3 = new PrivateBank("test", 0, 0.5, "testtmp");
        PrivateBank privateBank4 = new PrivateBank("test", 0.03, 1, "testtmp");
        privateBank.createAccount(acc);
        assertEquals(privateBank1kopie, privateBank1);
        assertFalse(privateBank1.equals(privateBank2));
        assertFalse(privateBank1.equals(privateBank3));
        assertFalse(privateBank1.equals(privateBank4));
    }

    @AfterEach
    public void TestCleanup() throws AccountDoesNotExistException {
        privateBank.deleteAccount(acc);
    }
}
