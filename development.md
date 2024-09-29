# 四、开发手册

## 1、简介
&emsp;&emsp;本项目基于"信息安全导论"课程第5次课讲述的S-DES算法，实现了S-DES算法的加解密、暴力破解密钥等功能，并在此基础上扩展了基于ASCII编码和Unicode编码对文本进行加解密。此外我们还设计了简洁的GUI界面,使用户能够以简便的方式使用。

## 2、项目结构
```plaintext                  
├── src
│    ├── DES
│    │    ├── SDES_.java            - SDES算法实现
│    │    ├── SDESGUI.java          - 界面设计  
│    └────└── Main.java             - 程序入口 
└── README.md                       
```
## 3、S-Des算法实现——S_Des.java
&emsp;&emsp;S-Des算法实现位于src/S_Des.java中。其包含了SDES算法所有功能的实现，包括加解密、暴力破解等功能。

### 3.1、初始设定
3.1.1分组长度：8-bit

3.1.2密钥长度：10-bit

3.1.3转换装置设定：基于作业文档设定所有置换盒，包括生成子密钥需要的密钥初始置换盒、密钥压缩置换盒、左移置换盒，初始置换和最终置换需要的初始置换盒、最终置换盒，轮函数需要的扩展置换盒、替换盒、直接置换盒。

### 3.2、 基本函数的功能介绍
1、String shift(String inSequence)：对输入序列进行左移一位的操作，返回左移后的序列

2、String pBox(String inSequence,int outNum,int[] transform)： 对输入序列按照给定置换盒进行置换操作，支持扩展置换和压缩置换，返回置换后的序列

3、String sBox(String inSequence,int[][] transform)： 对输入序列按照给定替换盒进行替换操作，返回替换后的序列

4 、getSubKey(String key)： 根据输入的密钥生成两个子密钥

5、String xor(String inSequence1,String inSequence2,int length)： 对输入的两个序列进行异或操作，返回结果

6、roundFun(String inSequence,String key)： 轮函数，根据密钥对输入的序列进行轮加密

### 3.3、 加解密功能的实现
加密函数：String s_des_encrypt(String plaintext,String key)
&emsp;根据密钥对输入明文序列进行加密，返回密文

解密函数：String s_des_decrypt(String ciphertext,String key)
&emsp;根据密钥对输入密文序列进行解密，返回明文


&emsp;&emsp;加密函数与解密函数类似，均分为子密钥生成、初始置换、f<sub>k</sub> S-DES函数、左右互换、f<sub>k</sub> S-DES函数、最终置换这几个步骤，只是f S-DES函数使用的子密钥k不同。

&emsp;&emsp;以下为加密函数的具体实现：
```plaintext
    public static String s_des_encrypt(String plaintext,String key){
        getSubKey(key);//获取子密钥
        String sequence1=pBox(plaintext,8,initialPBox);//初始置换
        String sequence2=roundFun(sequence1,subKey[0]);//fk1
        String sequence3=sequence2.substring(4)+sequence2.substring(0,4);//swap
        String sequence4=roundFun(sequence3,subKey[1]);//fk2
        return pBox(sequence4,8,finalPBox);//最终置换
    }
```
&emsp;&emsp;其中轮函数roundFun(String inSequence,String key)的具体实现如下：
```plaintext
    public static String roundFun(String inSequence,String key){
        String rightPart=inSequence.substring(4);//获取输入序列的右半部分
        String expendPart=pBox(rightPart,8,epBox);//对右半部分进行扩展置换
        String encryptPart=xor(expendPart,key,8);//对扩展后的序列加轮密钥
        String sBoxPart=sBox(encryptPart.substring(0,4),sBox1)+sBox(encryptPart.substring(4),sBox2);//对轮加密后的序列进行替换
        String spBoxPart=pBox(sBoxPart,4,spBox);//对替换后的序列进行直接置换
        String leftPart=xor(inSequence.substring(0,4),spBoxPart,4);//直接置换后的序列与输入序列的左半部分进行异或操作
        return leftPart+rightPart;//轮函数完成，返回得到的序列
    }
```
### 3.4、 暴力破解功能的实现
&emsp;&emsp;暴力破解思路：对于给定的明密文对，遍历所有的十位二进制序列，寻找到所有能使明文加密成对于密文的密钥

&emsp;&emsp;以下为暴力破解的具体实现：
```plaintext
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
```
### 3.5、 扩展功能的实现
&emsp;&emsp;本项目不仅实现了对二进制序列的加解密功能，还能对各种字符组成的字符串进行加解密。

&emsp;&emsp;基于两个简单的函数String charToBinary(char c,int num) 将字符转化为长度为num的二进制序列以及char BinaryToChar(String s) 将二进制序列转化为字符，我们扩展出了以下两对加解密函数：

1、String s_des_encrypt_ascii(String plaintext,String key)

&emsp;String s_des_decrypt_ascii(String ciphertext,String key)

2、String s_des_encrypt_unicode(String plaintext,String key)

&emsp;String s_des_decrypt_unicode(String ciphertext,String key)

&emsp;&emsp;通过将字符串的每个字符转化成二进制序列，我们可以依次对得到的二进制序列进行加解密，从而完成对整个字符串的加密，如通过ASCII编号转化进行加密的函数如下：
```plaintext
    public static String s_des_encrypt_ascii(String plaintext,String key){
        String ciphertext="";
        for(int i=0;i<plaintext.length();i++) {
            String binaryStr=charToBinary(plaintext.charAt(i),8);
            ciphertext+=BinaryToChar(s_des_encrypt(binaryStr,key));
        }
        return ciphertext;
    }
```
## 4、编译与运行
1. 使用Java编译器编译项目：
```bash
   javac Main.java
```

2. 运行项目：
```bash
   java Main
```





