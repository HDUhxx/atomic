package me.panavtec.title.hmutils;

import ohos.agp.components.Image;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;

public class RoundRectView extends Image {
    private static final int RADIUS = 320;//圆角半径，如果设置成图片的宽度的一半，则会图片变成圆形
    private PixelMapHolder pixelMapHolder;//像素图片持有者
    private final RectFloat rectDst;//目标区域
    private RectFloat rectSrc;//源区域
    public RoundRectView(Context context, Size viewSize) {//Size是该视图显示区域的大小
        super(context);
        onDraw();
        rectDst = new RectFloat(0,0,viewSize.width,viewSize.height);
    }

    public void putPixelMap(PixelMap pixelMap){
        if (pixelMap != null) {
            rectSrc = new RectFloat(0, 0, pixelMap.getImageInfo().size.width, pixelMap.getImageInfo().size.height);
            pixelMapHolder = new PixelMapHolder(pixelMap);
            invalidate();//重新检验该组件
        }else{
            pixelMapHolder = null;
            setPixelMap(null);
        }
    }

    private void onDraw(){
        //添加绘制任务
        this.addDrawTask((view, canvas) -> {
            if (pixelMapHolder == null){
                return;
            }
            synchronized (pixelMapHolder) {
                //绘制圆角矩形图片
                canvas.drawPixelMapHolderRoundRectShape(pixelMapHolder, rectSrc, rectDst, RADIUS, RADIUS);
                pixelMapHolder = null;
            }
        });
    }
}
