package DES;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;


public class SDESGUI {
    public static void main(String[] args) {
        // 创建主框架
        JFrame frame = new JFrame("S-DES 加解密集成系统");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout()); // 使用BorderLayout进行布局

        // 创建卡片面板
        JPanel cardPanel = new JPanel(new CardLayout());

        // 加解密界面
        JPanel encryptDecryptPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 设置间距
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 添加大标题
        JLabel titleLabel = new JLabel("S-DES 加解密集成系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("楷体", Font.BOLD, 30)); // 设置字体和大小
        titleLabel.setForeground(new Color(6, 194, 194, 255)); // 设置颜色
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0,0,50,0)); // 设置标题与顶部的距离
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 8; // 设置组件跨越的网格列数为8
        encryptDecryptPanel.add(titleLabel, gbc);

        // 创建组件
        // 添加选项框
        JLabel inputTypeLabel = new JLabel("输入类型:");
        inputTypeLabel.setFont(new Font("楷体", Font.PLAIN, 25));
        inputTypeLabel.setForeground(new Color(131, 127, 131));
        gbc.gridx = 0;
        gbc.gridy = 1;
        encryptDecryptPanel.add(inputTypeLabel, gbc);

        String[] inputTypes = {"ASCII", "Binary","unicode字符"};
        JComboBox<String> inputTypeComboBox = new JComboBox<>(inputTypes);
        gbc.gridx = 1;
        gbc.gridy = 1;
        encryptDecryptPanel.add(inputTypeComboBox, gbc);

        JLabel inputLabel = new JLabel("请输入明文或密文:");
        inputLabel.setFont(new Font("楷体", Font.PLAIN, 25)); // 设置字体和大小
        inputLabel.setForeground(new Color(131, 127, 131));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        encryptDecryptPanel.add(inputLabel, gbc);

        JTextField inputField = new JTextField(20); // 设置文本框的列数
        gbc.gridx = 1;
        gbc.gridy = 2;
        encryptDecryptPanel.add(inputField, gbc);



        JLabel keyLabel = new JLabel("请输入密钥:");
        keyLabel.setFont(new Font("楷体", Font.PLAIN, 25)); // 设置字体和大小
        keyLabel.setForeground(new Color(131, 127, 131));
        gbc.gridx = 0;
        gbc.gridy = 3;
        encryptDecryptPanel.add(keyLabel, gbc);

        JTextField keyField = new JTextField(20); // 设置文本框的列数
        gbc.gridx = 1;
        gbc.gridy = 3;
        encryptDecryptPanel.add(keyField, gbc);

        JLabel operationLabel = new JLabel("请选择操作:");
        operationLabel.setFont(new Font("楷体", Font.PLAIN, 25)); // 设置字体和大小
        operationLabel.setForeground(new Color(131, 127, 131));
        gbc.gridx = 0;
        gbc.gridy = 4;
        encryptDecryptPanel.add(operationLabel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); // 设置FlowLayout间距为0
        JButton encryptButton = new JButton("加密");
        encryptButton.setFont(new Font("楷体", Font.PLAIN, 20)); // 设置字体和大小
        JButton decryptButton = new JButton("解密");
        decryptButton.setFont(new Font("楷体", Font.PLAIN, 20)); // 设置字体和大小
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        gbc.gridx = 1;
        gbc.gridy = 4;
        encryptDecryptPanel.add(buttonPanel, gbc);

        JLabel resultLabel = new JLabel("加密或解密的结果:");
        resultLabel.setFont(new Font("楷体", Font.PLAIN, 25)); // 设置字体和大小
        resultLabel.setForeground(new Color(131, 127, 131));
        gbc.gridx = 0;
        gbc.gridy = 5;
        encryptDecryptPanel.add(resultLabel, gbc);

        JTextField resultField = new JTextField(20); // 设置文本框的列数
        resultField.setEditable(false);  // 设置文本框为不可编辑
        gbc.gridx = 1;
        gbc.gridy = 5;
        encryptDecryptPanel.add(resultField, gbc);

        // 破解界面
        JPanel bruteForcePanel = new JPanel(new GridBagLayout());

        JLabel bruteForceTitleLabel = new JLabel("S-DES 暴力破解系统", SwingConstants.CENTER);
        bruteForceTitleLabel.setFont(new Font("楷体", Font.BOLD, 30));
        bruteForceTitleLabel.setForeground(new Color(6, 194, 194, 255));
        bruteForceTitleLabel.setBorder(BorderFactory.createEmptyBorder(0,0,30,0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 8;
        bruteForcePanel.add(bruteForceTitleLabel, gbc);

        JLabel knownPlaintextLabel = new JLabel("请输入已知明文:");
        knownPlaintextLabel.setFont(new Font("楷体", Font.PLAIN, 25));
        knownPlaintextLabel.setForeground(new Color(131, 127, 131));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        bruteForcePanel.add(knownPlaintextLabel, gbc);

        JTextField knownPlaintextField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        bruteForcePanel.add(knownPlaintextField, gbc);

        JLabel knownCiphertextLabel = new JLabel("请输入已知密文:");
        knownCiphertextLabel.setFont(new Font("楷体", Font.PLAIN, 25));
        knownCiphertextLabel.setForeground(new Color(131, 127, 131));
        gbc.gridx = 0;
        gbc.gridy = 2;
        bruteForcePanel.add(knownCiphertextLabel, gbc);

        JTextField knownCiphertextField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        bruteForcePanel.add(knownCiphertextField, gbc);

        JButton bruteForceButton = new JButton("开始破解");
        bruteForceButton.setFont(new Font("楷体", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 3;
        bruteForcePanel.add(bruteForceButton, gbc);

        JLabel timeLabel = new JLabel("破解时间:");
        timeLabel.setFont(new Font("楷体", Font.PLAIN, 25));
        timeLabel.setForeground(new Color(131, 127, 131));
        gbc.gridx = 0;
        gbc.gridy = 4;
        bruteForcePanel.add(timeLabel, gbc);

        JProgressBar progressBar = new JProgressBar();
        gbc.gridx = 1;
        gbc.gridy = 4;
        bruteForcePanel.add(progressBar, gbc);

        JLabel force_keyLabel = new JLabel("暴力破解的密钥:");
        force_keyLabel.setFont(new Font("楷体", Font.PLAIN, 25));
        force_keyLabel.setForeground(new Color(131, 127, 131));
        gbc.gridx = 0;
        gbc.gridy = 5;
        bruteForcePanel.add(force_keyLabel, gbc);

        JTextArea keyTextArea = new JTextArea(5, 20); // 设置文本区域的行数和列数
        keyTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(keyTextArea); // 添加滚动条
        gbc.gridx = 1;
        gbc.gridy = 5;
        //gbc.gridwidth = ;
        bruteForcePanel.add(scrollPane, gbc);
        // 添加两个面板到卡片面板
        cardPanel.add(encryptDecryptPanel, "encryptDecrypt");
        cardPanel.add(bruteForcePanel, "bruteForce");

        // 添加卡片面板到框架
        frame.add(cardPanel, BorderLayout.CENTER);

        // 添加模式切换按钮
        JPanel modePanel = new JPanel();
        modePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton encryptDecryptModeButton = new JButton("加解密模式");
        encryptDecryptModeButton.setFont(new Font("楷体", Font.PLAIN, 20));
        JButton bruteForceModeButton = new JButton("破解模式");
        bruteForceModeButton.setFont(new Font("楷体", Font.PLAIN, 20));
        modePanel.add(encryptDecryptModeButton);
        modePanel.add(bruteForceModeButton);
        modePanel.setBorder(BorderFactory.createEmptyBorder(40,0,0,0));
        frame.add(modePanel, BorderLayout.NORTH);

        // 添加加密按钮的事件监听器
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputField.getText();
                String key = keyField.getText();
                String inputType = (String) inputTypeComboBox.getSelectedItem();
                String result = "";
                    if (inputType.equals("ASCII")) {
                        result = S_Des.s_des_encrypt_ascii(input, key);
                    }
                    else if(inputType.equals("unicode字符")) {
                        result = S_Des.s_des_encrypt_unicode(input, key);
                    }
                    else {
                        if (input.length() == 8 && key.length() == 10) {
                            result = S_Des.s_des_encrypt(input, key);
                        } else {
                            JOptionPane.showMessageDialog(frame, "请输入8位明文和10位密钥", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    resultField.setText(result);
                }
        });

        // 添加解密按钮的事件监听器
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputField.getText();
                String key = keyField.getText();
                String inputType = (String) inputTypeComboBox.getSelectedItem();
                String result = "";
                if (inputType.equals("ASCII")) {
                    result = S_Des.s_des_decrypt_ascii(input, key);
                }
                else if(inputType.equals("unicode字符")) {
                    result = S_Des.s_des_decrypt_unicode(input, key);
                }
                else {
                    if (input.length() == 8 && key.length() == 10) {
                        result = S_Des.s_des_decrypt(input, key);
                    } else {
                        JOptionPane.showMessageDialog(frame, "请输入8位明文和10位密钥", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
                resultField.setText(result);
            }
        });

// 添加暴力破解按钮的事件监听器
        bruteForceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String knownPlaintext = knownPlaintextField.getText();
                String knownCiphertext = knownCiphertextField.getText();
                keyTextArea.setText(""); // 清空文本区域
                long startTime = System.nanoTime(); // 记录开始时间

                // 调用暴力破解函数
                List<String> keys = S_Des.forcefullyCrack(knownPlaintext, knownCiphertext);

                long endTime = System.nanoTime(); // 记录结束时间
                long duration = endTime - startTime; // 计算持续时间
                progressBar.setValue(100); // 设置进度条为100%

                // 使用 setText 方法更新文本区域
                StringBuilder keysText = new StringBuilder();
                for (String key : keys) {
                    keysText.append(key).append("\n");
                }
                keyTextArea.setText(keysText.toString());
                JOptionPane.showMessageDialog(frame, "破解完成，耗时: " + duration + " 纳秒", "破解结果", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        // 添加模式切换按钮的事件监听器
        encryptDecryptModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (cardPanel.getLayout());
                cl.show(cardPanel, "encryptDecrypt");
            }
        });

        bruteForceModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (cardPanel.getLayout());
                cl.show(cardPanel, "bruteForce");
            }
        });

        // 显示框架
        frame.setVisible(true);
    }
}
