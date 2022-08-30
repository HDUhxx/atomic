/*
 *    Copyright 2021 youth5201314
 *    Copyright 2021 Institute of Software Chinese Academy of Sciences, ISRC

 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.youth.banner.loader;



import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public abstract class ImageLoader implements ImageLoaderInterface<Image> {

    public ImageLoader() {
    }

    public Image createImageView(Context context) {
        Image imageView = new Image(context);
        return imageView;
    }

//    @Override
//    public Image createImageView(Context context, Object path) {    //TODO:原 Banner 库只创建了一个 ImageView，不明白这个库的 createImageView 方法里为什么会构建网络请求，网络图片还是硬编码
//        Image imageView = new Image(context);
//        String   urlImage = "https://www.harmonyos.com/resource/image/community/20201009-164134eSpace.jpg";
//        HttpURLConnection connection = null;
//        try {
//            URL url = new URL(urlImage);
//            URLConnection urlConnection =   url.openConnection();
//            if (urlConnection instanceof   HttpURLConnection) {
//                connection =   (HttpURLConnection) urlConnection;
//            }
//            if (connection != null) {
//                connection.connect();
//                // 之后可进行url的其他操作
//                // 得到服务器返回过来的流对象
//                InputStream inputStream =   urlConnection.getInputStream();
//                ImageSource imageSource = ImageSource.create(inputStream,   new ImageSource.SourceOptions());
//                ImageSource.DecodingOptions   decodingOptions = new ImageSource.DecodingOptions();
//                decodingOptions.desiredPixelFormat   = PixelFormat.ARGB_8888;
//                // 普通解码叠加旋转、缩放、裁剪
//                PixelMap pixelMap = imageSource.createPixelmap(decodingOptions);
//                // 普通解码
//
//                DirectionalLayout.LayoutConfig   config = new DirectionalLayout.LayoutConfig(DirectionalLayout.LayoutConfig.MATCH_CONTENT,   DirectionalLayout.LayoutConfig.MATCH_CONTENT);
//                config.setMargins(10, 10,   10, 10);
//                imageView.setLayoutConfig(config);
//                imageView.setPixelMap(pixelMap);
//                pixelMap.release();
//
//
//                }
//
//
//            } catch (MalformedURLException ex) {
//            ex.printStackTrace();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return imageView;
//    }

}
