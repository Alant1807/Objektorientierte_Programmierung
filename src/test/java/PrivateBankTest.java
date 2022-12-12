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
    List<Transaction> list;
    String acc;

    @BeforeEach
    public void init() throws TransactionAttributeException {
        privateBank = new PrivateBank("test", 0.05, 1, "testfiles");
        copyprivateBank = privateBank;
        acc = "testacc";
        outgoingTransfer = new OutgoingTransfer("22.11.2022", 500, "Outgoing Transfer");
        outgoingTransfer1 = new OutgoingTransfer(outgoingTransfer, "Alan", "Rekan");
        incomingTransfer = new IncomingTransfer("23.11.2022", 1500, "Incoming Transfer");
        incomingTransfer1 = new IncomingTransfer(incomingTransfer, "Yusuf", "Alan");
        payment = new Payment("10.12.2022", 5000, "test");
        payment1 = new Payment(payment, 0.02, 1);
        list = new ArrayList<Transaction>(Arrays.asList(outgoingTransfer1, incomingTransfer1, payment1));
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
        privateBank.addTransaction(acc, incomingTransfer1);
        privateBank.addTransaction(acc, payment1);
        assertTrue(privateBank.containsTransaction(acc, incomingTransfer1));
        Exception exception = assertThrows(TransactionAlreadyExistException.class, () -> {
            privateBank.addTransaction(acc, incomingTransfer1);
        });
        assertEquals("Exception thrown: Transaktion existiert schon.", exception.getMessage());
        Exception exception1 = assertThrows(AccountDoesNotExistException.class, () -> {
            privateBank.addTransaction("tmpacc", incomingTransfer1);
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
        privateBank.addTransaction(acc, incomingTransfer1);
        privateBank.addTransaction(acc, payment1);
        privateBank.removeTransaction(acc, incomingTransfer1);
        privateBank.removeTransaction(acc, payment1);
        assertFalse(privateBank.containsTransaction(acc, incomingTransfer1));
        assertFalse(privateBank.containsTransaction(acc, payment1));
        Exception exception = assertThrows(AccountDoesNotExistException.class, () -> {
            privateBank.removeTransaction("newacc", outgoingTransfer1);
        });
        assertEquals("Exception thrown: Account existiert nicht.", exception.getMessage());
        Exception exception1 = assertThrows(TransactionDoesNotExistException.class, () -> {
            privateBank.removeTransaction(acc, incomingTransfer1);
        });
        assertEquals("Exception thrown: Transaktion existiert nicht.", exception1.getMessage());
    }

    @Test
    public void TestContainsTransaction() throws TransactionAlreadyExistException, AccountAlreadyExistsException {
        privateBank.createAccount(acc, list);
        assertTrue(privateBank.containsTransaction(acc, incomingTransfer1));
        assertTrue(privateBank.containsTransaction(acc, outgoingTransfer1));
        assertTrue(privateBank.containsTransaction(acc, payment1));
    }

    @Test
    public void TestGetAccountBalance() throws TransactionAlreadyExistException, AccountAlreadyExistsException {
        privateBank.createAccount(acc, list);
        assertEquals(1000, privateBank.getAccountBalance(acc));
        assertNotEquals(200, privateBank.getAccountBalance(acc), 0.0);
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
