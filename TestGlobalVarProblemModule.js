var globalData = "Alice";

export function getGlobalData() {
  return globalData;
}

export function setGlobalData(data) {
  globalData = data;
  return data;
}
