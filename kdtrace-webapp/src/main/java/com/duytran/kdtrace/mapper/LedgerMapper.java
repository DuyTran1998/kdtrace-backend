package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.entity.*;
import com.duytran.kdtrace.entity.Process;
import model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LedgerMapper {
    LedgerMapper INSTANCE = Mappers.getMapper(LedgerMapper.class);

    @Mapping(target = "producerId", source = "id")
    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "avatar", source = "avatar")
    @Mapping(target = "create_at", source = "create_at")
    @Mapping(target = "update_at", source = "update_at")
    LedgerProducer toLedgerProducer (Producer producer);

    @Mapping(target = "transportId", source = "id")
    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "avatar", source = "avatar")
    @Mapping(target = "create_at", source = "create_at")
    @Mapping(target = "update_at", source = "update_at")
    LedgerTransport toLedgerTransport (Transport transport);

    @Mapping(target = "distributorId", source = "id")
    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "avatar", source = "avatar")
    @Mapping(target = "create_at", source = "create_at")
    @Mapping(target = "update_at", source = "update_at")
    LedgerDistributor toLedgerDistributor (Distributor distributor);

    @Mapping(target = "userId", source = "producer.user.id")
    @Mapping(target = "producerId", source = "producer.id")
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "codes", ignore = true)
    LedgerProduct toLedgerProduct (Product product);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "processId", source = "process.id")
    LedgerQRCode toLedgerQrCode (QRCode qrCode);

    List<LedgerQRCode> toLedgerQrCodeList (List<QRCode> qrCodes);

    @Mapping(target = "distributorId", source = "distributor.id")
    @Mapping(target = "qrCodes", ignore = true)
    @Mapping(target = "deliveryTruckId", source = "deliveryTruck.id")
    @Mapping(target = "statusProcess", ignore = true)
    LedgerProcess toLedgerProcess (Process process);

    @Mapping(target = "autoMaker", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "transportId", source = "transport.id")
    LedgerDeliveryTruck toLedgerDeliveryTruck (DeliveryTruck deliveryTruck);
}
