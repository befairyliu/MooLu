package com.agl.rsa;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

public class RSATest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
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
        System.out.println("The modulus is : "+modulus); 
        System.out.println();  
        //公钥指数  
        String public_exponent = publicKey.getPublicExponent().toString();  
        System.out.println("The public_exponent is : "+public_exponent);  
        System.out.println();
        //私钥指数  
        String private_exponent = privateKey.getPrivateExponent().toString();  
        System.out.println("The public_exponent is : "+private_exponent); 
        System.out.println();
        //明文  
        //String plaintext = "123456789";
        String plaintext = "";
        String srcPath = "file/EntityList-1.5.6-android-p2p-test.xml";
        
        plaintext = RSAUtils.readFile(srcPath);
        System.out.println("The plain text is: "+plaintext);
        System.out.println();
        
        //使用模和指数生成公钥和私钥  
        RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);  
        RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent); 
        
        //加密后的密文  
        String encypttext = "";
		try {
			encypttext = RSAUtils.encryptByPublicKey(plaintext, pubKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        System.err.println("The encrypt text is: "+ encypttext);  
        System.err.println();  
        //解密后的明文  
        try {
        	plaintext = RSAUtils.decryptByPrivateKey(encypttext, priKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        System.err.println("The plain text's length is : "+plaintext.length());  
        System.err.println("The plain text  is: "+plaintext);  
	}

}
