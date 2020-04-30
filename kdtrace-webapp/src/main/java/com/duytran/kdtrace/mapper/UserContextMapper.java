package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.entity.HFUserContext;
import com.duytran.kdtrace.model.UserContextDto;
import model.UserContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserContextMapper {

    UserContextMapper INSTANCE = Mappers.getMapper(UserContextMapper.class);

    @Mapping(target = "id", ignore = true)
    HFUserContext toHFUserContext(UserContext userContext);

    UserContextDto toUserContextDto(HFUserContext hfUserContext);

    List<UserContextDto> toUserContextDtoList(List<HFUserContext> hfUserContexts);
}
