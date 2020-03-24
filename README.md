# React Native Async Load Bundle

This is an example project to build the common bundle file and the differential bundle file using Metro, and load the differential bundle asynchronously in app. Compare with loading the official bundle file synchronously, there was **30% ~ 50%**(200 ~ 400 ms) decrease in the load time of react view by using loading the differential bundle asynchronously.

## 📋 Contents

- [Background](#-background)
- [Usage](#-usage)
- [Experimental data](#-experimental-data)
- [How it works](#-how-it-works)
- [Contributing](#-contributing)
- [License](#-license)

## 📋 Background

As we all known that there are three parts in a official bundle file: **Polyfill**、 **Modules**、 **Requires**. If you build two different official bundle files, you will find that there are many repeated content, which is close to 500K. In order to minimize the bundle file, we define a **common bundle file**, which only includes some basic modules(such as `react` and `react-native`). And we define a **differential bundle file**, which only includes your custom code.

Before React Native 0.55, we generally use `google-diff-match-patch` or `BSDiff` to build the differential bundle file, which needs the process of merging before your app loading the differential bundle file.

However, there is a new way to build the differential bundle file by using Metro.

## 📋 Usage

1. Modify the `common.js` like blew:

```javascript
require("react-native");
require("react");
// Add other libs you want to add to the common bundle like this:
// require('other');
```

2. Build the common bundle file with `--config metro.config.common.js` or use the command blew:

```shell
# For android:
npm run build_android_common_bundle
# For iOS:
npm run build_ios_common_bundle
```

3. Build the differential bundle file with `--config metro.config.diff.js` or use the command blew:

```shell
# For android:
npm run build_android_index_diff_bundle
# For iOS:
npm run build_ios_index_diff_bundle
```

4. Copy all output files to the dir of app project or use the command blew:

```shell
npm run copy_files_to_projects
```

5. Run the app project by Android Studio or XCode.

> NOTICE: There are two ways to start an activity with react native in android app: one as sync, the other as async. It is same with the official reference implementation when using sync. As for async, it will start a general activity, which will load a common bundle file, after that it will start a custom activity using react native, which will only load the differential bundle file. The load time of react view will display by log and toast. If you want to get the load time accurately, you should restart the app before clicking one of the bottom two buttons.

## 📋 Experimental data

### 1. Compare the size of output file

| Android File                                        |   Size    | Size After gzip |
| --------------------------------------------------- | :-------: | :-------------: |
| common.android.bundle                               |  637.0 K  |      175K       |
| index.android.bundle (Original)                     |  645.0 K  |      177K       |
| diff.android.bundle (Using Metro)                   |   8.3 K   |    **2.5 K**    |
| diff.android.bundle (Using BSDiff)                  | **3.9 K** |      3.9 K      |
| diff.android.bundle (Using google-diff-match-patch) |  11.0 K   |      3.0 K      |

| iOS File                                        |   Size    | Size After gzip |
| ----------------------------------------------- | :-------: | :-------------: |
| common.ios.bundle                               |  629.0 K  |      173K       |
| index.ios.bundle (Original)                     |  637.0 K  |      176K       |
| diff.ios.bundle (Using Metro)                   |   8.3 K   |    **2.5 K**    |
| diff.ios.bundle (Using BSDiff)                  | **3.9 K** |      3.9 K      |
| diff.ios.bundle (Using google-diff-match-patch) |  11.0 K   |      3.0 K      |

> You can find more information about `google-diff-match-patch` and `BSDiff` by visiting [this](https://github.com/MarcusMa/compare-file-diff-tools).

### 2. Compare the load time of react view.

| Loading Type     |   Redmi 3   | Huawei P20  |  iPhone 6s  | iPhone XS MAX |
| ---------------- | :---------: | :---------: | :---------: | :-----------: |
| Synchronization  |  1628.0 ms  |  738.8 ms   |  961.3 ms   |   472.2 ms    |
| Asynchronization |  1148.2 ms  |  514.8 ms   |  564.2 ms   |   196.3 ms    |
|                  | **-29.50%** | **-30.30%** | **-41.60%** |  **-58.43%**  |

## 📋 How it works

### 1. Build a differential bundle file using Metro.

The key to build a differential bundle file is making the id of input module **invariant** during the process of bundling. It is noteworthy that the Metro provides two configuration items in `metro.config.js` file: `createModuleIdFactory(path)` and `processModuleFilter(module)`.

By customizing `createModuleIdFactory(path)`, we used the **hash** of the file as the key to allocate module id.

```javascript
// See more code int metro.config.base.js
// ...
function getFindKey(path) {
  let md5 = crypto.createHash("md5");
  md5.update(path);
  let findKey = md5.digest("hex");
  return findKey;
}
// ...
```

In order to avoid duplication of allocation of module id, we use a local file (`repo_for_module_id.json`) to store the result of allocation during the process of building.

```json
"8b055b854fd2345d343b6618c9b71f7e":
{
    "id": 5,
    "type": "common"
}
```

By customizing `processModuleFilter(module)`, we compare the hash of input `module` with local-storage. If input `module` is included by common bundle file, it will be filtered and will not be written to the output bundle file.

```javascript
// See more code int metro.config.base.js
// ...
buildProcessModuleFilter = function(buildConfig) {
  return moduleObj => {
    let path = moduleObj.path;
    if (!fs.existsSync(path)) {
      return true;
    }
    if (buildConfig.type == BUILD_TYPE_DIFF) {
      let findKey = getFindKey(path);
      let storeObj = moduleIdsJsonObj[findKey];
      if (storeObj != null && storeObj.type == BUILD_TYPE_COMMON) {
        return false;
      }
      return true;
    }
    return true;
  };
};
// ...
```

However, the polyfills is also written in the output bundle file after running Metro, we should remove those code by ourselves.

For example, we made a script file call `removePolyfill.js` in the dir `__async_load_shell__`, you can use it by run:

```shell
node ./__async_load_shell__/removePolyfill.js  {your_different_bundle_file_path}
```

### 2. Load the differential bundle file asynchronously in android.

Because the common bundle file includes all basic codes, we should make sure a good timing to load the common bundle file before loading the differential bundle file.

In the demo app, a guide activity is created to load the common bundle file, which is also used to simulate a **PARENT** activity of the activity using react native. Sometimes, The guide activity can also usually be displayed the entrance of your business which was builded by react native in your official app.

All related code was organized in package `com.marcus.rn.async`. There are some key points about the implementation:

1. We use the `ReactNativeHost` to point the path of common bundle file, and call `createReactContextInBackground()` to initialize the context of React Native and load the common bundle file.
2. In order to get approximate finish time of loading common bundle file, we use `addReactInstanceEventListener()` of `ReactInstanceManager` to add custom listener and monitor the event  `onReactContextInitialized` to indicate the finish of loading common bundle file.
3. We redefine `ReactActivityDelegate` class to suit the scene of loading asynchronously. which can be found by name with `AsyncLoadActivityDelegate.java`.
4. Because the guide activity and the container activity of react native **MUST** shared the same `AsyncLoadActivityDelegate` object, we build a **singleton** class called `AsyncLoadManager` to provider the object.
5. The **load time of react view** will be displayed by log and toast, which records time period from `onCreate()` of the activity to monitor the event called `CONTENT_APPEARED`.
6. As for the global variable problem in javascript, we should clear the context of react native before reused it. we provides a simple way to fix the problem by rebuild the `AsyncLoadActivityDelegate` object, you can see the code in `prepareReactNativeEnv()` in `AsyncLoadManager`.

### 3. Load the differential bundle file asynchronously in iOS.

## 📋 Contributing

PRs accepted.

## 📋 License

[MIT © Marcus Ma.](./LICENSE)
