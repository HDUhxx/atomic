# PhotoView
PhotoView aims to help produce an easily usable implementation of a zooming openharmony Image Component.

## Dependency


add the library to your module `build.gradle`
```gradle
dependencies {
    implementation 'io.openharmony.tpc.thirdlib:PhotoView:1.1.1'
}
```

## Features
- Out of the box zooming, using multi-touch and double-tap.
- Scrolling, with smooth scrolling fling.
- Works perfectly when used in a scrolling parent .
- Allows the application to be notified when the displayed Matrix has changed. Useful for when you need to update your UI based on the current zoom/scroll position.
- Allows the application to be notified when the user taps on the Photo.

## Usage
There is a [sample](https://gitee.com/openharmony-tpc/PhotoView/tree/master/entry) provided which shows how to use the library in a more advanced way, but for completeness, here is all that is required to get PhotoView working:
```xml
<?xml version="1.0" encoding="utf-8"?>
<DirectionalLayout
    xmlns:ohos="http://schemas.huawei.com/res/ohos"
    xmlns:photo="http://schemas.huawei.com/res/photo"
    ohos:height="match_parent"
    ohos:width="match_parent"
    ohos:id="$+id:container"
    ohos:orientation="vertical">

<com.github.chrisbanes.photoview.PhotoView
        ohos:id="$+id:photo_v"
        ohos:height="match_parent"
        ohos:width="match_parent"
        photo:image="$media:wallpaper"
        />

</DirectionalLayout>
```
or
```java
PhotoView photoView = (PhotoView) findViewById(ResourceTable.Id_photo_v);
photoView.setPixelMap(ResourceTable.Media_wallpaper);
or


photoView.setPixelMap(pixelMap);
photoView.setImageElement(element);
photoView.setImageAndDecodeBounds(ResourceTable.Media_wallpaper);
```

Add temporary solution for PageSlider
``` java
public class PageProvider extends PageSliderProvider {
      ...
       @Override
          public Object createPageInContainer(ComponentContainer componentContainer, int i) {
              final int data = list.get(i);
              PhotoView view = new PhotoView(context);
              ...
              view.setPageSlider(slider); //add
              componentContainer.addComponent(view);
              return view;
          }
}
```

## Subsampling Support
This library aims to keep the zooming implementation simple. If you are looking for an implementation that supports subsampling, check out [this project](https://gitee.com/openharmony-tpc/subsampling-scale-image-view)

License
--------

    Copyright 2018 Chris Banes

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
