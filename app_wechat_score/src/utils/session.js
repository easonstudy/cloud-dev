import StorageCache from 'web-storage-cache'

/*var wsCache = new WebStorageCache({
  // [可选] 'localStorage', 'sessionStorage', window.localStorage, window.sessionStorage
  //        或者其他实现了 [Storage API] 的storage实例.
  //        默认 'localStorage'.
  storage: 'localStorage',
  // [可选]  类型Number，公共超时事件设置。默认无限大 单位秒
  exp: Infinity
});*/
let storage = new StorageCache({
  exp: 12 * 60 * 60
});
console.log(JSON.stringify(storage));

const TokenKey = 'Authorization';
export function getToken() {
  return storage.get(TokenKey);
}
export function setToken(token) {
  return storage.set(TokenKey, token);
}
export function removeToken() {
  storage.delete(TokenKey)
}

const UserKey = 'access-user';
export function getUser() {
  return storage.get(UserKey);
}
export function setUser(user) {
  return storage.set(UserKey, user);
}
export function removeUser() {
  storage.delete(UserKey);
}

/*var storage = sessionStorage;

const TokenKey = 'Authorization';
export function getToken() {
  return storage.getItem(TokenKey);
}
export function setToken(token) {
  return storage.setItem(TokenKey, token);
}
export function removeToken() {
  storage.removeItem(TokenKey)
}

const UserKey = 'access-user';
export function getUser() {
  return storage.getItem(UserKey);
}
export function setUser(user) {
  return storage.setItem(UserKey, user);
}
export function removeUser() {
  storage.removeItem(UserKey);
}*/




















/*const storage= localStorage; //sessionStorage

const TokenKey = 'Authorization';
export function getToken() {
  return storage.getItem(TokenKey);
}
export function setToken(token) {
  return storage.setItem(TokenKey, token);
}
export function removeToken() {
  storage.removeItem(TokenKey)
}

const UserKey = 'access-user';
export function getUser() {
  return storage.getItem(UserKey);
}
export function setUser(user) {
  return storage.setItem(UserKey, user);
}
export function removeUser() {
  window.storage.removeItem(UserKey);
}*/


