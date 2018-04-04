package com.dxk.controller;

import com.dxk.service.WalletService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("")
public class TestController {

    @Resource
    private WalletService walletService;

    @GetMapping("/demo")
    public void demo() {
        try {
            walletService.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
