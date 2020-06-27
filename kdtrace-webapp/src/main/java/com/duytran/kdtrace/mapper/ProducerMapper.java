package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.entity.Producer;
import com.duytran.kdtrace.model.ProducerInfoModel;
import com.duytran.kdtrace.model.ProducerModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProducerMapper {
    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

    ProducerInfoModel producerToProducerInfoModel(Producer producer);

//    Producer producerToProducerModel(ProducerModel producerModel);

    @Mapping(source = "products", target = "productModels")
    ProducerModel producerToProducerModel(Producer producer);
}
