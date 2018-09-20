const TokenKey = 'Authorization';
export function getToken() {
  return sessionStorage.getItem(TokenKey);
}
export function setToken(token) {
  return sessionStorage.setItem(TokenKey, token);
}
export function removeToken() {
  sessionStorage.removeItem(TokenKey)
}

const UserKey = 'access-user';
export function getUser() {
  return sessionStorage.getItem(UserKey);
}
export function setUser(user) {
  return sessionStorage.setItem(UserKey, user);
}
export function removeUser() {
  window.sessionStorage.removeItem(UserKey);
}

const PermissionsKey = 'access-user-permissions';
export function getPermissions() {
  return sessionStorage.getItem(PermissionsKey);
}
export function setPermissions(user) {
  return sessionStorage.setItem(PermissionsKey, user);
}
export function removePermissions() {
  window.sessionStorage.removeItem(PermissionsKey)
}

const RoutersKey = "routers-setting";
export function getRouters() {
  return sessionStorage.getItem(RoutersKey);
}
export function setRouters(routers) {
  return sessionStorage.setItem(RoutersKey, routers);
}
export function removeRouters() {
  window.sessionStorage.removeItem(RoutersKey)
}

/**
 * 是否已经加载了 路由节点
 */
const RoutersLoaded = "routers-loaded";
export function getRoutersLoaded() {
  return sessionStorage.getItem(RoutersLoaded);
}
export function setRoutersLoaded(routersLoaded) {
  return sessionStorage.setItem(RoutersLoaded, routersLoaded);
}
export function removeRoutersLoaded() {
  window.sessionStorage.removeItem(RoutersLoaded)
}

