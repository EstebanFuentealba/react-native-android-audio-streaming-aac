# React Native Android Audio Streaming AAC

A react native wrapper for [aacdecoder-android](https://code.google.com/p/aacdecoder-android/).


## Setup

* `android/settings.gradle`

```gradle
...
include ':react-native-android-audio-streaming-aac'
project(':react-native-android-audio-streaming-aac').projectDir = new File(settingsDir, '../node_modules/react-native-android-audio-streaming-aac')
```

* `android/app/build.gradle`

```gradle
...
dependencies {
    ...
    compile project(':react-native-android-audio-streaming-aac')
}
```

* register module (in MainActivity.java)

```java
import cl.json.react.AACStreamingPackage;  // <--- import

public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {

  ......

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mReactRootView = new ReactRootView(this);

    mReactInstanceManager = ReactInstanceManager.builder()
      .setApplication(getApplication())
      .setBundleAssetName("index.android.bundle")
      .setJSMainModuleName("index.android")
      .addPackage(new MainReactPackage())
      .addPackage(new AACStreamingPackage(MainActivity.class))      // <------- add package
      .setUseDeveloperSupport(BuildConfig.DEBUG)
      .setInitialLifecycleState(LifecycleState.RESUMED)
      .build();

    mReactRootView.startReactApplication(mReactInstanceManager, "ExampleRN", null);

    setContentView(mReactRootView);
  }

  ......

}
```

## Usage

```js
var AACStreamingAndroid = require('react-native-android-audio-streaming-aac');

AACStreamingAndroid.setURLStreaming('http://tunein.digitalproserver.com/bioconcebb.aac');
AACStreamingAndroid.play();
```
