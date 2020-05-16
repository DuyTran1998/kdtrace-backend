package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.generator.ZXingHelper;
import com.duytran.kdtrace.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Slf4j
@Controller
public class EndUserController {
    @Autowired
    private ProcessService processService;

    @RequestMapping(value = {"process"}, method = RequestMethod.GET)
    public String index(Model model, @RequestParam Long id){
       //Long a = new Long(2);
        model.addAttribute("qrCodes", processService.getQRCodeByProcessId(id));
        return "index";
    }


    @RequestMapping(value = "process/qrcode/{code}", method = RequestMethod.GET)
    public void qrCode(@PathVariable("code") String code, HttpServletResponse response) throws Exception{
        response.setContentType("image/png");
        String link = "http://localhost:8080/getInfo?qr_code=" + code;
        log.info(link);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(ZXingHelper.getQRCodeImage(link, 200 ,200));
        outputStream.flush();
        outputStream.close();
    }

    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getInfo(@RequestParam String qr_code){
        return ResponseEntity.ok(processService.getInfomation(qr_code));
    }
    //localhost:8080/getInfo?qr_code=OrangeVN-L1-N24
}
