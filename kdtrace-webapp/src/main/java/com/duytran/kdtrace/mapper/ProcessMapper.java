package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.entity.Process;
import com.duytran.kdtrace.entity.QRCode;
import com.duytran.kdtrace.model.ProcessModel;
import com.duytran.kdtrace.model.QRCodeModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProcessMapper {
    ProcessMapper INSTANCE = Mappers.getMapper(ProcessMapper.class);

    QRCodeModel qrCodeToQRCodeModel(QRCode qrCode);

    ProcessModel processToProcessModel(Process process);
}
