package com.company;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.String;

class WindowOperation extends JFrame {
    JMenuBar menuBar; //菜单条
    JMenu menu1, menu2, menu3; //菜单
    JPanel panel; //按钮面板
    JTextArea textArea; //文本区
    JButton[][] button = new JButton[6][5]; //30个按钮
    String Mem = "0"; //存储区的值

    WindowOperation() {
        init();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        validate();
    }

    void init() {
        setBounds(300, 50, 640, 700);    //窗口大小
        setTitle("calculator");    //窗口名称
        setLayout(new BorderLayout()); //布局样式

        LineBorder border = new LineBorder(Color.LIGHT_GRAY, 6, true);//文本区域边框设置
        textArea = new JTextArea(2, 10);     //计算区域
        textArea.setFont(new Font("Calibre", Font.PLAIN, 60)); //设置字体、样式、大小
        textArea.setBorder(border);

        String [][] str = {
                {"Mc", "Mr", "Ms", "M+", "M-"},
                {"<—", "CE", "C", "±", "√"},
                {"7", "8", "9", "/", "%"},
                {"4", "5", "6", "*", "1/x"},
                {"1", "2", "3", "-", "^2"},
                {"00", "0", ".", "+", "="}
        };

        panel = new JPanel();
        panel.setLayout(new GridLayout(6, 5));
        for (int i = 0; i < 6; i++) { //插入各个功能、数字按钮
            for (int j = 0; j < 5; j++) {
                button[i][j] = new JButton(str[i][j]);
                button[i][j].setFont(new Font("黑体", Font.BOLD, 30)); //设置按钮字体、样式、大小
                panel.add(button[i][j]);
            }
        }
        add(textArea, BorderLayout.NORTH);
        add(panel);

        menu1 = new JMenu(" 查看(C) ");
        menu1.setFont(new Font("楷体", Font.PLAIN, 20)); //设置字体、样式、大小
        menu2 = new JMenu(" 编辑(E) ");
        menu2.setFont(new Font("楷体",Font.PLAIN, 20));
        menu3 = new JMenu(" 帮助(H) ");
        menu3.setFont(new Font("楷体", Font.PLAIN, 20));
        menuBar = new JMenuBar();
        menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu3);
        setJMenuBar(menuBar);

        Event();
    }

    public void Event() { //对各种事件注册监视器
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                //除了”=“和”MC“等功能键，全部设置自己的监视器
                if(i != 5 || j != 4)
                    button[i][j].addActionListener(new TextListener(button[i][j], textArea));
            }
        }
        //清楚存储区数值
        button[0][0].addActionListener((e -> Mem = "0"));
        //显示存储区数值字符串
        button[0][1].addActionListener(e -> textArea.setText(Mem));
        //存储结果
        button[0][2].addActionListener(e -> {
            Mem = textArea.getText();
            Mem = Mem.substring(Mem.indexOf('\n')+1); //去掉第一行字符串，只留下结果
        });
        //当前文本区的值与存储区的值相加
        button[0][3].addActionListener(e -> {
            double x1, x2;
            x1 = Double.parseDouble(Mem);
            x2 = Double.parseDouble(textArea.getText());
            Mem = "" + (x1 + x2);
        });
        button[0][4].addActionListener(e -> {
            double x1, x2;
            x1 = Double.parseDouble(Mem);
            x2 = Double.parseDouble(textArea.getText());
            Mem = "" + (x2 - x1);
        });
        button[1][0].addActionListener(new BackListener(textArea));
        button[1][1].addActionListener(new BackListener(textArea));
        button[1][2].addActionListener(new ClearListener(textArea));
        button[1][3].addActionListener(new TextListener(button[1][3], textArea));
        button[1][4].addActionListener(new TextListener(button[1][4], textArea));
        button[5][4].addActionListener(new ResultListener(textArea));
    }
}

class ResultListener implements ActionListener {
    JTextArea textArea;
    String str; //用来保存文本区域的字符串；
    public ResultListener(JTextArea textArea1) { //将文本区传入
        textArea = textArea1;
    }
    public void actionPerformed(ActionEvent e) {
        textArea.setText(textArea.getText() + "="); //获取文本内容
        str = textArea.getText();

        if (str.contains("+")) { //如果字符串中包含“+”，做加法运算
            String str1 = str.substring(0, str.indexOf('+')); //获取加数
            String str2 = str.substring(str.indexOf('+') + 1, str.indexOf('=')); //获取被加数
            double x1 = Double.parseDouble(str1); //转化为double类型
            double x2 = Double.parseDouble(str2);
            textArea.setText(str + "\n" + (x1 + x2));
        }
        else if (str.contains("*")) { //乘法运算
            String str1 = str.substring(0, str.indexOf('*'));
            String str2 = str.substring(str.indexOf('*') + 1, str.indexOf('='));
            double x1 = Double.parseDouble(str1);
            double x2 = Double.parseDouble(str2);
            textArea.setText(str + "\n" + x1 * x2);
        }
        else if(str.contains("1/x")) { //倒数运算
            String str1 = str.substring(0, str.indexOf("1/x"));
            double x1 = Double.parseDouble(str1);
            if(x1 == 0) textArea.setText(str + "\n" + "不能求倒数");
            else textArea.setText(str + "\n" + 1/x1);
        }
        else if (str.contains("/")) { //除法运算
            String str1 = str.substring(0, str.indexOf('/'));
            String str2 = str.substring(str.indexOf('/') + 1, str.indexOf('='));
            double x1 = Double.parseDouble(str1);
            double x2 = Double.parseDouble(str2);
            if (x2 == 0) textArea.setText(str + "\n" + "不能除以0");
            else textArea.setText(str + "\n" + x1 / x2);
        }
        else if (str.contains("^2")) { //平方运算
            String str1 = str.substring(0, str.indexOf('^'));
            double x = Double.parseDouble(str1);
            textArea.setText(str + "\n" + x * x);
        }
        else if (str.contains("√")) { //开方运算
            String str1 = str.substring(str.indexOf('√') + 1, str.indexOf('='));
            double x = Double.parseDouble(str1);
            textArea.setText(str + "\n" + Math.sqrt(x));
        }
        else if (str.contains("±")) { //加减运算
            String str1 = str.substring(0, str.indexOf('±'));
            String str2 = str.substring(str.indexOf('±') + 1, str.indexOf('='));
            double x1 = Double.parseDouble(str1);
            double x2 = Double.parseDouble(str2);
            if (x2 == 0) textArea.setText(str + "\n" + x1);
            else textArea.setText(str + "\n" + (x1 + x2) + " & " + (x1 - x2));
        }
        else if (str.contains("%")) { //百分号运算
            String str1 = str.substring(0, str.indexOf('%'));
            double x1 = Double.parseDouble(str1);
            textArea.setText(str + "\n" + x1/100);

        }
        else if (str.contains("-")) { //减法运算
            if(str.charAt(0) == '-') { //如果第一个字符是“-”，形如-2-3形式，就转化成为-(2+3)形式
                String str_x = str;
                str = str.substring(1);
                String str1 = str.substring(0, str.indexOf('-'));
                String str2 = str.substring(str.indexOf('-') + 1, str.indexOf('='));
                double x1 = Double.parseDouble(str1);
                double x2 = Double.parseDouble(str2);
                textArea.setText(str_x + "\n" + (-x1-x2));
            }
            else {
                String str1 = str.substring(0, str.indexOf('-'));
                String str2 = str.substring(str.indexOf('-') + 1, str.indexOf('='));
                double x1 = Double.parseDouble(str1);
                double x2 = Double.parseDouble(str2);
                textArea.setText(str + "\n" + (x1-x2));
            }
        }

    }
}

class ClearListener implements ActionListener {
    JTextArea textArea;
    public ClearListener(JTextArea textArea1) { //将按钮和文本区传入
        textArea = textArea1;
    }
    public void actionPerformed(ActionEvent e) { //设置文本区
        textArea.setText("");
    }
}

class BackListener implements ActionListener {
    JTextArea textArea;
    String str;
    public BackListener(JTextArea textArea1) { //将文本区传入
        textArea = textArea1;
    }
    public void actionPerformed(ActionEvent e) { //设置文本区
        str = textArea.getText();
        if(str.length() == 0) textArea.setText(""); //如果文本区内容为空，输出为空
        else { //否则让当前文本区删掉最后一个符号/数字
            str = str.substring(0, str.length()-1);
            textArea.setText(str);
        }
    }
}

class TextListener implements ActionListener {
    JButton button;
    JTextArea textArea;
    public TextListener(JButton button1, JTextArea textArea1) { //将按钮和文本区传入
        textArea = textArea1;
        button = button1;
    }
    public void actionPerformed(ActionEvent e) { //设置文本区
        textArea.setText(textArea.getText() + button.getText());
    }
}

public class Main {
    public static void main(String[] args) {
        new WindowOperation();
    }
}
