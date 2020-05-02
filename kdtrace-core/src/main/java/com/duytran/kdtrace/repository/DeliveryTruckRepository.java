package com.duytran.kdtrace.repository;

import com.duytran.kdtrace.entity.DeliveryTruck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryTruckRepository extends JpaRepository<DeliveryTruck, Long> {
    boolean existsDeliveryTruckByNumberPlate(String numberPlate);

    List<DeliveryTruck> findAllByTransport_Id(Long id);
}
