package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.entity.Process;
import com.duytran.kdtrace.entity.QRCode;
import com.duytran.kdtrace.model.ProcessModel;
import com.duytran.kdtrace.model.QRCodeModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProcessMapper {
    ProcessMapper INSTANCE = Mappers.getMapper(ProcessMapper.class);

 //   QRCodeModel qrCodeToQRCodeModel(QRCode qrCode);

    @Mapping(target = "transportModel", source = "deliveryTruck.transport")
    @Mapping(target = "deliveryTruckModel", source = "deliveryTruck")
    @Mapping(target = "distributorModel", source = "distributor")
    ProcessModel processToProcessModel(Process process);

    List<ProcessModel> toProcessModelList (List<Process> processes);
}
