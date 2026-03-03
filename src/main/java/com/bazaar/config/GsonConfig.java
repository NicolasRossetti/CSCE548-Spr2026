package com.bazaar.config;

import java.time.LocalDateTime;

import org.springframework.boot.autoconfigure.gson.GsonBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

@Configuration
public class GsonConfig {

    @Bean
    public GsonBuilderCustomizer localDateTimeGsonCustomizer() {
        JsonSerializer<LocalDateTime> serializer = (src, typeOfSrc, context) -> new JsonPrimitive(src.toString());
        JsonDeserializer<LocalDateTime> deserializer = (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString());

        return builder -> builder
                .registerTypeAdapter(LocalDateTime.class, serializer)
                .registerTypeAdapter(LocalDateTime.class, deserializer);
    }
}
