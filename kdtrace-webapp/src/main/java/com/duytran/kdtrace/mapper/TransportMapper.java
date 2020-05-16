package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.entity.Transport;
import com.duytran.kdtrace.model.TransportModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TransportMapper {
    TransportMapper INSTANCE = Mappers.getMapper(TransportMapper.class);

    TransportModel transportToTransportModel(Transport transport);

    Transport transportModelToTransport(TransportModel transportModel);

    List<TransportModel> listTransportToListTransportModel(List<Transport> transports);
}
