package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.entity.Medicine;
import com.duytran.kdtrace.entity.Product;
import com.duytran.kdtrace.entity.QRCode;
import com.duytran.kdtrace.entity.Report;
import com.duytran.kdtrace.model.MedicineModel;
import com.duytran.kdtrace.model.ProductModel;
import com.duytran.kdtrace.model.QRCodeModel;
import com.duytran.kdtrace.model.RequestReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    QRCodeModel qrCodeToQRCodeModel(QRCode qrCode);

    List<QRCodeModel> qrCodeListToQRCodeModel(List<QRCode> qrCodes);

    @Mapping(target = "companyName", source = "producer.companyName")
    ProductModel productToProductModel(Product product);

    Product productModelToProduct(ProductModel productModel);

    Medicine medicineModelToMedicine(MedicineModel medicineModel);

    List<ProductModel> listProductToModel(List<Product> products);

    @Mapping(target = "code", ignore = true)
    Report requestReportToReport (RequestReport requestReport);
}
