# 一、开发手册

## 1、简介

&emsp;&emsp;本项目基于"信息安全导论"课程第5次课讲述的S-DES算法，实现了S-DES算法的加解密、暴力破解密钥等功能，并在此基础上扩展了基于ASCII编码和Unicode编码对文本进行加解密。此外我们还设计了简洁的GUI界面,使用户能够以简便的方式使用。

## 2、项目结构

```plaintext                  
├── picture                         - 存放结果数据和图片
├── src
│    ├── DES
│    │    ├── S_DES.java            - S-DES算法实现
│    │    └── SDESGUI.java          - 界面设计   
└── README.md   
└── deevelopment.md                 - 开发手册
└── Test Results.md                 - 测试结果
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





# 二、用户指南

## 1、应用简介

-   S-DES 加解密集成系统是一个图形用户界面（GUI）应用程序，旨在帮助用户使用简化数据加密标准（S-DES）进行加密和解密操作。该系统还提供暴力破解功能，允许用户通过已知的明文和密文来破解密钥。

## 2、功能概述

1. **加密和解密**：用户可以选择输入类型（ASCII、二进制、Unicode字符），输入明文或密文以及密钥，然后进行加密或解密操作。
2. **暴力破解**：用户可以输入已知的明文和密文，系统将尝试通过暴力破解找到对应的密钥。

可以选择不同的功能模块，包括：**加解密模式、暴力破解模式**

**加解密模式界面：**

<img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\主界面.png" style="zoom:50%;" />

**暴力破解模式界面：**

<img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\暴力破解用户界面.png" style="zoom:50%;" />



## 3、使用指南

**启动应用**

1. 运行 `SDESGUI` 类的 `main` 方法启动应用程序。
2. 应用程序启动后，将显示主界面。

##### 加解密模式

- 选择输入类型

  - 在“输入类型”下拉菜单中选择输入类型（ASCII、二进制、Unicode字符）。

- 输入明文或密文

  - 在“请输入明文或密文”文本框中输入要加密或解密的文本。

- 输入密钥

  - 在“请输入密钥”文本框中输入10位二进制密钥。

- 选择操作

  点击“加密”按钮进行加密操作。

  点击“解密”按钮进行解密操作。

- 查看结果

  - 加密或解密的结果将显示在“加密或解密的结果”文本框中。

##### 破解模式

- 输入已知明文
  - 在“请输入已知明文”文本框中输入已知的明文。
- 输入已知密文
  - 在“请输入已知密文”文本框中输入已知的密文。
- 开始破解
  - 点击“开始破解”按钮，系统将尝试通过暴力破解找到对应的密钥。
- 查看破解结果
  - 破解的密钥将显示在“暴力破解的密钥”文本区域中。
  - 系统将弹出消息框显示破解所耗时间。

##### 模式切换

- 切换到加解密模式
  - 点击“加解密模式”按钮，切换到加解密界面。
- 切换到破解模式
  - 点击“破解模式”按钮，切换到暴力破解界面。

#### 注意事项

- 确保输入的明文为8位二进制字符串，或不限制长度的ASCII字符串，或不限制长度的unicode字符串，密钥为10位二进制字符串。
- 输入不符合要求时，系统将弹出错误提示。

#### 反馈与支持

如果您遇到任何问题或需要帮助，请随时联系我们。感谢您使用SDES算法加解密工具！

祝您使用愉快！



# 测试结果

## 第一关：基本测试

- 根据S-DES算法编写和调试程序，提供GUI解密支持用户交互。输入可以是8bit的数据和10bit的密钥，输出是8bit的密文。

**S-DES算法流程：**

<img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\algorithm.png" style="zoom:50%;" />

### **主界面**

可以选择不同的功能模块，包括：**加解密模式、暴力破解模式**

<img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\主界面.png" style="zoom:50%;" />

### 加密操作

- 测试密钥均为0000011111

- **二进制类型的加密**

  明文为10101010

  <img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\test1_binary_encode_result.png" style="zoom:50%;" />

**ASCII类型的加密**

明文为abcdefg

<img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\test1_ASCII_encode_result.png" style="zoom:50%;" />

### 解密操作

- 测试密钥均为0000011111

- **二进制类型的解密**

密文为11101000

<img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\test1_binary_decode_result.png" style="zoom:50%;" />

- **ASCII类型的解密**

<img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\test1_ASCII_decode_result.png" style="zoom:50%;" />

## 第二关：交叉测试

- 考虑到是**算法标准**，所有人在编写程序的时候需要使用相同算法流程和转换单元(P-Box、S-Box等)，以保证算法和程序在异构的系统或平台上都可以正常运行。
- 设有A和B两组位同学(选择相同的密钥K)；则A、B组同学编写的程序对明文P进行加密得到相同的密文C；或者B组同学接收到A组程序加密的密文C，使用B组程序进行解密可得到与A相同的P。

## 第三关：扩展功能

考虑到向实用性扩展，加密算法的数据输入可以是ASII编码字符串(分组为1 Byte)，对应地输出也可以是ACII字符串(很可能是乱码)。

我们扩展了对ASCII码和Unicode码的加解密算法。

- 当明文或者密文输入格式不符合标准输入格式时，都会视为拓展输入，此时将输入作为一个长为n的字符串，n为输入的长度。
- 将每一位字符先转化为8位的二进制ASCII码，再对8位二进制的ASCII码进行标准格式的加密，再将加密结果视作ASCII码然后转化为字符。
- 或将每一位字符先转化为16位的二进制Unicode码，再对16位二进制的Unicode码分组进行标准格式的加密，再将加密结果视作Unicode码然后转化为字符。
- 将每一位转化的结果进行组合，得到新的字符串即为密文。

### 功能实现

- GUI界面提供了ASCII或二进制编码或Unicode编码的按钮，选择可以切换加密模式，ASCII模式下输出密文为ASCII对应的字符串，Unicode模式下输出密文为Unicode对应的字符串。

- #### ASCII类型加解密模式

- **加密**

<img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\test3_ASCII_encode_result.png" style="zoom:50%;" />

- **解密**

<img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\test3_ASCII_decode_result.png" style="zoom:50%;" />

#### Unicode类型加解密模式

- **加密**

<img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\test3_Unicode_encode_result.png" style="zoom:50%;" />

- **解密**

  <img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\test3_Unicode_decode_result.png" style="zoom:50%;" />

## 第四关：暴力破解

- 假设你找到了使用相同密钥的明、密文对(一个或多个)，请尝试使用暴力破解的方法找到正确的密钥Key。在编写程序时，你也可以考虑使用多线程的方式提升破解的效率。请设定时间戳，用视频或动图展示你在多长时间内完成了暴力破解。

### 暴力破解用户界面

<img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\暴力破解用户界面.png" style="zoom:50%;" />

- 用户输入已知明文和已知密文，点击开始破解，在一段时间后便可得到暴力破解的密钥。

![](D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\force.gif)

## 第5关：封闭测试

- 根据第4关的结果，进一步分析，对于你随机选择的一个明密文对，是不是有不止一个密钥Key？进一步扩展，对应明文空间任意给定的明文分组P~~n~~，是否会出现选择不同的密钥K~~i~~ ≠ K~~j~~加密得到相同密文C~~n~~的情况？

| 明文     |   密文   | 加密使用的密钥 | 破解的密钥 | 破解时间 |
| -------- | :------: | :------------: | :--------: | :------: |
| 10101010 | 11101000 |   0000011111   | 0000011111 | 2.7046ms |
|          |          |                | 0100011111 |          |
|          |          |                | 1011001011 |          |
|          |          |                | 1111001011 |          |
| 11110000 | 11101110 |   0101010101   | 0001010101 | 1.9388ms |
|          |          |                | 0010001100 |          |
|          |          |                | 0101010101 |          |
|          |          |                | 0110001100 |          |
| 10010010 | 11001011 |   0101010101   | 0001010101 | 4.6399ms |
|          |          |                | 0010101100 |          |
|          |          |                | 0101010101 |          |
|          |          |                | 0110101100 |          |
|          |          |                | 1010111010 |          |
|          |          |                | 1110111010 |          |
| 00110011 | 10001100 |   0100011111   | 0000011111 | 4.2784ms |
|          |          |                | 0100011111 |          |
|          |          |                | 1001110000 |          |
|          |          |                | 1010001001 |          |
|          |          |                | 1101110000 |          |
|          |          |                | 1110001001 |          |

- 由分析结果来看，对于随机选择的一个明密文对，有不止一个密钥Key。进一步发现，对同一明文使用不同密钥进行加密，可能会得到相同的密文。
- 即：对于明文空间内任意给定的明文分组，确实存在可能性，即选择不同的密钥，加密可以得到相同的密文，如下所示。

<img src="D:\Dowmloads\大三上课程\信息安全导论\CODE\S-DES\picture\test5.png" style="zoom:50%;" />

- 明文在测试1中选择10101010，密钥选择0000011111，密文为11101000
- 之后我们选择密钥0100011111，明文不变，加密的密文仍为11101000



## Summary

- S-DES的密钥空间只有10-bit，共1024种情况，这样的密码系统显然安全性不足

- S-DES中的置换盒，替换盒，轮函数和移位都展现了密码学的精华，即扩散和混淆，通过这一系列的变化能够很好地抹去明文和密文的统计特征，密文和密钥的统计关系

  





