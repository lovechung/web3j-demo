package com.dxk.service;

import com.dxk.utils.HumanStandardToken;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class WalletService {

    private static Logger log = LoggerFactory.getLogger(WalletService.class);

    @Value("${wallet.filepath}")
    private String wallet_filepath;
    @Value("${wallet.pwd}")
    private String wallet_pwd;
    @Value("${wallet.test}")
    private String wallet_test;

    @Resource
    private Web3j web3j;

    /**
     * 建立连接
     */
    public void connect() {
        try {
            log.info("====================开始建立连接====================");
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            log.info("web3j客户端版本：{}", web3ClientVersion.getWeb3ClientVersion());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建钱包
     */
    public void createWallet() {
        try {
            log.info("====================开始创建钱包====================");
            String fileName = WalletUtils.generateNewWalletFile(wallet_pwd, new File(wallet_filepath), true);
            log.info("钱包文件名: {}", fileName);
            String content = FileUtils.readFileToString(new File(wallet_filepath + fileName), "UTF-8");
            log.info("钱包内容: {}", content);

            Credentials credentials = WalletUtils.loadCredentials(wallet_pwd, wallet_filepath + fileName);
            log.info("凭证地址: {}", credentials.getAddress());
            log.info("凭证私钥: {}", credentials.getEcKeyPair().getPrivateKey());
            log.info("凭证公钥: {}", credentials.getEcKeyPair().getPublicKey());
        } catch (CipherException | IOException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载钱包
     */
    public Credentials loadWallet(String fileName) {
        try {
            Credentials credentials = WalletUtils.loadCredentials(wallet_pwd, wallet_filepath + fileName);
            log.info("凭证地址: {}", credentials.getAddress());
            log.info("凭证私钥: {}", credentials.getEcKeyPair().getPrivateKey());
            log.info("凭证公钥: {}", credentials.getEcKeyPair().getPublicKey());
            return credentials;
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 部署合约
     */
    public void deployContract() {
        try {
            log.info("====================开始部署合约====================");
            HumanStandardToken contract = HumanStandardToken.deploy(
                    web3j,
                    this.loadWallet(""),
                    ManagedTransaction.GAS_PRICE,
                    Contract.GAS_LIMIT,
                    BigInteger.valueOf(1_000_000),
                    "CNet5G",
                    BigInteger.valueOf(18),
                    "NE5G").send();
            log.info("智能合约已部署到地址：" + contract.getContractAddress());
            log.info("合约详情请查看：https://rinkeby.etherscan.io/address/" + contract.getContractAddress());
            log.info("Value stored in remote smart contract: " + contract.greet().send());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解锁账户
     */
//    public Boolean unLockAccount() {
//        try {
//            PersonalUnlockAccount personalUnlockAccount = web3j.personalUnlockAccount(
//                    wallet_test,
//                    "Jason123",
//                    BigInteger.ONE).sendAsync().get(5, TimeUnit.MINUTES);
//            Boolean isUnlocked = personalUnlockAccount.accountUnlocked();
//            log.info("账户是否已解锁？---{}", isUnlocked);
//            return isUnlocked;
//        } catch (TimeoutException | InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 新建交易事务
     */
    public Transaction createTx(String toAddress) {
        try {
            // 获取下一个有效nonce
            EthGetTransactionCount ethGetTxCount = web3j.ethGetTransactionCount(
                    wallet_test,
                    DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger nonce = ethGetTxCount.getTransactionCount();
            log.info("随机数nonce：{}", nonce);

            Transaction tx = Transaction.createFunctionCallTransaction(
                    wallet_test,
                    nonce,
                    Numeric.toBigInt("0x9184e72a000"),
                    Numeric.toBigInt("0x76c0"),
                    toAddress,
                    Numeric.toBigInt(Numeric.toHexString("1".getBytes())),
                    Numeric.toHexString("hello eth".getBytes())
            );
            log.info("交易金额：{}", Numeric.toBigInt(tx.getValue()));
            return tx;

//            // 创建交易 -> toAddress
//            RawTransaction rawTx = RawTransaction.createEtherTransaction(
//                    nonce,
//                    BigInteger.ONE,
//                    BigInteger.TEN,
//                    toAddress,
//                    BigInteger.valueOf(100));
//            log.info("交易金额：{}", rawTx.getValue());
//
//            // 对交易进行签名加密
//            byte[] signedMessage = TransactionEncoder.signMessage(rawTx, this.loadWallet("360e5b31c324843cd57a12a8f9bd740b4d50d447.json"));
//            String hexValue = Numeric.toHexString(signedMessage);
//            log.info("交易订单号：{}", hexValue);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 估算交易消耗gas
     */
    public void getEstimateGas(Transaction tx) {
        try {
            EthEstimateGas gas = web3j.ethEstimateGas(tx).sendAsync().get(5, TimeUnit.MINUTES);
            log.info("此次交易需要gas：{}", gas.getAmountUsed());
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询钱包余额
     */
    public BigInteger getBalance(String address) {
        try {
            EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            log.info("钱包余额: {}", balance.getBalance());
            return balance.getBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送交易
     */
    public String sendTx(Transaction tx) {
        try {
            EthSendTransaction sendTx = web3j.ethSendTransaction(tx).send();
            log.info("交易结果：{}", Numeric.toBigInt(sendTx.getResult()));
            log.info("交易hash值：{}", sendTx.getTransactionHash());
            return sendTx.getTransactionHash();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据hash查询交易
     */
    public void getTxByHash(String hash) {
        try {
            EthTransaction ethTx = web3j.ethGetTransactionByHash(hash).send();
            log.info("交易详情：{}", ethTx);
            log.info("交易gas值：{}", ethTx.getResult().getGas());
            log.info("交易金额：{}", ethTx.getResult().getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 交易
     */
    public void tx() {
        // 1.解锁账户
//        if (this.unLockAccount()) {
            // 2.创建交易事务
            Transaction tx = this.createTx("0x360e5b31c324843cd57a12a8f9bd740b4d50d447");
            // 3.估算gas
            this.getEstimateGas(tx);
            // 4.查询余额
            this.getBalance(wallet_test);
            // 5.发送交易
            String txHash = this.sendTx(tx);
            // 6.验证交易
            this.getTxByHash(txHash);
//        }
    }
}
