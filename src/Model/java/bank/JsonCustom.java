package bank;

import bank.exceptions.TransactionAttributeException;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Objects;

public class JsonCustom implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {
    /**
     * Gson invokes this call-back method during serialization when it encounters a field of the
     * specified type.
     *
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonSerializationContext#serialize(Object, Type)} method to create JsonElements for any
     * non-trivial field of the {@code src} object. However, you should never invoke it on the
     * {@code src} object itself since that will cause an infinite loop (Gson will call your
     * call-back method again).</p>
     *
     * @param src       the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully generalized version) of the source object.
     * @param context
     * @return a JsonElement corresponding to the specified object.
     */
    @Override
    public JsonElement serialize(Transaction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        String classname = src.getClass().getSimpleName();
        jsonObject.addProperty("CLASSNAME", classname);
        if (src instanceof IncomingTransfer) {
            jsonObject.add("INSTANCE", new Gson().toJsonTree(src));
        } else if (src instanceof OutgoingTransfer) {
            jsonObject.add("INSTANCE", new Gson().toJsonTree(src));
        } else if (src instanceof Payment) {
            jsonObject.add("INSTANCE", new Gson().toJsonTree(src));
        }
        return jsonObject;
    }

    /**
     * Gson invokes this call-back method during deserialization when it encounters a field of the
     * specified type.
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
     * for any non-trivial field of the returned object. However, you should never invoke it on
     * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
     * call-back method again).
     *
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    @Override
    public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String classType = jsonObject.get("CLASSNAME").getAsString();
        JsonObject instance = jsonObject.get("INSTANCE").getAsJsonObject();
        String date = instance.get("date").getAsString();
        double amount = instance.get("amount").getAsDouble();
        String description = instance.get("description").getAsString();
        if (Objects.equals(classType, "Payment")) {
            try {
                Payment payment = new Payment(date, amount, description);
                return new Payment(payment, instance.get("incomingInterest").getAsDouble(),
                        instance.get("outgoingInterest").getAsDouble());
            } catch (TransactionAttributeException e) {
                throw new RuntimeException(e);
            }
        } else if (Objects.equals(classType, "IncomingTransfer")) {
            try {
                IncomingTransfer incomingTransfer = new IncomingTransfer(date, amount, description);
                return new IncomingTransfer(incomingTransfer, instance.get("sender").getAsString(),
                        instance.get("recipient").getAsString());
            } catch (TransactionAttributeException e) {
                throw new RuntimeException(e);
            }
        } else if (Objects.equals(classType, "OutgoingTransfer")) {
            try {
                OutgoingTransfer outgoingTransfer = new OutgoingTransfer(date, amount, description);
                return new OutgoingTransfer(outgoingTransfer, instance.get("sender").getAsString(),
                        instance.get("recipient").getAsString());
            } catch (TransactionAttributeException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
