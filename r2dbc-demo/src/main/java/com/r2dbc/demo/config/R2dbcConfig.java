package com.r2dbc.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2dbc.demo.converters.JsonToMapReadingConverter;
import com.r2dbc.demo.converters.MapToJsonWritingConverter;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class R2dbcConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        return new R2dbcCustomConversions(
                CustomConversions.StoreConversions.NONE,
                List.of(
                        new JsonToMapReadingConverter(objectMapper),    // Read (DB -> Java)
                        new MapToJsonWritingConverter(objectMapper)     // Write (Java -> DB)
//                        new JsonbReadingConverter(),
//                        new JsonbWritingConverter()
                )
        );
    }

    public abstract static class AbstractJsonbConverter implements GenericConverter {
        protected final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            Set<ConvertiblePair> pairs = new HashSet<>();
            pairs.add(new ConvertiblePair(Json.class, Object.class)); // Reading
            pairs.add(new ConvertiblePair(Object.class, Json.class)); // Writing
            return pairs;
        }
    }

    @ReadingConverter
    public static class JsonbReadingConverter extends AbstractJsonbConverter {

        @Override
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            if (source == null) return null;
            try {
                System.out.println("sourceType: " + sourceType.getType() + ", targetType: " + targetType.getType());
                if (source instanceof Json s) {
                    return objectMapper.readValue(s.asString(), targetType.getType());
                }
                if (source instanceof Enum<?> e) {
                    return e.name();
                }
                return source;
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to convert JSON to " + targetType.getType(), e);
            }
        }
    }

    @WritingConverter
    public class JsonbWritingConverter extends AbstractJsonbConverter {

        @Override
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            if (source == null) return null;
            try {
                String json = objectMapper.writeValueAsString(source);
                return Json.of(json);
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to convert " + sourceType.getType() + " to JSON", e);
            }
        }
    }
}
