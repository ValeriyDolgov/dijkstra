package com.example.dijkstra.service.mapper;

import com.example.dijkstra.controller.request.CreateManagerRequest;
import com.example.dijkstra.controller.request.CreateUserRequest;
import com.example.dijkstra.model.Role;
import com.example.dijkstra.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", expression = "java(setDriverRole())")
    User createDriverUser(CreateUserRequest createUserRequest);

    @Mapping(target = "roles", expression = "java(setManagerRole())")
    User createManagerUser(CreateManagerRequest createUserRequest);

    default Set<Role> setDriverRole() {
        return  Set.of(Role.DRIVER);
    }

    default Set<Role> setManagerRole() {
        return  Set.of(Role.MANAGER);
    }
}
