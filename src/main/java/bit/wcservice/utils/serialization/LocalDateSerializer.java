package bit.wcservice.utils.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {
    private final DateTimeFormatter serializeFormatter;

    public LocalDateSerializer(DateTimeFormatter serializeFormatter) {
        this.serializeFormatter = serializeFormatter;
    }

    @Override
    public void serialize(LocalDate value,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(value.format(serializeFormatter));
    }
}
