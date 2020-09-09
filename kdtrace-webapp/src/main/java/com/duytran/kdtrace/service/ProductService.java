package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.*;
import com.duytran.kdtrace.exeption.MessagingExceptionHandler;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.ProducerMapper;
import com.duytran.kdtrace.mapper.ProductMapper;
import com.duytran.kdtrace.model.*;
import com.duytran.kdtrace.repository.*;
import com.duytran.kdtrace.security.principal.UserPrincipalService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.IntStream;


@Service
public class ProductService {
    @Autowired
    ProductRepositoty productRepository;

    @Autowired
    QRCodeRepository qrCodeRepository;

    @Autowired
    UserPrincipalService userPrincipalService;

    @Autowired
    ProducerService producerService;

    @Autowired
    CommonService commonService;

    @Autowired
    BlockchainService blockchainService;

    @Autowired
    ProducerRepository producerRepository;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    MedicineRepository medicineRepository;

    @Transactional
    public ResponseModel createProduct(ProductModel productModel, MultipartFile[] multipartFiles) throws IOException {
        Product product = ProductMapper.INSTANCE.productModelToProduct(productModel);
        Producer producer = producerService.getProducerInPrincipal();
        product.setProducer(producer);
        product.setCreate_at(commonService.getDateTime());
        List<Medicine> medicines = product.getMedicines();
        Product temp = productRepository.save(product);

        for(Medicine medicine : medicines){
            medicine.setProduct(temp);
        }
        medicineRepository.saveAll(medicines);
        product.setImage(saveImage(multipartFiles, product.getId()));
        List<QRCode> qrCodes = generateCode(product);
        qrCodeRepository.saveAll(qrCodes);
        product.setCodes(qrCodes);
        productRepository.save(product);
        blockchainService.updateProduct(producer.getUser(), product.getId(), "kdtrace");
        blockchainService.createQRCodes(producer.getUser(), product.getId(), "kdtrace");
        return new ResponseModel("Create Successfully", HttpStatus.OK.value(), productModel);
    }

    private String saveImage(MultipartFile[] multipartFiles, Long id) throws IOException {
        String directory = "image-store/" + id;
        Path dest = Files.createDirectories(Paths.get(directory));
        List<String> imageList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String fileName = UUID.randomUUID().toString().replace("-", "") + "." +
                    FilenameUtils.getExtension(file.getOriginalFilename());
            file.transferTo(Files.createFile(dest.resolve(fileName)));
            imageList.add("https://kdtrace.xyz/" + directory + "/" + fileName);
        }
        return imageList.toString();
    }

    @Value("${url}")
    private String url;

    private List<QRCode> generateCode(Product product) {
        List<QRCode> qrCodes = new ArrayList<>();
        IntStream.rangeClosed(1, (int) product.getQuantity()).forEach(
                i -> {
                    String code = null;
                    try {
                        code = URLEncoder.encode("P" + product.getId() + "-N" + i, StandardCharsets.UTF_8.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String link = url + code;
                    qrCodes.add(new QRCode(product, code, userPrincipalService.getUserCurrentLogined(), link,
                            StatusQRCode.AVAILABLE, commonService.getDateTime()));
                }
        );
        return qrCodes;
    }

    @Transactional
    public ResponseModel getAllProducts() {
        Long producerID = producerService.getProducerInPrincipal().getId();
        List<Product> products = productRepository.findAllByProducer_Id(producerID);
        List<ProductModel> productModels = ProductMapper.INSTANCE.listProductToModel(products);
        return new ResponseModel("List Product", HttpStatus.OK.value(), productModels);
    }

    @Transactional
    public boolean checkQuanlityProducts(Long id_product, long quanlity) {
        long quanlity_products = productRepository.getQuanlityProducts(id_product);
        return (quanlity_products >= quanlity);
    }

    @Transactional
    public Product getProductEntityById(Long id) {
        return productRepository.findProductById(id).orElseThrow(
                () -> new RecordNotFoundException("Product is not exist")
        );
    }

    public ResponseModel getProductById(Long id) {
        Product product = getProductEntityById(id);
        ProductModel productModel = ProductMapper.INSTANCE.productToProductModel(product);
        return new ResponseModel("Successfully", HttpStatus.OK.value(), productModel);
    }

    public ResponseModel getProductByIdWithAvailable(Long id) {
        Product product = getProductEntityById(id);
        ProductModel productModel = ProductMapper.INSTANCE.productToProductModel(product);
        Predicate<QRCodeModel> isNotAVAILABLE = qrCodeModel -> (qrCodeModel.getStatusQRCode() != StatusQRCode.AVAILABLE);
        productModel.getCodes().removeIf(isNotAVAILABLE);
        return new ResponseModel("Successfully", HttpStatus.OK.value(), productModel);
    }

    @Transactional
    public void changeStatusQRCode(User user, Long productID, long quanlity, StatusQRCode statusQRCode) {
        Product product = productRepository.findProductById(productID).get();
        List<QRCode> qrCodes;
        if (statusQRCode == StatusQRCode.WAITING) {
            product.setQuantity(product.getQuantity() - quanlity);
            qrCodes = qrCodeRepository.getListQRCodeByProductIdAndStatusQRCode(productID, "AVAILABLE");
        } else {
            product.setQuantity(product.getQuantity() + quanlity);
            qrCodes = qrCodeRepository.getListQRCodeByProductIdAndStatusQRCode(productID, "WAITING");
        }

        List<Long> qrCodeIds = new ArrayList<>();
        IntStream.rangeClosed(0, (int) quanlity - 1).forEach(
                i -> {
                    QRCode qrCode = qrCodes.get(i);
                    qrCode.setStatusQRCode(statusQRCode);
                    qrCodeIds.add(qrCode.getId());
                    qrCodeRepository.save(qrCode);
                });
        blockchainService.saveQRCodes(user, qrCodeIds, statusQRCode, null, "Org1", "kdtrace");
        productRepository.save(product);
    }

    public boolean checkExistProductByIdAndProducer(Long id, Long producer_id) {
        return productRepository.existsByIdAndProducer_Id(id, producer_id);
    }

    public Long trackingCode(String code, String otp) {
        QRCode qrCode = qrCodeRepository.findByCode(code).orElse(new QRCode());
        if (otp.equals(qrCode.getOtp())) {
            if (qrCode.getTracking() == null)
                qrCode.setTracking(1L);
            else
                qrCode.setTracking(qrCode.getTracking() + 1);
            return qrCodeRepository.save(qrCode).getTracking();
        }
        return null;
    }

    public String updateReport(RequestReport requestReport) {
        QRCode qrCode = qrCodeRepository.findByCode(requestReport.getCode()).orElse(null);
        if (qrCode == null || qrCode.getStatusQRCode()!= StatusQRCode.READY) {
            return "QRCode not found!";
        }
        Report report = ProductMapper.INSTANCE.requestReportToReport(requestReport);
        report.setProductLink(qrCode.getLink() );
        report.setTime(commonService.getDateTime());
        report.setCode(qrCode);
        reportRepository.save(report);
        return "Submit report for " + qrCode.getProduct().getName() + " successfully. Management department will check and overcome that is concerned. Thank you!";
    }

    public ResponseModel getAllReports() {
        try {
            List<Report> reports = reportRepository.findAll();
            return new ResponseModel("Get all report successfully!", HttpStatus.OK.value(), reports);
        }catch (Exception e){
            throw new MessagingExceptionHandler("Fail to get all Record");
        }
    }

    @Transactional
    public ResponseModel getAllProductForDistributor() {
        List<Product> products = productRepository.findAllByOrderByIdAsc();
        List<ProductModel> productModels = ProductMapper.INSTANCE.listProductToModel(products);
        String name = products.get(0).getProducer().getCompanyName();
        productModels.forEach(productModel -> {
            Predicate<QRCodeModel> isNotAVAILABLE = qrCodeModel -> (qrCodeModel.getStatusQRCode() != StatusQRCode.AVAILABLE);
            productModel.getCodes().removeIf(isNotAVAILABLE);
            productModel.setCompanyName(name);
        });
        Predicate<ProductModel> notContainsAVAILABLE = productModel -> (productModel.getCodes().size() == 0);
        productModels.removeIf(notContainsAVAILABLE);

        Predicate<ProductModel> productExp = productModel -> (Duration.between(
                LocalDate.parse(commonService.getDateTime().substring(0, 10).replace("/", "-"), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(),
                LocalDate.parse(productModel.getExp().toString().substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay()
        ).toDays() <= 0);
        productModels.removeIf(productExp);
        return new ResponseModel("List Products For Market", HttpStatus.OK.value(), productModels);
    }

    @Transactional
    public ResponseModel getAllOrderByProducer() {
        List<Producer> producers = producerRepository.findAll();
        List<ProducerModel> producerModels = new ArrayList<>();
        producers.forEach(producer -> {
            if (producer.getUser().isActive() && producer.getProducts() != null) {
                ProducerModel producerModel = ProducerMapper.INSTANCE.producerToProducerModel(producer);
                producerModel.getProductModels().forEach(productModel -> {
                    Predicate<QRCodeModel> isAVAILABLE = qrCodeModel -> (qrCodeModel.getStatusQRCode() != StatusQRCode.AVAILABLE);
                    productModel.getCodes().removeIf(isAVAILABLE);
                });
                Predicate<ProductModel> nullProductAVAILABLE = productModel -> (productModel.getCodes().size() == 0);
                producerModel.getProductModels().removeIf(nullProductAVAILABLE);
                producerModels.add(producerModel);
            }
        });
        Predicate<ProducerModel> nullProducerAVAILABLE = producerModel -> (producerModel.getProductModels().size() == 0);
        producerModels.removeIf(nullProducerAVAILABLE);
        return new ResponseModel("Get all product successfully", 200, producerModels);
    }
}
