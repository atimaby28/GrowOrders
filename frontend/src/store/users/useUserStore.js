import { defineStore } from 'pinia'
import { ref, reactive } from 'vue'
import { EncryptStorage } from 'encrypt-storage'

const encryptKey = process.env.VUE_APP_ENCRYPTION_KEY || "default-secret-key-here";

const encryptLocalStorage = new EncryptStorage(encryptKey, { prefix: 'OL_', storageType: 'localStorage' });
const encryptSessionStorage = new EncryptStorage(encryptKey, { prefix: 'OL_', storageType: 'sessionStorage' });

export const useUserStore = defineStore('user', () => {
  const isLogin = ref(false)
  const user = reactive({
    id: null,
    email: '',
    name: '',
    ownedFarm: null,
    role: '',
  })

  // 로그인 상태 체크
  const checkLogin = () => {
    const key = 'user'
    let stored = encryptLocalStorage.getItem(key)
    if (!stored) stored = encryptSessionStorage.getItem(key)

    if (stored) {
      const data = typeof stored === 'string' ? JSON.parse(stored) : stored
      user.id = data.id
      user.email = data.email
      user.name = data.name
      user.role = data.role
      user.ownedFarm = data.ownedFarm
      isLogin.value = true
      return true
    } else {
      isLogin.value = false
      return false
    }
  }

  // 로그인 정보 저장
  const setWithEncrypt = (data, rememberMe) => {
    const key = 'user'
    user.id = data.id
    user.email = data.email
    user.name = data.name
    user.role = data.role
    user.ownedFarm = data.ownedFarm
    isLogin.value = true

    if (rememberMe) {
      encryptLocalStorage.setItem(key, data);
    } else {
      encryptSessionStorage.setItem(key, data);
    }
  }

  // 로그아웃
  const logout = () => {
    encryptLocalStorage.removeItem('user')
    encryptSessionStorage.removeItem('user')
    isLogin.value = false
    user.id = null
    user.email = ''
    user.name = ''
    user.role = ''
    user.ownedFarm = null
  }

const getUserId = () => {
  const key = "user";
  let stored = encryptLocalStorage.getItem(key);
  if (!stored) stored = encryptSessionStorage.getItem(key);

  if (stored) {
    const data = typeof stored === "string" ? JSON.parse(stored) : stored;
    return data.id;
  }
  return null;
};

const getUserRole = () => {
  const key = "user";
  let stored = encryptLocalStorage.getItem(key);
  if (!stored) stored = encryptSessionStorage.getItem(key);

  if (stored) {
    const data = typeof stored === "string" ? JSON.parse(stored) : stored;
    return data.role || "GUEST"; // 안전하게 기본값 설정
  }
  return "GUEST";
};


  return { isLogin, user, checkLogin, setWithEncrypt, logout, getUserId, getUserRole }
})
