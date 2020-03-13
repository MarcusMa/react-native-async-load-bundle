/**
 * Metro configuration for React Native
 * https://github.com/facebook/react-native
 *
 * @format
 */
const fs = require('fs');
const crypto = require('crypto');
const defaultCreateModuleIdFactory = require('./node_modules/metro/src/lib/createModuleIdFactory');

const BUILD_TYPE_COMMOM = 'common';
const BUILD_TYPE_DEFAULT = 'default';
const BUILD_TYPE_DIFF = 'diff';

const moduleIdsMapFilePath = './repo_for_module_id.json';

/**
 *
 * @param {*} filepath
 */
function getOrCreateModuleIdsJsonObj(filepath) {
  if (fs.existsSync(filepath)) {
    console.log(`init map from file : ${filepath}`);
    let data = fs.readFileSync(filepath, 'utf-8');
    return JSON.parse(data);
  } else {
    return {};
  }
}
/**
 *
 * @param {*} filepath
 * @param {*} jsonObj
 */
function saveModuleIdsJsonObj(filepath, jsonObj) {
  let data = JSON.stringify(jsonObj);
  fs.writeFile(filepath, data, err => {
    if (err) throw err;
    console.log(`Save ${filepath} SUCCESS.`);
  });
}
/**
 * Get the key, which used to find the Id in local storage
 * @param {get} path
 */
function getFindKey(path) {
  let md5 = crypto.createHash('md5');
  md5.update(path);
  let findKey = md5.digest('hex');
  return findKey;
}

var moduleIdsJsonObj = {};

/**
 * Create a Id with local storage.
 */
buildCreateModuleIdFactoryWithLocalStorage = function(buildConfig) {
  if (buildConfig.type == BUILD_TYPE_DEFAULT) {
    // return the orginal createModuleIdFactory implements.
    // Please. See node_modules / metro / src / lib / createModuleIdFactory.js
    return defaultCreateModuleIdFactory;
  } else {
    var currentModuleId = 0;
    moduleIdsJsonObj = getOrCreateModuleIdsJsonObj(moduleIdsMapFilePath);
    for (var key in moduleIdsJsonObj) {
      currentModuleId > moduleIdsJsonObj[key].id
        ? currentModuleId
        : moduleIdsJsonObj[key].id;
    }
    return () => {
      return path => {
        // console.log(`buildType: ${buildType}`);
        let findKey = getFindKey(path);
        if (moduleIdsJsonObj[findKey] == null) {
          moduleIdsJsonObj[findKey] = {
            id: ++currentModuleId,
            type: buildConfig.type,
          };
          saveModuleIdsJsonObj(moduleIdsMapFilePath, moduleIdsJsonObj);
        }
        let id = moduleIdsJsonObj[findKey].id;
        console.log(`createModuleIdFactory id = ${id} for ${path}`);
        return id;
      };
    };
  }
};

buildProcessModuleFilter = function(buildConfig) {
  return moduleObj => {
    if (buildConfig.type == BUILD_TYPE_DIFF) {
      let findKey = getFindKey(moduleObj.path);
      let storeObj = moduleIdsJsonObj[findKey];
      if (storeObj != null && storeObj.type == BUILD_TYPE_COMMOM) {
        return false;
      }
      return true;
    }
    return true;
  };
};

module.exports = {
  BuildType: {
    COMMON: BUILD_TYPE_COMMOM,
    DEFAULT: BUILD_TYPE_DEFAULT,
    DIFF: BUILD_TYPE_DIFF,
  },
  buildCreateModuleIdFactory: buildCreateModuleIdFactoryWithLocalStorage,
  buildProcessModuleFilter: buildProcessModuleFilter,
};
