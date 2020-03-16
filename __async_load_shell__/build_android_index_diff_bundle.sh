# 1. Build a bundle with metro.config.diff.js
react-native bundle --platform android --dev false --entry-file index.js --bundle-output __async_load_output__/diff.android.bundle --assets-dest __async_load_output__/index/android --config ./metro.config.diff.js
# 2. Remve the Polyfills part in the output file.
node ./__async_load_shell__/removePolyfill.js  __async_load_output__/diff.android.bundle 