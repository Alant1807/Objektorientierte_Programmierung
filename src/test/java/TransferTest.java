import bank.*;
import bank.Transfer;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TransferTest {
    private Transfer transfer;
    private Transfer transfer1;
    private Transfer copyTransfer;

    @BeforeEach
    void setup() throws TransactionAttributeException {
        transfer = new Transfer("05.12.2022", 1000, "Vorgang");
        transfer1 = new Transfer(transfer, "Alan", "Yusuf");
        copyTransfer = new Transfer(transfer1);
    }

    @Test
    public void TestConstructors() throws TransactionAttributeException {
        assertEquals(1000, transfer1.getAmount());
        assertEquals("05.12.2022", transfer1.getDate());
        assertEquals("Alan", copyTransfer.getSender());
        assertEquals("Yusuf", copyTransfer.getRecipient());
    }

    @Test
    public void testCalculate() throws TransactionAttributeException {
        IncomingTransfer incomingTransfer = new IncomingTransfer(transfer, "Alan", "Yusuf");
        OutgoingTransfer outgoingTransfer = new OutgoingTransfer(transfer, "Alan", "Yusuf");
        assertEquals(incomingTransfer.getAmount(), incomingTransfer.calculate());
        assertEquals(-(outgoingTransfer.getAmount()), outgoingTransfer.calculate());

    }

    @Test
    public void testEquals() throws TransactionAttributeException {
        Transfer vergleichsObjekt1 = new Transfer(copyTransfer, "Alan", "Yusuf");
        Transfer vergleichsObjekt1Kopie = new Transfer(copyTransfer, "Alan", "Yusuf");
        Transfer vergleichsObjekt2 = new Transfer(copyTransfer, "Alan", "Rekan");
        Transfer vergleichsObjekt3 = new Transfer(copyTransfer, "Alan", "Ali");
        Transfer vergleichsObjekt4 = new Transfer(copyTransfer, "Alan", "Cango");
        Transfer vergleichsObjekt5 = new Transfer(copyTransfer, "Alan", "Talabanie");
        Transfer vergleichsObjekt6 = new Transfer(copyTransfer, "Alan", "Ruffy");

        assertTrue(vergleichsObjekt1.equals(vergleichsObjekt1));
        assertTrue(vergleichsObjekt1.equals(vergleichsObjekt1Kopie));
        assertFalse(vergleichsObjekt1.equals(vergleichsObjekt2));
        assertFalse(vergleichsObjekt1.equals(vergleichsObjekt3));
        assertFalse(vergleichsObjekt1.equals(vergleichsObjekt4));
        assertFalse(vergleichsObjekt1.equals(vergleichsObjekt5));
        assertFalse(vergleichsObjekt1.equals(vergleichsObjekt6));
    }

    @Test
    public void testToString() {
        String excepted = "\nDatum: " + transfer1.getDate() + "\n" + "Geldmenge: " +
                transfer1.getAmount() + "\n" + "Beschreibung: " + transfer1.getDescription() + "\n" +
                "Sender: " + transfer1.getSender() +
                "\n" + "Empfänger: " + transfer1.getRecipient() + "\n";
        assertEquals(excepted, transfer1.toString());
    }

    @Test
    public void testException() throws TransactionAttributeException {
        Exception exception = assertThrows(TransactionAttributeException.class, () -> {
            Transfer transfer2 = new Transfer("12.2.2022", -100, "überweisen");
            Transfer transfer3 = new Transfer(transfer2, "Alan", "Yusuf");
        });
        assertEquals("Exception thrown: Es können keine negativen Geldmengen überwiesen werden", exception.getMessage());
        assertDoesNotThrow(() -> { transfer1.setAmount(100); } );
    }
}
