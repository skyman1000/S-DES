package DES;

import java.util.ArrayList;
import java.util.List;

public class S_Des {
    static int[] pKey={3,5,2,7,4,10,1,9,8,6};//密钥初始置换盒
    static int[] pSubKey={6,3,7,4,8,5,10,9};//密钥压缩置换盒
    static int[] initialPBox={2,6,3,1,4,8,5,7};//初始置换盒
    static int[] finalPBox={4,1,3,5,7,2,8,6};//最终置换盒
    static int[] epBox={4,1,2,3,2,3,4,1};//扩展置换盒
    static int[][] sBox1 ={{1,0,3,2},{3,2,1,0},{0,2,1,3},{3,1,0,2}};//替换盒1
    static int[][] sBox2={{0,1,2,3},{2,3,1,0},{3,0,1,2},{2,1,0,3}};//替换盒2
    static int[] spBox={2,4,3,1};//直接置换盒
    static String[] subKey=new String[2];//子密钥

    public static String shift(String inSequence){//对输入序列进行左移一位的操作，返回左移后的序列
        return inSequence.substring(1)+inSequence.charAt(0);
    }
    public static String pBox(String inSequence,int outNum,int[] transform){//对输入序列按照给定置换盒进行置换操作，支持扩展置换和压缩置换，返回置换后的序列
        String outSequence="";
        for(int i=0;i<outNum;i++)
        {
            outSequence+=inSequence.charAt(transform[i]-1);
        }
        return outSequence;
    }

    public static String sBox(String inSequence,int[][] transform){//对输入序列按照给定替换盒进行替换操作，返回替换后的序列
        int row=(inSequence.charAt(0)-'0')*2+inSequence.charAt(3)-'0';
        int colum=(inSequence.charAt(1)-'0')*2+inSequence.charAt(2)-'0';
        String outSequence=Integer.toBinaryString(transform[row][colum]);
        while (outSequence.length() < 2) {
            outSequence = "0" + outSequence;
        }
        return outSequence;
    }

    public static void getSubKey(String key) {//根据输入的密钥生成两个子密钥
        String iniKey=pBox(key,10,pKey);
        String leftKey = shift(iniKey.substring(0, 5));
        String rightKey = shift(iniKey.substring(5));
        for(int i=0;i<2;i++) {
            subKey[i]=pBox(leftKey+rightKey,8,pSubKey);
            leftKey=shift(leftKey);
            rightKey=shift(rightKey);
        }
    }

    public static String xor(String inSequence1,String inSequence2,int length) {//对输入的两个序列进行异或操作，返回结果
        String outSequence="";
        for(int i=0;i<length;i++)
        {
            if(inSequence1.charAt(i)==inSequence2.charAt(i))
                outSequence+="0";
            else
                outSequence+="1";
        }
        return outSequence;
    }
    public static String roundFun(String inSequence,String key){
        String rightPart=inSequence.substring(4);//获取输入序列的右半部分
        String expendPart=pBox(rightPart,8,epBox);//对右半部分进行扩展置换
        String encryptPart=xor(expendPart,key,8);//对扩展后的序列加轮密钥
        String sBoxPart=sBox(encryptPart.substring(0,4),sBox1)+sBox(encryptPart.substring(4),sBox2);//对轮加密后的序列进行替换
        String spBoxPart=pBox(sBoxPart,4,spBox);//对替换后的序列进行直接置换
        String leftPart=xor(inSequence.substring(0,4),spBoxPart,4);//直接置换后的序列与输入序列的左半部分进行异或操作
        return leftPart+rightPart;//轮函数完成，返回得到的序列
    }
    public static String charToBinary(char c,int num){//将字符转化为长度为num的二进制序列
        String res=Integer.toBinaryString(c);
        while(res.length()<num)
                res="0"+res;
        return res;
    }
    public static char BinaryToChar(String s) {
        return (char)Integer.parseInt(s,2);
    }//将二进制序列转化为字符
    public static String s_des_encrypt(String plaintext,String key){
        getSubKey(key);//获取子密钥
        String sequence1=pBox(plaintext,8,initialPBox);//初始置换
        String sequence2=roundFun(sequence1,subKey[0]);//fk1
        String sequence3=sequence2.substring(4)+sequence2.substring(0,4);//swap
        String sequence4=roundFun(sequence3,subKey[1]);//fk2
        return pBox(sequence4,8,finalPBox);//最终置换
    }
    public static String s_des_decrypt(String ciphertext,String key){
        getSubKey(key);//获取子密钥
        String sequence1=pBox(ciphertext,8,initialPBox);//初始置换
        String sequence2=roundFun(sequence1,subKey[1]);//fk1
        String sequence3=sequence2.substring(4)+sequence2.substring(0,4);//swap
        String sequence4=roundFun(sequence3,subKey[0]);//fk2
        return pBox(sequence4,8,finalPBox);//最终置换
    }
    public static String s_des_encrypt_ascii(String plaintext,String key){
        String ciphertext="";
        for(int i=0;i<plaintext.length();i++) {
            String binaryStr=charToBinary(plaintext.charAt(i),8);
            ciphertext+=BinaryToChar(s_des_encrypt(binaryStr,key));
        }
        return ciphertext;
    }
    public static String s_des_decrypt_ascii(String ciphertext,String key){
        String plaintext="";
        for(int i=0;i<ciphertext.length();i++) {
            String binaryStr=charToBinary(ciphertext.charAt(i),8);
            plaintext+=BinaryToChar(s_des_decrypt(binaryStr,key));
        }
        return plaintext;
    }
    public static List<String> forcefullyCrack(String plaintext, String ciphertext) {
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < 1024; i++) {
            String key = Integer.toBinaryString(i);//遍历所有十位二进制序列
            while (key.length() < 10)
                key = "0" + key;
            if (s_des_encrypt(plaintext, key).equals(ciphertext)) {//对输入的明文序列进行加密能得到对应的密文，说明该密钥为正确的密钥
                keys.add(key); // 将密钥添加到列表中
                System.out.println(key); // 在控制台打印密钥
            }
        }
        return keys;
    }

    public static String s_des_encrypt_unicode(String plaintext,String key){
        String ciphertext="";
        for(int i=0;i<plaintext.length();i++) {
            String binaryStr=charToBinary(plaintext.charAt(i),16);
            String encryptBinaryStr=s_des_encrypt(binaryStr.substring(0,8),key)+s_des_encrypt(binaryStr.substring(8),key);
            ciphertext+=(char)Integer.parseInt(encryptBinaryStr,2);
        }
        return ciphertext;
    }
    public static String s_des_decrypt_unicode(String ciphertext,String key){
        String plaintext="";
        for(int i=0;i<ciphertext.length();i++) {
            String binaryStr=charToBinary(ciphertext.charAt(i),16);
            String encryptBinaryStr=s_des_decrypt(binaryStr.substring(0,8),key)+s_des_decrypt(binaryStr.substring(8),key);
            plaintext+=(char)Integer.parseInt(encryptBinaryStr,2);
        }
        return plaintext;
    }

}
