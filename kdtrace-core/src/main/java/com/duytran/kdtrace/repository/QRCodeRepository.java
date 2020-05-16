package com.duytran.kdtrace.repository;

import com.duytran.kdtrace.entity.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCode, Long> {
    @Query(value = "SELECT * FROM QRCodes where QRCodes.product_id= ?1", nativeQuery = true)
    List<QRCode> getListQRCode(Long id);

    @Query(value = "SELECT * FROM QRCodes where QRCodes.product_id= ?1 and QRCodes.statusQRCode= ?2", nativeQuery = true)
    List<QRCode> getListQRCodeByProductIdAndStatusQRCode(Long id, String statusQRCode);

    List<QRCode> findAllByProcess_Id(Long id);

    Optional<QRCode> findByCode(String code);
}
