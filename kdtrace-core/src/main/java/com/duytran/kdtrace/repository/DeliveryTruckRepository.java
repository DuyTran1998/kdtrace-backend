package com.duytran.kdtrace.repository;

import com.duytran.kdtrace.entity.DeliveryTruck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryTruckRepository extends JpaRepository<DeliveryTruck, Long> {
    boolean existsDeliveryTruckByNumberPlate(String numberPlate);

    List<DeliveryTruck> findAllByTransport_Id(Long id);

    Optional<DeliveryTruck> findDeliveryTruckById(Long id);
}
