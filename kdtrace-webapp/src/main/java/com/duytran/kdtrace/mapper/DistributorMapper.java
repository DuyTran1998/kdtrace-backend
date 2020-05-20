package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.entity.Distributor;
import com.duytran.kdtrace.model.DistributorModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DistributorMapper {
    DistributorMapper INSTANCE = Mappers.getMapper(DistributorMapper.class);

    DistributorModel distributorToDistributorModel(Distributor distributor);

 //   Distributor distributorModelToDistributor(DistributorModel distributorModel);
}