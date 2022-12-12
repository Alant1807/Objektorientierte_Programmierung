import bank.Payment;
import bank.Transfer;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {
    Payment payment;
    Payment payment1;
    Payment copyPayment;
    Payment secondP;
    Payment secondP2;

    @BeforeEach
    public void init() throws TransactionAttributeException {
        payment = new Payment("5.12.2022", 1000, "Vorgang");
        payment1 = new Payment(payment, 0.05, 0.1);
        copyPayment = new Payment(payment1);
        secondP = new Payment("test", -1000, "test");
        secondP2 = new Payment(secondP, 0.05, 0.1);
    }

    @Test
    public void TestConstructors() throws TransactionAttributeException {
        assertEquals(1000, payment1.getAmount());
        assertEquals("5.12.2022", copyPayment.getDate());
        assertEquals(0.05, copyPayment.getIncomingInterest());
        assertEquals(0.1, copyPayment.getOutgoingInterest());
    }

    @Test
    public void TestCalculate() {
        assertEquals(950, payment1.calculate());
        assertEquals(-1100, secondP2.calculate());
    }

    @Test
    public void testEquals() throws TransactionAttributeException {
        Payment vergleichsObjekt1 = new Payment(copyPayment, 1, 0);
        Payment vergleichsObjekt1Kopie = new Payment(copyPayment, 1, 0);
        Payment vergleichsObjekt2 = new Payment(copyPayment, 1, 0.8);
        Payment vergleichsObjekt3 = new Payment(copyPayment, 0.4, 0.5);
        Payment vergleichsObjekt4 = new Payment(copyPayment, 1, 0.5);
        Payment vergleichsObjekt5 = new Payment(copyPayment, 0.06, 0);
        Payment vergleichsObjekt6 = new Payment(copyPayment, 1, 1);

        assertTrue(vergleichsObjekt1.equals(vergleichsObjekt1Kopie));
        assertFalse(vergleichsObjekt1.equals(vergleichsObjekt2));
        assertFalse(vergleichsObjekt1.equals(vergleichsObjekt3));
        assertFalse(vergleichsObjekt1.equals(vergleichsObjekt4));
        assertFalse(vergleichsObjekt1.equals(vergleichsObjekt5));
        assertFalse(vergleichsObjekt1.equals(vergleichsObjekt6));
    }

    @Test
    public void testToString() {
        String excepted = "\nDatum: " + payment1.getDate() + "\n" + "Geldmenge: " +
                payment1.getAmount() + "\n" + "Beschreibung: " + payment1.getDescription() + "\n"
                + "Zinsen bei der Einzahlung: " + payment1.getIncomingInterest() +
                "%\n" + "Zinsen bei der Auszahlung: " + payment1.getOutgoingInterest() + "%\n";
        assertEquals(excepted, payment1.toString());
    }

    @Test
    public void testException() {
        Exception exception = assertThrows(TransactionAttributeException.class, () -> {
            Payment payment2 = new Payment("12.2.2022", 1000, "Vorgang");
            Payment payment3 = new Payment(payment2, 3, 1);
        });
        assertEquals("Exception thrown: Fehlerhafte Zinseingabe", exception.getMessage());
        assertDoesNotThrow(() -> {
            payment1.setIncomingInterest(1);
        });
    }
}
