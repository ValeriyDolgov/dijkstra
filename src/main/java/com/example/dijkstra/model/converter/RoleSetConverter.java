package com.example.dijkstra.model.converter;

import com.example.dijkstra.model.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Converter(autoApply = true)
@Component
public class RoleSetConverter implements AttributeConverter<Set<Role>, String> {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<Role> roles) {
        try {
            return objectMapper.writeValueAsString(roles);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Set<Role> convertToEntityAttribute(String s) {
        JavaType roleSetType = objectMapper.getTypeFactory().constructParametricType(Set.class, Role.class);
        try {
            return objectMapper.readValue(s, roleSetType);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
