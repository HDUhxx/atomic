package com.example.JLTFCalculator.util;

import java.util.*;
public class Calculate {

    static final String symbol = "+-*/()"; //运算符
    static final String[] priority = {"+-", "*/", "()"}; //运算符优先级

    static Comparator<String> comp = new Comparator<String>() { //运算符比较器
        public int compare(String s1, String s2) {
            int n1=0, n2=0;
            for (int i=0; i<priority.length; i++) {
                if (priority[i].indexOf(s1) >= 0) {n1 = i;}
                if (priority[i].indexOf(s2) >= 0) {n2 = i;}
            }
            return (n1 - n2);
        }
    };


    public static List<String> analyze(String exp) throws Exception { //分析
        if (exp == null) {
            throw new Exception ("illegal parameter.");
        }
        exp = exp.replaceAll("\\s*", ""); //去掉所有的空格（为了方便中间存在空格算合法）


        List<String> list = new ArrayList<String>();
        Stack<String> sym = new Stack<String>();

        StringBuilder buf = new StringBuilder();
        for (char c : exp.toCharArray()) {
            if (symbol.indexOf(c) >= 0) { //如果是运算符
                if (buf.length() > 0) { //如果有操作数
                    String v = buf.toString();
                    if (! v.matches("\\d+([.]\\d+)?")) {
                        throw new Exception ("illegal varaible("+v+").");
                    }
                    list.add(v);
                    buf.delete(0, buf.length());
                }

                if (c == '(') {
                    sym.push(String.valueOf(c));
                } else if (c == ')') {
                    String last = "";
                    while (sym.size() > 0) {
                        last = sym.pop();
                        if (last.equals("(")) {
                            break;
                        } else {
                            list.add(last);
                        }
                    }
                    if (!"(".equals(last)) {
                        throw new Exception ("illigal express.");
                    }
                } else if (sym.size() > 0) {
                    String s = String.valueOf(c);
                    String last = sym.peek();
                    if (last.equals("(") || comp.compare(s, last) > 0) {
                        sym.push(s);
                    } else {
                        last = sym.pop();
                        list.add(last);
                        sym.push(s);
                    }
                } else {
                    sym.push(String.valueOf(c));
                }
            } else { //不是运算符则当作操作数（因为已经去除所有空格，这里不再需要判断空格）
                buf.append(c);
            }
        }

        if (buf.length() > 0) {
            list.add(buf.toString());
        }

        while (sym.size() > 0) {
            String last = sym.pop();
            if ("()".indexOf(last) >= 0) {
                throw new Exception ("illigal express.");
            }
            list.add(last);
        }

        return list;
    }

    public static double cacl(List<String> list) throws Exception { //计算
        Stack<Double> val = new Stack<Double>();
        double result = 0;
        while (list.size() > 0) {
            String s = list.remove(0);
            if (symbol.indexOf(s) >= 0) {
                double d1 = val.pop();
                double d2 = val.pop();
                if ("+".equals(s)) {
                    result = d2 + d1;
                } else if ("-".equals(s)) {
                    result = d2 - d1;
                } else if ("*".equals(s)) {
                    result = d2 * d1;
                } else if ("/".equals(s)) {
                    result = d2 / d1;
                } else {
                    throw new Exception ("illigal symbol("+s+").");
                }
                val.push(result);
            } else {
                if (!s.matches("\\d+([.]\\d+)?")) {
                    throw new Exception ("illigal variable("+s+").");
                }
                val.push(Double.valueOf(s));
            }
        }

        return result;
    }

}
