package com.ohos.trebleshot.utils;

import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;

import java.io.IOException;
import java.io.InputStream;

public class ImageLUtils {

    public void loadMethOne(Image image , String path) throws IOException {

//        String filePath = String.format("/storage/emulated/0/DCIM/Camera/IMG_20210422_165248.jpg", "page=1.json");

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
//        final ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
//        int ch;
//        while ((ch = inputStream.read()) != -1) {
//            swapStream.write(ch);
//        }
//                图片流的加载
        ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
        System.out.println("加载的图片大小>>>>>>>>>"+inputStream.available());
        sourceOptions.formatHint = "image/jpg";
        ImageSource imageSource = ImageSource.create(inputStream, sourceOptions);
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
//        decodingOptions.desiredSize = new Size(0, 0);
//        decodingOptions.desiredRegion = new Rect(0, 0, 0, 0);
        decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;
        PixelMap pixelMap = imageSource.createPixelmap(decodingOptions);
        System.out.println("加载的图片大小>>>>>>>>>"+pixelMap.getBytesNumberPerRow());
        image.setPixelMap(pixelMap);

    }
    public void methTwo(Context context,String fileName,Image image) throws IOException {
        ResourceManager resourceManager = context.getApplicationContext().getResourceManager();
        RawFileEntry rawFileEntry = resourceManager.getRawFileEntry(fileName);
        Resource resource = rawFileEntry.openRawFile();
        int lenth = resource.available();
        byte[] buffer = new byte[lenth];
        resource.read(buffer, 0, lenth);
//        ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
//    sourceOptions.formatHint = "image/jpg";
        ImageSource imageSource = ImageSource.create(buffer, null/*sourceOptions*/);
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        decodingOptions.desiredSize = new Size(200, 200);
//    decodingOptions.desiredRegion = new Rect(0, 0, 0, 0);
//    decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;
        PixelMap pixelMap = imageSource.createPixelmap(decodingOptions);

        image.setPixelMap(pixelMap);
    }
}
