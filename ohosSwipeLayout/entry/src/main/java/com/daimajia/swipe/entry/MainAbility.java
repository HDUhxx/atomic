package com.daimajia.swipe.entry;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.entry.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.window.dialog.ToastDialog;

public class MainAbility extends Ability {
    private SwipeLayout swipe1;
    private SwipeLayout swipe2;
    private SwipeLayout swipe3;

    private Text gotoListContainer;
    private Text gotoGridView;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        Component starBottView = findComponentById(ResourceTable.Id_starbott);

        swipe1 = (SwipeLayout) findComponentById(ResourceTable.Id_swipe1);
        swipe1.addDrag(SwipeLayout.DragEdge.Left, ResourceTable.Id_bottom_wrapper);
        swipe1.addDrag(SwipeLayout.DragEdge.Right, ResourceTable.Id_bottom_wrapper2);
        swipe1.addDrag(SwipeLayout.DragEdge.Top, starBottView);
        swipe1.addDrag(SwipeLayout.DragEdge.Bottom, starBottView);
        swipe1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                new ToastDialog(MainAbility.this).setText("click on surface").show();
            }
        });

        swipe1.setLongClickedListener(new Component.LongClickedListener() {
            @Override
            public void onLongClicked(Component component) {
                new ToastDialog(MainAbility.this).setText("longClick on surface").show();
            }
        });

        swipe1.addRevealListener(ResourceTable.Id_starbott, new SwipeLayout.OnRevealListener() {
            @Override
            public void onReveal(Component child, SwipeLayout.DragEdge edge, float fraction, int distance) {
                Component star = child.findComponentById(ResourceTable.Id_star);
                float d = child.getHeight() / 2 - star.getHeight() / 2;
                star.setTranslationY(d * fraction);
                star.setScale(fraction + 0.6f, fraction + 0.6f);
            }
        });

        swipe2 = (SwipeLayout) findComponentById(ResourceTable.Id_swipe2);
        swipe2.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipe2.addDrag(SwipeLayout.DragEdge.Right, findComponentById(ResourceTable.Id_Bottom2));
        findComponentById(ResourceTable.Id_star3).setClickedListener(component -> {
            new ToastDialog(MainAbility.this).setText("star").show();
        });
        findComponentById(ResourceTable.Id_trash).setClickedListener(component -> {
            new ToastDialog(MainAbility.this).setText("trash").show();
        });
        findComponentById(ResourceTable.Id_magnifier).setClickedListener(component -> {
            new ToastDialog(MainAbility.this).setText("magnifier").show();
        });

        swipe3 = (SwipeLayout) findComponentById(ResourceTable.Id_swipe3);
        swipe3.addDrag(SwipeLayout.DragEdge.Top, findComponentById(ResourceTable.Id_Bottom3));
        swipe3.addRevealListener(ResourceTable.Id_bottom_wrapper_child2, new SwipeLayout.OnRevealListener() {
            @Override
            public void onReveal(Component child, SwipeLayout.DragEdge edge, float fraction, int distance) {
                Component star = child.findComponentById(ResourceTable.Id_star4);
                float d = child.getHeight() / 2 - star.getHeight() / 2;
                star.setTranslationY(d * fraction);
                star.setScale(fraction + 0.6f, fraction + 0.6f);


                int c = (Integer) evaluate(fraction, 0xffdddddd, 0xff4C535B);
                ShapeElement shapeElement = new ShapeElement();
                shapeElement.setRgbColor(RgbColor.fromArgbInt(c));
                child.setBackground(shapeElement);
            }
        });

        findComponentById(ResourceTable.Id_bottom_wrapper_child2).setClickedListener(component -> {
            new ToastDialog(MainAbility.this).setText("Yo!").show();
        });

        swipe3.getSurfaceView().setClickedListener(component -> new ToastDialog(MainAbility.this).setText("Click on surface").show());


        gotoListContainer = (Text) findComponentById(ResourceTable.Id_gotolist);
        gotoListContainer.setClickedListener(component -> {
            Intent intent2 = new Intent();
            Operation operation = new Intent.OperationBuilder().withAction("action.swipelayout.listcontainer").build();
            intent2.setOperation(operation);
            startAbility(intent2);
        });

        gotoGridView = (Text) findComponentById(ResourceTable.Id_gotogrid);
        gotoGridView.setClickedListener(component -> {
            Intent intent2 = new Intent();
            Operation operation = new Intent.OperationBuilder().withAction("action.swipelayout.gridview").build();
            intent2.setOperation(operation);
            startAbility(intent2);
        });
    }

    public Object evaluate(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24) |
                (int) ((startR + (int) (fraction * (endR - startR))) << 16) |
                (int) ((startG + (int) (fraction * (endG - startG))) << 8) |
                (int) ((startB + (int) (fraction * (endB - startB))));
    }
}
