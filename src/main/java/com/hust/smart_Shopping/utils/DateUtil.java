// package com.hust.smart_Shopping.utils;

// import java.io.IOException;
// import java.time.Instant;
// import java.time.LocalDate;
// import java.time.ZoneId;
// import java.time.format.DateTimeFormatter;

// import com.fasterxml.jackson.core.JsonGenerator;
// import com.fasterxml.jackson.core.JsonParser;
// import com.fasterxml.jackson.databind.DeserializationContext;
// import com.fasterxml.jackson.databind.JsonDeserializer;
// import com.fasterxml.jackson.databind.JsonSerializer;
// import com.fasterxml.jackson.databind.SerializerProvider;

// public class DateUtil {

// // Định dạng MM/dd/yyyy
// private static final DateTimeFormatter DATE_FORMATTER =
// DateTimeFormatter.ofPattern("MM/dd/yyyy");

// // Deserialize: từ chuỗi "MM/dd/yyyy" thành Instant
// public static class InstantDeserializer extends JsonDeserializer<Instant> {

// @Override
// public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws
// IOException {
// String date = p.getText();
// LocalDate localDate = LocalDate.parse(date, DATE_FORMATTER);
// return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
// }

// }

// // Serialize: từ Instant thành chuỗi "MM/dd/yyyy"
// public static class InstantSerializer extends JsonSerializer<Instant> {

// @Override
// public void serialize(Instant value, JsonGenerator gen, SerializerProvider
// serializers) throws IOException {
// String formattedDate =
// value.atZone(ZoneId.systemDefault()).toLocalDate().format(DATE_FORMATTER);
// gen.writeString(formattedDate);
// }

// }
// }
