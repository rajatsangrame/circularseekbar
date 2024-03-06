# circularseekbar

[![](https://jitpack.io/v/rajatsangrame/circularseekbar.svg)](https://jitpack.io/#rajatsangrame/circularseekbar)

Similar to the android Seekbar, Circular Seekbar is created to seek the progress in circular shape.
This library contains two seekbars. One is simple `CircularSeekbar` and other is `RainbowSeekbar`.
Both provides feature like gradients, colors, animated progress etc but the major difference is,
`RainbowSeekbar` currently doesn't support rotation and focused on sweep angle in fixed position
(start position is bottom) and `CircularSeekbar` supports rotation.

Below are some of the key features provided by Circular Seekbar

## Features

* Set progress accurately with animation as well as with touch events.
* Apply gradients and colors to seekbar elements.
* Change the sweep angle of seekbar between 0 to 360 degrees.
* Rotation (Moving start angle of seekbar by 0, 90, 180 and 270 degrees).
* Option to change its thickness, thumb-radius, enable/disable touch event, thumb visibility etc.

Clone this repository play with the sample app to know more.

## Installation

```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    implementation 'com.github.rajatsangrame:circularseekbar:1.0.0'
}
```

## Usage

Define `CircularSeekbar` in your `activity_layout.xml`. You can define multiple
properties in xml declaration. Check the below example for the reference.

```xml

<com.rajatsangrame.circularseekbar.CircularSeekbar
    ...   
    app:backgroundColor="#9e9e9e" 
    app:enableTouch="true"
    app:progress="22" 
    app:progressColor="#03a9f4"
    app:showThumb="true"
    app:startAngle="top" 
    app:thickness="20dp" 
    app:thumbColor="#ff5722"
    app:thumbPadding="4dp" 
    app:thumbRadius="16dp" />


<com.rajatsangrame.circularseekbar.RainbowSeekbar
    ...
    app:roundCorners="true"
    app:sweepAngle="270"

```

Defining the `CircularSeekbar` on runtime will work similar like any other views.
Check the below example for the reference.

```kotlin
    val circularSeekbar = CircularSeekbar(this)
    circularSeekbar.setThickness(value)
    circularSeekbar.setThumbRadius(value)
    circularSeekbar.setStartAngle(StartAngle.TOP)
    circularSeekbar.setBackgroundColor(Color.parseColor(color))
    circularSeekbar.setProgressColor(Color.parseColor(color))
    circularSeekbar.setThumbColor(Color.parseColor(color))
    circularSeekbar.setEnableTouch(isChecked)
    circularSeekbar.setShowThumb(isChecked)
    circularSeekbar.setRoundCorners(true)
    
    circularSeekbar.setAnimatedProgress(progress = 75f, duration = 600L)
    
    circularSeekbar.onProgressChanged { progress, _ ->
        findViewById<TextView>(R.id.tvprogress).text = "$progress"
    }
```

We recommend to use equal padding for the `CircularSeekbar` else touch event might now work for now.

### License

```
   Copyright 2024 Rajat Sangrame

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






