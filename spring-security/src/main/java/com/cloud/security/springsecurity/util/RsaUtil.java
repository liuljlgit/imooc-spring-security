package com.cloud.security.springsecurity.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class RsaUtil {

    public static final String CHARSET = "UTF-8";

    public static final String RSA_ALGORITHM = "RSA";

    public static void createKeys(int keySize) {
        // 为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        // 初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        // 生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        // 得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        log.info("生成公钥：" + publicKeyStr);
        // 得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        log.info("生成私钥：" + privateKeyStr);
    }

    /**
     * 得到公钥
     * @param publicKey  密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 得到私钥
     * @param privateKey  密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            //每个Cipher初始化方法使用一个模式参数opmod，并用此模式初始化Cipher对象。此外还有其他参数，包括密钥key、包含密钥的证书certificate、算法参数params和随机源random。
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥解密
     * @param data
     * @param publicKey
     * @return
     */

    public static String publicDecrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    //rsa切割解码  , ENCRYPT_MODE,加密数据   ,DECRYPT_MODE,解密数据
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;  //最大块
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    //可以调用以下的doFinal（）方法完成加密或解密数据：
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }

    // 简单测试____________
    public static void main(String[] args) throws Exception {
        //生成公私密钥
//        RsaUtil.createKeys(1024);

        //得到公钥和私钥
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCeD3OIrbUGE4_CEGkGmVVLGFMhop9-RvgU18sgEH1T2Dc-2uTAtMUA5vbiPnpD8L4RxVbkMJtlx0ULbrqwCOPEcnM-Ri3h3nR6kRTRDdy82BYiANDV5UTiqZ0lul10rSvf5NjwnSxK5DeRySrOQci6nLGa654paY7RxVHpg3_oqwIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ4Pc4ittQYTj8IQaQaZVUsYUyGin35G-BTXyyAQfVPYNz7a5MC0xQDm9uI-ekPwvhHFVuQwm2XHRQtuurAI48Rycz5GLeHedHqRFNEN3LzYFiIA0NXlROKpnSW6XXStK9_k2PCdLErkN5HJKs5ByLqcsZrrnilpjtHFUemDf-irAgMBAAECgYBUyc0cQLKe89dmLw9n7tEGW6IcpPQR9VgXNjtMlAvm8lGQcjPL_CzB8yP08bb8wyPyvH2EI175FhA_tcKHs_0aCMKBRZvZTVLYQ08SneCRhUZmMBGk3Vix0_Zs3hFaoDKNxstj1O73DUeWLy9wZFtqbNCEGZ-BkIHOqCtlOxxXQQJBAMyiyXYRAhiADfvzMhrhQNt6krHAn9kJnS6Pwmh-d7mWZdJPUYlnXCTO-K4Pg9ZqPWYhscteCePvddSeFHnmgwkCQQDFu-CrqpYhgDlGDjVEsL99jgxzcc9aKjV0s2DmDf6Zt6_jzxIoKsp7flwuM8oz0x-vAuYNUhJMTfRR6h2EY3cTAkEAtPyI5-Z1Dww60gou130_6p4HHjU6ay_ImATHsbmg1ECDf7VtbPRAl8GCShgoxzuhYDIO2MosPy9r7Id_nqzUWQJAfvImxK-JIUrr-lEG2-nJlrcKwVcw80cj-J9tN2WGrnOihdQcaRNH2c7_KV5wk6MYtUhcH_Mv8jMeBzUR7DvbZwJAL6XRW5vQnXd0QgL5s5ZOTi26BQOsY_5MKtudTxTG0hXIxtFtimlIghpBhMmiFKS9gov-zSWylyeQ9sx6PdJdHQ";
        log.info("公钥: \n\r" + publicKey);
        log.info("私钥： \n\r" + privateKey);
        log.info("公钥加密 --- 私钥解密");
        String str = "123456";

        //传入明文和公钥加密,得到密文
        log.info("\r明文：\r\n" + str);
        log.info("\r明文大小：\r\n" + str.getBytes().length);
        String encodedData = RsaUtil.publicEncrypt(str, RsaUtil.getPublicKey(publicKey));
        log.info("密文：\r\n" + encodedData);

        //传入密文和私钥,得到明文
        String decodedData = RsaUtil.privateDecrypt(encodedData, RsaUtil.getPrivateKey(privateKey));
        log.info("解密后文字: \r\n" + decodedData);

    }
}
