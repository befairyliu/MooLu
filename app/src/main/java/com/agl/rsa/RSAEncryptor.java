package com.agl.rsa;

import android.util.Base64;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.*;
import java.io.*;
public class RSAEncryptor {
    private static final String KEY_FILENAME = "c:\\mykey.dat";
    private static final String OTHERS_KEY_FILENAME = "c:\\Otherskey.dat";
    // private static final int KEY_SIZE = 1024;
    // private static final int BLOCK_SIZE = 117;
    // private static final int OUTPUT_BLOCK_SIZE = 128;

    //RSA key 是多少位的
    private static final int KEY_SIZE = 2048;
    //一次RSA加密操作所允许的最大长度
    //这个值与 KEY_SIZE 已经padding方法有关。因为 1024的key的输出是128，2048key输出是256字节
    //但是11个字节用于保存padding信息了，所以最多可用的就只有245字节了。
    private static final int BLOCK_SIZE = 245;

    private static final int OUTPUT_BLOCK_SIZE = 256;
    private SecureRandom secrand;
    private Cipher rsaCipher;
    private KeyPair keys;
    private Map<String, Key> allUserKeys;
    public RSAEncryptor() throws Exception {
        try {
            allUserKeys = new HashMap<String, Key>();
            secrand = new SecureRandom();
            //SunJCE Provider 中只支持ECB mode，试了一下只有PKCS1PADDING可以直接还原原始数据，
            //NOPadding导致解压出来的都是blocksize长度的数据，还要自己处理
            //参见另外根据 Open-JDK-6.b17-src（）
            // 中代码的注释，使用RSA来加密大量数据不是一种标准的用法。所以现有实现一次doFinal调用之进行一个RSA操作，
            //如果用doFinal来加密超过的一个操作所允许的长度数据将抛出异常。
            //根据keysize的长度，典型的1024个长度的key和PKCS1PADDING一起使用时
            //一次doFinal调用只能加密117个byte的数据。（NOPadding 和1024 keysize时128个字节长度）
            //（2048长度的key和PKCS1PADDING 最多允许245字节一次）
            //想用来加密大量数据的只能自己用其他办法实现了。可能RSA加密速度比较慢吧，要用AES才行
            rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace(); throw e;
        }

        ObjectInputStream in;
        try {
            in = new ObjectInputStream(new FileInputStream(KEY_FILENAME));
        } catch (FileNotFoundException e) {

            if (false == GenerateKeys()) {
                throw e;
            }
            LoadKeys();
            return;
        }

        keys = (KeyPair) in.readObject();
        in.close();
        LoadKeys();
    }

    /* * 生成自己的公钥和私钥 */
    private Boolean GenerateKeys() {
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            // secrand = new SecureRandom();
            // sedSeed之后会造成 生成的密钥都是一样的
            // secrand.setSeed("chatencrptor".getBytes());
            // 初始化随机产生器 //key长度至少512长度，不过好像说现在用2048才算比较安全的了
            keygen.initialize(KEY_SIZE, secrand);
            // 初始化密钥生成器 keys = keygen.generateKeyPair();
            // 生成密钥组 AddKey("me", EncodeKey(keys.getPublic()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); return false;
        }

        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(new FileOutputStream(KEY_FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } try {
            out.writeObject(keys);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } return true;
    }

    public String EncryptMessage(String toUser, String Message) throws IOException {
        Key pubkey = allUserKeys.get(toUser);
        if ( pubkey == null ) {
            throw new IOException("NoKeyForThisUser") ;
        } try {
            //PublicKey pubkey = keys.getPublic();
            rsaCipher.init(Cipher.ENCRYPT_MODE, pubkey, secrand);
            //System.out.println(rsaCipher.getBlockSize()); 返回0，非block 加密算法来的？
            //System.out.println(Message.getBytes("utf-8").length);
            //byte[] encryptedData = rsaCipher.doFinal(Message.getBytes("utf-8"));
            byte[] data = Message.getBytes("utf-8");
            int blocks = data.length / BLOCK_SIZE ;
            int lastBlockSize = data.length % BLOCK_SIZE ;
            byte [] encryptedData = new byte[ (lastBlockSize == 0 ? blocks : blocks + 1)* OUTPUT_BLOCK_SIZE];
            for (int i=0; i < blocks; i++) {
                //int thisBlockSize = ( i + 1 ) * BLOCK_SIZE > data.length data.length - i * BLOCK_SIZE : BLOCK_SIZE ;
                rsaCipher.doFinal(data,i * BLOCK_SIZE, BLOCK_SIZE, encryptedData ,i * OUTPUT_BLOCK_SIZE);
            } if (lastBlockSize != 0 ){
                rsaCipher.doFinal(data, blocks * BLOCK_SIZE, lastBlockSize,encryptedData ,blocks * OUTPUT_BLOCK_SIZE);
            }
            //System.out.println(encrypted.length); 如果要机密的数据不足128/256字节，加密后补全成为变为256长度的。
            //数量比较小时，Base64.GZIP产生的长度更长，没什么优势
            //System.out.println(Base64.encodeBytes(encrypted,Base64.GZIP).length());
            //System.out.println(Base64.encodeBytes(encrypted).length());
            //System.out.println (rsaCipher.getOutputSize(30));
            //这个getOutputSize 只对 输入小于最大的block时才能得到正确的结果。其实就是补全 数据为128/256 字节
            //return Base64.encodeBytes(encryptedData);
            return Base64.encodeToString(encryptedData,1);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new IOException("InvalidKey") ;
        }catch (ShortBufferException e) {
            e.printStackTrace();
            throw new IOException("ShortBuffer");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IOException("UnsupportedEncoding");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new IOException("IllegalBlockSize") ;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new IOException("BadPadding") ;
        }finally {
            //catch 中 return 或者throw之前都会先调用一下这里
        }
    }

    public String DecryptMessage(String Message) throws IOException {
        //byte[] decoded = Base64.decode(Message);
        byte[] decoded = Base64.decode(Message,1);
        PrivateKey prikey = keys.getPrivate();
        try {
            rsaCipher.init(Cipher.DECRYPT_MODE, prikey, secrand);
            int blocks = decoded.length / OUTPUT_BLOCK_SIZE;
            ByteArrayOutputStream decodedStream = new ByteArrayOutputStream(decoded.length);
            for (int i =0 ;i < blocks ; i ++ ) {
                decodedStream.write (rsaCipher.doFinal(decoded,i * OUTPUT_BLOCK_SIZE, OUTPUT_BLOCK_SIZE));
            }
            return new String(decodedStream.toByteArray(), "UTF-8");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new IOException("InvalidKey");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IOException("UnsupportedEncoding");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new IOException("IllegalBlockSize");
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new IOException("BadPadding");
        } finally {
            // catch 中 return 或者throw之前都会先调用一下这里。
        }
    }

    public boolean AddKey(String user, String key) {
        PublicKey publickey;
        try {
            publickey = DecodePublicKey(key);
        } catch (Exception e) {
            return false;
        }

        allUserKeys.put(user, publickey);
        SaveKeys();
        return true;
    }

    private boolean LoadKeys() {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader( new FileInputStream(OTHERS_KEY_FILENAME)));
        } catch (FileNotFoundException e1) {
            // e1.printStackTrace(); return false;
        }

        try {
            allUserKeys.clear();
            String line;
            while ((line = input.readLine()) != null) {
                String[] temp = line.split("\\|");
                String user = temp[0];
                PublicKey key = DecodePublicKey(temp[1]);
                allUserKeys.put(user, key);
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    private boolean SaveKeys() {
        FileWriter output;
        try {
            output = new FileWriter(OTHERS_KEY_FILENAME);
        } catch (IOException e1) {
            // 1.printStackTrace();
            return false;
        }

        try {
            for (String user : allUserKeys.keySet()) {
                Key key = allUserKeys.get(user);
                output.write(user + "|" + EncodeKey(key) + "\n");
            }
        } catch (IOException e1) {
            // 1.printStackTrace();
            return false;
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    /** * 解密base64编码得到公钥 * *
     @param key * 密钥字符串（经过base64编码） *
     @throws Exception
     */
    public static PublicKey DecodePublicKey(String key) throws Exception {
        byte[] keyBytes;
        //keyBytes = Base64.decode(key);
        keyBytes = Base64.decode(key,1);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /** * 解密base64编码得到私钥 * *
     @param key * 密钥字符串（经过base64编码） *
     @throws Exception
     */
    public static PrivateKey DecodePrivateKey(String key) throws Exception {
        byte[] keyBytes;
        //keyBytes = Base64.decode(key);
        keyBytes = Base64.decode(key,1);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
    /** * 编码key为base64字符串 * *
     @return
     */
    public static String EncodeKey(Key key) {
        byte[] keyBytes = key.getEncoded();
        // System.out.print(key.getFormat()) ;
        //String s = Base64.encodeBytes(keyBytes);
        String s = Base64.encodeToString(keyBytes,1);
        return s;
    }
}
