package com.iot.data_management_system.Business.mapper;

import com.iot.data_management_system.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-26T00:02:43+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User updateUserPartial(User user, User updatedUser) {
        if ( updatedUser == null ) {
            return user;
        }

        user.setId( updatedUser.getId() );
        if ( updatedUser.getName() != null ) {
            user.setName( updatedUser.getName() );
        }
        if ( updatedUser.getSurname() != null ) {
            user.setSurname( updatedUser.getSurname() );
        }
        if ( updatedUser.getEmail() != null ) {
            user.setEmail( updatedUser.getEmail() );
        }
        if ( updatedUser.getPassword() != null ) {
            user.setPassword( updatedUser.getPassword() );
        }
        if ( updatedUser.getRole() != null ) {
            user.setRole( updatedUser.getRole() );
        }

        return user;
    }
}
