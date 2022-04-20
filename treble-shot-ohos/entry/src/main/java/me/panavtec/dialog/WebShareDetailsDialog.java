package me.panavtec.dialog;

import com.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import com.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import me.panavtec.title.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.image.PixelMap;

/**
 * created by: veli
 * date: 4/8/19 9:16 AM
 */
public class WebShareDetailsDialog extends CommonDialog
{
    public WebShareDetailsDialog(Context context, Component contentComponent,  String address)
    {
        super(context);

        Image qrImage = (Image) contentComponent.findComponentById(ResourceTable.Id_web_share_image);
        Text addressText = (Text) contentComponent.findComponentById(ResourceTable.Id_web_share_text);
        contentComponent.findComponentById(ResourceTable.Id_web_share_close).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                WebShareDetailsDialog.this.hide();
            }
        });
        addressText.setText(address);

        try {

            new EventHandler(EventRunner.create("QRCodeGen")).postTask(() -> {
                PixelMap pixelMap =  QRCodeEncoder.syncEncodeQRCode(address, BGAQRCodeUtil.dp2px(context, 160));
                context.getUITaskDispatcher().asyncDispatch(() -> {
                    qrImage.setPixelMap(pixelMap);
                });
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
