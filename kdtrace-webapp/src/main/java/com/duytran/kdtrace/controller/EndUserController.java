package com.duytran.kdtrace.controller;


import com.duytran.kdtrace.model.RequestFeedback;
import com.duytran.kdtrace.model.ResponseModel;
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
    private ProcessService processService;
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/get-product-info", method = RequestMethod.GET)
    public ResponseModel getByQRCode(@RequestParam String code) {
        return processService.getInfomation(code);
    }

    @RequestMapping(value = "/tracking-code", method = RequestMethod.PATCH)
    public ResponseModel trackingQRCode(@RequestParam String otp,
                                        @RequestParam String code) {
        if(otp == null || otp =="" || code == null || code ==""){
            return new ResponseModel("Error", 401, null);
        }
        return new ResponseModel("Tracking QRCode successfully",
                200,
                productService.trackingCode(code, otp));
    }

    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public ResponseModel updateFeedback(@RequestBody RequestFeedback requestFeedback) {
        return new ResponseModel(null, 200,
                productService.updateFeedback(requestFeedback.getCode(), requestFeedback.getFeedback()));
    }
}
