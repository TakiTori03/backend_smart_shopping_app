// package com.hust.smart_Shopping.configurations;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.SerializationFeature;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
// import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
// import com.hust.smart_Shopping.utils.DateUtil;

// import java.time.Instant;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Primary;

// @Configuration
// public class JacksonConfig {

// @Bean
// @Primary
// public ObjectMapper objectMapper() {
// ObjectMapper mapper = new ObjectMapper();

// // Đăng ký JavaTimeModule để hỗ trợ Java Time API
// JavaTimeModule module = new JavaTimeModule();

// // Thêm serializer và deserializer tùy chỉnh cho Instant
// module.addSerializer(Instant.class, new DateUtil.InstantSerializer());
// module.addDeserializer(Instant.class, new DateUtil.InstantDeserializer());

// // module.addSerializer(LocalDate.class, new
// // LocalDateSerializer(DateTimeFormatter.ISO_DATE_TIME));
// // module.addDeserializer(LocalDate.class, new
// // LocalDateDeserializer(DateTimeFormatter.ISO_DATE_TIME));

// // Đăng ký module
// mapper.registerModule(module);

// // Đảm bảo không serialize ngày dưới dạng timestamp
// mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

// return mapper;
// }
// }
