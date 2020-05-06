package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.entity.Producer;
import model.LedgerProducer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LedgerUserMapper {
    LedgerUserMapper INSTANCE = Mappers.getMapper(LedgerUserMapper.class);

    @Mapping(target = "producerId", source = "id")
    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "avatar", source = "avatar")
    @Mapping(target = "create_at", source = "create_at")
    @Mapping(target = "update_at", source = "update_at")
    LedgerProducer toLedgerProducer (Producer producer);
}
