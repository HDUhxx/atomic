package com.example.JLTFCalculator.slice;

import com.example.JLTFCalculator.ResourceTable;
import com.example.JLTFCalculator.util.Calculate;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;

import java.util.ArrayList;
import java.util.List;


public class MainAbilitySlice extends AbilitySlice  {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        // 获取文本
        Text text = (Text) findComponentById(ResourceTable.Id_text);
        // 获取数字按钮
        Button one = (Button) findComponentById(ResourceTable.Id_one);
        Button two = (Button) findComponentById(ResourceTable.Id_two);
        Button three = (Button) findComponentById(ResourceTable.Id_three);
        Button four = (Button) findComponentById(ResourceTable.Id_four);
        Button five = (Button) findComponentById(ResourceTable.Id_five);
        Button six = (Button) findComponentById(ResourceTable.Id_six);
        Button seven = (Button) findComponentById(ResourceTable.Id_seven);
        Button eight = (Button) findComponentById(ResourceTable.Id_eight);
        Button nine = (Button) findComponentById(ResourceTable.Id_nine);
        Button zero = (Button) findComponentById(ResourceTable.Id_zero);
        // 获取运算符按钮
        Button add = (Button) findComponentById(ResourceTable.Id_add);
        Button subtract = (Button) findComponentById(ResourceTable.Id_subtract);
        Button multiply = (Button) findComponentById(ResourceTable.Id_multiply);
        Button divide = (Button) findComponentById(ResourceTable.Id_divide);
        // 获取退格键
        Button backspace = (Button) findComponentById(ResourceTable.Id_backspace);
        // 获取按钮
        Button button = (Button) findComponentById(ResourceTable.Id_button);
        // 赋值数字数组
        Button[] numButtons = new Button[]{zero, one, two, three, four, five, six, seven, eight, nine};
        // 赋值操作符数组
        Button[] operatorButtons = new Button[]{add, subtract, multiply, divide};

        List<String> operators = new ArrayList<>();
        operators.add("+");
        operators.add("*");
        operators.add("/");
        operators.add("-");

        // 循环数字数组
        for (int i = 0; i < numButtons.length; i++) {
            int finalI = i;
            numButtons[i].setClickedListener((component) -> text.setText(text.getText() + numButtons[finalI].getText()));
        }
        // 循环操作符数组
        for (int i = 0; i < operatorButtons.length; i++) {
            int finalI = i;
            operatorButtons[i].setClickedListener((component) -> {
                if(text.getText().equals("")) {
                    new ToastDialog(getContext()).setAutoClosable(true).setSize(800,100).setDuration(2000).setText("请先输入数字").show();
                    return;
                } else if(operators.contains(text.getText().substring(text.length()-1))) {
                    new ToastDialog(getContext()).setAutoClosable(true).setSize(800,100).setDuration(2000).setText("请不要连续输入运算符").show();
                    return;
                }
                text.setText(text.getText() + operatorButtons[finalI].getText());
            }
        );
        }


        // 设置退格键的点击事件
        backspace.setClickedListener((component) -> {
            if (text.getText()!=null && !text.getText().trim().equals(""))
                {
                    text.setText(text.getText().substring(0,text.length()-1));
                }
        });

        // 计算结果
        button.setClickedListener((component) -> {
            String target = text.getText();
            if (target.equals("")) {
                new ToastDialog(getContext()).setAutoClosable(true).setSize(800,100).setDuration(2000).setText("请输入算式").show();
                return;
            }else if (operators.contains(text.getText().substring(text.length()-1))) {
                new ToastDialog(getContext()).setAutoClosable(true).setSize(800,100).setDuration(2000).setText("请不要以运算符结尾").show();
                return;
            }
            try {
                    if ((Calculate.cacl(Calculate.analyze(target)) + "").equals("Infinity")) {
                        new ToastDialog(getContext()).setAutoClosable(true).setSize(800, 100).setDuration(2000).setText("0不能作为除数").show();
                    } else {
                        text.setText(Calculate.cacl(Calculate.analyze(target))+ "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        });
    }


    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
