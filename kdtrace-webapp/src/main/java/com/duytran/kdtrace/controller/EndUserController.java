package com.duytran.kdtrace.controller;


import com.duytran.kdtrace.entity.RoleName;
import com.duytran.kdtrace.model.RequestReport;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.service.EndUserService;
import com.duytran.kdtrace.service.ProcessService;
import com.duytran.kdtrace.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/enduser")
public class EndUserController {
    @Autowired
    private EndUserService endUserService;

    @RequestMapping(value = "/get-root-product", method = RequestMethod.GET)
    public ResponseModel getRootByQRCode(@RequestParam String code) {
        return endUserService.getRootProduct(code);
    }

    @RequestMapping(value = "/get-product-info", method = RequestMethod.GET)
    public ResponseModel getByQRCode(@RequestParam String code) {
        return endUserService.getInfomation(code);
    }

    @RequestMapping(value = "/tracking-code", method = RequestMethod.PATCH)
    public ResponseModel trackingQRCode(@RequestParam String otp,
                                        @RequestParam String code) {
        if(otp == null || otp =="" || code == null || code ==""){
            return new ResponseModel("Error", 401, null);
        }
        return new ResponseModel("Tracking QRCode successfully",
                200,
                endUserService.trackingCode(code, otp));
    }

    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public ResponseModel updateReport(@RequestBody RequestReport requestReport) {
        return new ResponseModel(null, 200,
                endUserService.updateReport(requestReport));
    }

    @RequestMapping(value = "/getRate", method = RequestMethod.GET)
    public ResponseModel getRate(@RequestParam Long id, @RequestParam RoleName role) {
        return new ResponseModel(null, 200, endUserService.getRate(id, role));
    }
}
