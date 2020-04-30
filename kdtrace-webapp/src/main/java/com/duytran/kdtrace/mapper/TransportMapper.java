package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.entity.Transport;
import com.duytran.kdtrace.model.TransportModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransportMapper {
    TransportMapper INSTANCE = Mappers.getMapper(TransportMapper.class);

    TransportModel transportToTransportModel(Transport transport);

    Transport transportModelToTransport(TransportModel transportModel);

}
