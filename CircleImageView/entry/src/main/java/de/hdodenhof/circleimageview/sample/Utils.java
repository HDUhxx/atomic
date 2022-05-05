/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.hdodenhof.circleimageview.sample;

import ohos.agp.components.element.PixelMapElement;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Utils class
 */
public class Utils {
    private static final int HILOG_TYPE = 3;

    private static final int HILOG_DOMAIN = 0xD000F00;

    private static final HiLogLabel LABEL = new HiLogLabel(HILOG_TYPE, HILOG_DOMAIN, "[Utils]");

    private Utils() {
    }

    /**
     * * Prepare pixelmapelement from resource
     *
     * @param resource resource
     * @return PixelMapElement
     * @throws IOException IOException
     * @throws NotExistException NotExistException
     */
    public static Optional<PixelMapElement> prepareElement(Resource resource) throws IOException, NotExistException {
        Optional<PixelMap> pixelMap = preparePixelmap(resource);
        if (pixelMap.isPresent()) {
            PixelMapElement pixelMapElement = new PixelMapElement(pixelMap.get());
            return Optional.of(pixelMapElement);
        }
        return Optional.empty();
    }

    /**
     * * Create pixelmap from resource
     *
     * @param resource resource
     * @return PixelMap
     * @throws IOException IOException
     */
    public static Optional<PixelMap> preparePixelmap(Resource resource) throws IOException {
        byte[] bytes = null;
        try {
            if (resource != null) {
                bytes = readBytes(resource);
                resource.close();
            }
        } catch (IOException e) {
            HiLog.error(LABEL, "get pixelmap failed, read resource bytes failed");
            return Optional.empty();
        }
        if (bytes == null) {
            HiLog.error(LABEL, "get pixelmap failed, read resource bytes is null");
            return Optional.empty();
        }
        ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
        ImageSource imageSource = ImageSource.create(bytes, srcOpts);
        if (imageSource == null) {
            HiLog.error(LABEL, "get pixelmap failed, image source is null");
        }
        ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
        decodingOpts.desiredSize = new Size(0, 0);
        decodingOpts.desiredRegion = new Rect(0, 0, 0, 0);
        decodingOpts.desiredPixelFormat = PixelFormat.ARGB_8888;
        PixelMap decodePixelMap = null;
        if (imageSource != null) {
            decodePixelMap = imageSource.createPixelmap(decodingOpts);
        }
        return Optional.ofNullable(decodePixelMap);
    }

    private static byte[] readBytes(Resource resource) {
        final int bufferSize = 1024;
        final int ioEnd = -1;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] bytes = new byte[bufferSize];
        byte[] bytesArray = new byte[0];
        while (true) {
            try {
                int readLen = resource.read(bytes, 0, bufferSize);
                if (readLen == ioEnd) {
                    bytesArray = output.toByteArray();
                    break;
                }
                output.write(bytes, 0, readLen);
            } catch (IOException e) {
                break;
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                    HiLog.error(LABEL, "close output failed");
                }
            }
        }
        return bytesArray;
    }
}
