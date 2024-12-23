package com.hust.smart_Shopping.utils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateUtil {

    private static final DateTimeFormatter ISO_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Deserialize: từ chuỗi "yyyy-MM-dd" thành Instant
    public static class InstantDeserializer extends JsonDeserializer<Instant> {

        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String date = p.getText();
            LocalDate localDate = LocalDate.parse(date, ISO_DATE_FORMAT);
            return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        }
    }

    // Serialize: từ Instant thành chuỗi "yyyy-MM-dd"
    public static class InstantSerializer extends JsonSerializer<Instant> {

        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // Chuyển Instant thành LocalDate và sau đó format theo định dạng "yyyy-MM-dd"
            String formattedDate = value.atZone(ZoneOffset.UTC).toLocalDate().format(ISO_DATE_FORMAT);
            gen.writeString(formattedDate);
        }
    }
}
