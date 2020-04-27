package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.entity.DeliveryTruck;
import com.duytran.kdtrace.model.DeliveryTruckModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DeliveryTruckMapper {
    DeliveryTruckMapper INSTANCE = Mappers.getMapper(DeliveryTruckMapper.class);

    DeliveryTruck deliveryTruckModelToDelivery(DeliveryTruckModel deliveryTruckModel);

    DeliveryTruckModel deliveryTruckToDeliveryTruckModel(DeliveryTruck deliveryTruck);

    List<DeliveryTruckModel> deliveryTruckListToDeliveryTruckModelList(List<DeliveryTruck> deliveryTrucks);
}
