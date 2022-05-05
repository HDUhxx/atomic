# CircleImageView

CircleImageView: A fast circular ImageView perfect for profile images.It uses a PixelMapShader in creating a custom image view.

# CircleImageView includes :
* Creating a circular image(using PixelMap)
* Creating a circular image(using resource Id)
* Creating a circular image(using URI)
* Creating a circular image(using PixelMapElement)
* Creating a circular image with a specified border width value
* Creating a circular image with border set to a color specified
* Creating a circular image with given padding values
* Creating a circular image by adjusting brightness/contrast of images(by manipulating RGBA channels)
* Creating a circular image with a given transparency value(alpha)
* Creating an image by disabling circular transformation


# Usage Instructions
1.	A sample project which provides runnable code examples that demonstrate uses of the classes in this project is available in the entry/ folder.
2.	This can be used by instantiating the layout elements programmatically as shown below :

```
CircleImageView civ = new CircleImageView(this);
civ.setPixelMap(ResourceTable.id);  // pass in the resource id of the image to load
```
This can also be done via xml as shown below :

```
   <de.hdodenhof.circleimageview.CircleImageView
            ohos:id="$+id:one_cv"
            ohos:width="match_content"
            ohos:height="match_content"
            ohos:orientation="vertical"
            ohos:image_src="$media:Lighthouse"
            ohos:civ_color="#4CAF50"
            ohos:civ_border_width="3vp"
            />
```

# Installation Instructions

1.For using CircleImageView module in sample app, include the source code and add the below dependencies in entry/build.gradle to generate hap/circleimageview.har.
```
	dependencies {
		implementation project(':circleimageview')
        implementation fileTree(dir: 'libs', include: ['*.har'])
        testCompile 'junit:junit:4.12'
	}
```

2. 2.For using CircleImageView in separate application using har file, add the har file in the entry/libs folder and add the dependencies in entry/build.gradle file.
```
	dependencies {
		implementation fileTree(dir: 'libs', include: ['*.har'])
		testCompile 'junit:junit:4.12'
	}
   ```

3. For using CircleImageView from a remote repository in separate application, add the below dependencies in entry/build.gradle file.
```
    	dependencies {
    	    implementation fileTree(dir: 'libs', include: ['*.har'])
    	    implementation 'io.openharmony.tpc.thirdlib:circleimageview:1.0.0'
    	    testCompile 'junit:junit:4.12'
    	}
```

# License
```
Copyright 2014 - 2020 Henning Dodenhof

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```