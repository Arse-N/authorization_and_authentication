package com.stellarlabs.authentication_and_authorization_service.mapper;

import com.stellarlabs.authentication_and_authorization_service.dto.auth.RegisterDto;
import com.stellarlabs.authentication_and_authorization_service.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface RegisterDtoConverter {

    User mapTo(RegisterDto registerDto);

}
