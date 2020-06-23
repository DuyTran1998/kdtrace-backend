package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.generator.ZXingHelper;
import com.duytran.kdtrace.model.RequestFeedback;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.service.ProcessService;
import com.duytran.kdtrace.service.ProductService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.OutputStream;
import java.util.Map;

@Slf4j
@RestController
public class EndUserController {
    @Autowired
    private ProcessService processService;
    @Autowired
    private ProductService productService;

    @RequestMapping(value = {"process"}, method = RequestMethod.GET)
    public String index(Model model, @RequestParam Long id) {
        //Long a = new Long(2);
        model.addAttribute("qrCodes", processService.getQRCodeByProcessId(id));
        return "index";
    }

    @RequestMapping(value = "process/qrcode/{code}", method = RequestMethod.GET)
    public void qrCode(@PathVariable("code") String code, HttpServletResponse response) throws Exception {
        response.setContentType("image/png");
        String link = "http://localhost:8080/getInfo?qr_code=" + code;
        log.info(link);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(ZXingHelper.getQRCodeImage(link, 200, 200));
        outputStream.flush();
        outputStream.close();
    }

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
