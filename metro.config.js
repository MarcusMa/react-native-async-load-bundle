/**
 * Metro configuration for React Native
 * https://github.com/facebook/react-native
 *
 * @format
 */

const baseMetroConfig = require('./metro.config.base.js');
const buildConfig = {
  type: baseMetroConfig.BuildType.DEFAULT,
};
module.exports = {
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: false,
      },
    }),
  },
  serializer: {
    createModuleIdFactory: baseMetroConfig.buildCreateModuleIdFactory(
      buildConfig,
    ),
    processModuleFilter: baseMetroConfig.buildProcessModuleFilter(buildConfig),
  },
};
