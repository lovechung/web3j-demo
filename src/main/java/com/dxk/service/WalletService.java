package com.dxk.service;

import com.dxk.utils.Greeter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class WalletService {

    private static Logger log = LoggerFactory.getLogger(WalletService.class);

    @Value("${wallet.filepath}")
    private String wallet_filepath;
    @Value("${wallet.pwd}")
    private String wallet_pwd;

    @Resource
    private Web3j web3j;

    public void run() throws Exception {
        // 建立连接
        log.info("====================开始建立连接====================");
        Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
        log.info("web3ClientVersion：{}", web3ClientVersion.getWeb3ClientVersion());

        // 加载钱包
        log.info("====================开始加载钱包====================");
        String fileName = WalletUtils.generateNewWalletFile(wallet_pwd, new File(wallet_filepath), true);
        log.info("钱包文件名: {}", fileName);
        String content = FileUtils.readFileToString(new File(wallet_filepath + fileName), "UTF-8");
        log.info("钱包内容: {}", content);

        Credentials credentials = WalletUtils.loadCredentials(wallet_pwd, wallet_filepath + fileName);
        log.info("凭证地址: {}", credentials.getAddress());
        log.info("凭证私钥: {}", credentials.getEcKeyPair().getPrivateKey());
        log.info("凭证公钥: {}", credentials.getEcKeyPair().getPublicKey());

        // 查询钱包余额
        EthGetBalance balance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
        log.info("钱包余额: {}", balance.getBalance());

        // 发送wei
//        log.info("====================开始发送1个wei====================");
//        log.info("发送 1 Wei ( {} Ether)", Convert.fromWei("1", Convert.Unit.ETHER).toPlainString());
//        TransactionReceipt transferReceipt = Transfer.sendFunds(
//                web3j,
//                credentials,
//                credentials.getAddress(),
//                BigDecimal.ONE,
//                Convert.Unit.WEI).send();
//        log.info("交易完成, 交易详情请查看：https://mainnet.etherscan.io/tx/{}", transferReceipt.getTransactionHash());

        // 部署合约
        log.info("====================开始部署合约====================");
        Greeter contract = Greeter.deploy(
                web3j,
                credentials,
                ManagedTransaction.GAS_PRICE,
                Contract.GAS_LIMIT,
                "Hello blockchain world!").send();
        log.info("智能合约已部署到地址：" + contract.getContractAddress());
        log.info("合约详情请查看：https://rinkeby.etherscan.io/address/" + contract.getContractAddress());
        log.info("Value stored in remote smart contract: " + contract.greet().send());
    }
}
