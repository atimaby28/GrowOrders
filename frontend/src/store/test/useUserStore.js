// stores/useUserStore.js
import { defineStore } from "pinia";
import { ref, reactive } from "vue";
import { EncryptStorage } from "encrypt-storage";

const encryptKey = process.env.VUE_APP_ENCRYPTION_KEY || "fallback-secret";
const encryptLocalStorage = new EncryptStorage(encryptKey, { prefix: "OL_", storageType: "localStorage" });
const encryptSessionStorage = new EncryptStorage(encryptKey, { prefix: "OL_", storageType: "sessionStorage" });

export const useUserStore = defineStore("user", () => {
  const isLogin = ref(false);
  const user = reactive({ id: null, email: "", name: "", role: "" });

  const safeParse = (v) => {
    try { return typeof v === "string" ? JSON.parse(v) : v; }
    catch { return null; }
  };

  // 로그인 상태 체크(복호화/JSON 파싱 에러에도 안전)
  const checkLogin = () => {
    let stored = encryptLocalStorage.getItem("user");
    if (!stored) stored = encryptSessionStorage.getItem("user");

    const data = safeParse(stored);
    if (data && data.id) {
      user.id = data.id;
      user.email = data.email;
      user.name = data.name;
      user.role = data.role;
      isLogin.value = true;
      return true;
    } else {
      // 깨진 데이터는 제거
      encryptLocalStorage.removeItem("user");
      encryptSessionStorage.removeItem("user");
      isLogin.value = false;
      user.id = null; user.email = ""; user.name = ""; user.role = "";
      return false;
    }
  };

  // 로그인 정보 저장
  const setWithEncrypt = (data, rememberMe) => {
    user.id = data.id;
    user.email = data.email;
    user.name = data.name;
    user.role = data.role;
    isLogin.value = true;
    if (rememberMe) encryptLocalStorage.setItem("user", data);
    else encryptSessionStorage.setItem("user", data);
  };

  const logout = () => {
    encryptLocalStorage.removeItem("user");
    encryptSessionStorage.removeItem("user");
    isLogin.value = false;
    user.id = null; user.email = ""; user.name = ""; user.role = "";
  };

  return { isLogin, user, checkLogin, setWithEncrypt, logout };
});
