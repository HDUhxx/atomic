package com.jltf.jltf_idiom.utils;

import ohos.agp.components.Component;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.miscservices.pasteboard.IPasteDataChangedListener;
import ohos.miscservices.pasteboard.PasteData;
import ohos.miscservices.pasteboard.SystemPasteboard;


public class PasteboardUtils {

    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP,0x0,"蛟龙腾飞--粘贴版");

    private SystemPasteboard pasteboard;

    public PasteboardUtils(Context context) {
        initPasteboard(context);
    }
    // 复制到粘贴板
    public void copyText(String text) {
        pasteboard.setPasteData(PasteData.creatPlainTextData(text));
    }

    // 粘贴
    public String pasteText() {
        String result = "";
        PasteData pasteData = pasteboard.getPasteData();
        if (pasteData == null) {
            return result;
        }
        PasteData.DataProperty dataProperty = pasteData.getProperty();
        boolean hasHtml = dataProperty.hasMimeType(PasteData.MIMETYPE_TEXT_HTML);
        boolean hasText = dataProperty.hasMimeType(PasteData.MIMETYPE_TEXT_PLAIN);
        if (hasHtml || hasText) {
            for (int i = 0; i < pasteData.getRecordCount(); i++) {
                PasteData.Record record = pasteData.getRecordAt(i);
                String mimeType = record.getMimeType();
                if (mimeType.equals(PasteData.MIMETYPE_TEXT_HTML)) {
                    result = record.getHtmlText();
                } else if (mimeType.equals(PasteData.MIMETYPE_TEXT_PLAIN)) {
                    result = record.getPlainText().toString();
                } else {
                    HiLog.info(TAG, "%{public}s", "getPasteData mimeType :" + mimeType);
                }
            }
        }
        return result;
    }

    //   监听粘贴版内容
    private final IPasteDataChangedListener listener = new IPasteDataChangedListener() {

        @Override
        public void onChanged() {
            PasteData pasteData = pasteboard.getPasteData();
            if (pasteData != null) {
                HiLog.info(TAG,"粘贴版内容改变："+pasteData);
            }
        }
    };

    //  清空粘贴板
    public void clearPasteboard(Component component) {
        if (pasteboard != null) {
            pasteboard.clear();
        }
    }

    //初始化
    private void initPasteboard(Context context) {
        pasteboard = SystemPasteboard.getSystemPasteboard(context);
        pasteboard.addPasteDataChangedListener(listener);
    }
}
