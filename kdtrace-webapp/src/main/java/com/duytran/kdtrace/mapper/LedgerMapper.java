package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.entity.*;
import com.duytran.kdtrace.entity.Process;
import com.duytran.kdtrace.model.*;
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
    @Mapping(target = "website", source = "website")
    @Mapping(target = "tin", source = "tin")
    LedgerProducer toLedgerProducer (Producer producer);

    @Mapping(target = "transportId", source = "id")
    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "avatar", source = "avatar")
    @Mapping(target = "create_at", source = "create_at")
    @Mapping(target = "update_at", source = "update_at")
    @Mapping(target = "website", source = "website")
    @Mapping(target = "tin", source = "tin")
    LedgerTransport toLedgerTransport (Transport transport);

    @Mapping(target = "distributorId", source = "id")
    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "avatar", source = "avatar")
    @Mapping(target = "create_at", source = "create_at")
    @Mapping(target = "update_at", source = "update_at")
    @Mapping(target = "website", source = "website")
    @Mapping(target = "tin", source = "tin")
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
    @Mapping(target = "transportId", source = "transportID")
    @Mapping(target = "create_at", source = "create_at")
    @Mapping(target = "statusProcess", ignore = true)
    LedgerProcess toLedgerProcess (Process process);

    @Mapping(target = "autoMaker", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "transportId", source = "transport.id")
    LedgerDeliveryTruck toLedgerDeliveryTruck (DeliveryTruck deliveryTruck);

    @Mapping(target = "codes", ignore = true)
    ProductModel toProductModel(LedgerProduct ledgerProduct);
    @Mapping(target = "id", source = "producerId")
    ProducerModel toProducerModel(LedgerProducer ledgerProducer);
    @Mapping(target = "id", source = "transportId")
    TransportModel toTransportModel(LedgerTransport ledgerTransport);
    DeliveryTruckModel toDeliveryTruckModel(LedgerDeliveryTruck ledgerDeliveryTruck);
    @Mapping(target = "id", source = "distributorId")
    DistributorModel toDistributorModel(LedgerDistributor ledgerDistributor);
}
