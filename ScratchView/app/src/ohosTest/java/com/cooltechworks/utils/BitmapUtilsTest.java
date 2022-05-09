 /*
  * Copyright (c) 2021 Huawei Device Co., Ltd.
  * <p>
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  * <p>
  * http://www.apache.org/licenses/LICENSE-2.0
  * <p>
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package com.cooltechworks.utils;

import com.cooltechworks.scratchview.ResourceTable;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class BitmapUtilsTest {

    private PixelMap pixelMap;

    @Before
    public void setUp() throws Exception {
        Context appContext = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();
        try {
            Resource resource = appContext.getResourceManager().getResource(ResourceTable.Media_ic_scratch_pattern);
            ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
            srcOpts.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(resource, srcOpts);
            ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
            pixelMap = imageSource.createPixelmap(decodingOptions);
        } catch (IOException | NotExistException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        pixelMap = null;
    }

    @Test
    public void compareEquivalanceTest() {
        if (pixelMap != null) {
            float result = BitmapUtils.compareEquivalance(pixelMap, pixelMap);
            Assert.assertEquals(1.0f, result, 0.000001f);
        }
    }

    @Test
    public void getTransparentPixelPercentTest() {
        if (pixelMap != null) {
            float percent = BitmapUtils.getTransparentPixelPercent(pixelMap);
            Assert.assertEquals(0.5f, percent, 0.000001f);
        }
    }
}
