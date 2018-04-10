package com.dxk.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.core.methods.request.Transaction;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WalletServiceTest {

    @Value("${wallet.test}")
    private String wallet_test;

    @Resource
    private WalletService walletService;

    @Test
    public void test() {
//        walletService.connect();
//        walletService.createWallet();
//        walletService.loadWallet("360e5b31c324843cd57a12a8f9bd740b4d50d447.json");
//        walletService.deployContract();

//        walletService.unLockAccount();
//        Transaction tx = walletService.createTx("0x360e5b31c324843cd57a12a8f9bd740b4d50d447");
//        walletService.getEstimateGas(tx);
//        walletService.getBalance(wallet_test);
//        String txHash = walletService.sendTx(tx);
//        walletService.getTxByHash(txHash);
        walletService.tx();
    }


}
