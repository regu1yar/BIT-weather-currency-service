package bit.utils.serialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateKeyDeserializer extends KeyDeserializer {
    private final DateTimeFormatter deserializeFormatter;

    public LocalDateKeyDeserializer(DateTimeFormatter deserializeFormatter) {
        this.deserializeFormatter = deserializeFormatter;
    }

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) {
        return LocalDate.parse(s, deserializeFormatter);
    }
}
