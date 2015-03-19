package com.agl.rsa;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;

public class RSATest {



    /**
     * @param args
     */
    public static void main(String[] args) {

        Date startDate0 = new Date();
        //
		/*
		 *
		//--------------KeyPairGenerator to generate rsa key----------
		HashMap<String, Object> map = null ;
		try {
			map = RSAUtils.getKeys();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        //生成公钥和私钥
        RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");

        //模
        String modulus = publicKey.getModulus().toString();
        //System.out.println("The modulus is : "+modulus);
        //System.out.println();
        //公钥指数  65537
        String public_exponent = publicKey.getPublicExponent().toString();
        //System.out.println("The public_exponent is : "+public_exponent);
        //System.out.println();
        //私钥指数
        String private_exponent = privateKey.getPrivateExponent().toString();
        //System.out.println("The private_exponent is : "+private_exponent);
        //System.out.println();

        //明文
        //String plaintext = "123456789好人啊，杭州欢迎您";
        String srcPath = "file/EntityList-1.5.6-android-p2p-test.xml";
        String plaintext = RSAUtils.readFile(srcPath);
        System.out.println("The plain text is: "+plaintext);
        System.out.println();

        //使用模和指数生成公钥和私钥
        RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
        RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);

        //Date startDate = new Date();

        //加密后的密文
        String encypttext = "";
		try {
			encypttext = RSAUtils.encryptByPublicKey(plaintext, pubKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.err.println("The encrypt text is: "+ encypttext);
        System.err.println();
        //解密后的明文
        try {
        	plaintext = RSAUtils.decryptByPrivateKey(encypttext, priKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

        System.err.println("The cost time is : "+ (new Date().getTime() - startDate0.getTime()));

        System.err.println("The plain text's length is : "+plaintext.length());
        System.err.println("The plain text  is: "+plaintext);
        */



        //----------------------load key from file----------------------------
        String public_path = "file/public_key.der";
        String private_path = "file/private_key.p12";
        String password  = "123456";
        PrivateKey prikey = null;
        PublicKey pubkey = null;


        //get private key from xxxx.p12 file
        try {
            prikey = RSAUtils.loadPriKey(private_path, password);
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get public key from xxxx.der file
        try {
            pubkey = RSAUtils.loadPubKey(public_path);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //-------------------------------------------------
        /**
         * 数字签名部分算法
         *
         * 使用UTF-8，中文一个汉字占3个字节，一个字母占1个字节；如果是unicode,则都是占2个字节。
         */

        System.err.println("The init cost time is : "+ (new Date().getTime() - startDate0.getTime()));
        //String signData = "hello数字签名哟";
        String srcPath = "file/EntityList-1.5.6-android-p2p-test.xml";
        String signData = RSAUtils.readFile(srcPath);
        System.out.println("数字签名原文为: "+signData);
        byte[] datas = signData.getBytes();
        Date startDate = new Date();
        byte[] sign = null;
        try {
            sign = RSAUtils.sign(datas, prikey.getEncoded());
            System.out.println("数字签名为: "+sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("The sign cost time is : "+ (new Date().getTime() - startDate.getTime()));
        Date startDate2 = new Date();
        //for verify
        boolean status = false;
        try {
            status = RSAUtils.verify(signData.getBytes(), pubkey.getEncoded(), sign);
            System.out.println("数字签名stauts为: "+status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.err.println("The verify cost time is : "+ (new Date().getTime() - startDate2.getTime()));





    }



}
