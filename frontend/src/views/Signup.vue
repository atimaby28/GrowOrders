<script setup>
import { onBeforeUnmount, onBeforeMount, reactive, ref, watch } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

import Navbar from "@/examples/PageLayout/Navbar.vue";
import AppFooter from "@/examples/PageLayout/Footer.vue";
import ArgonInput from "@/components/ArgonInput.vue";
import ArgonCheckbox from "@/components/ArgonCheckbox.vue";
import ArgonButton from "@/components/ArgonButton.vue";
import BirthDay from "../components/BirthDay.vue";
import SelectPosition from "../components/SelectPosition.vue";
import CellPhone from "../components/CellPhone.vue";

import api from "../api/users/login";

const body = document.getElementsByTagName("body")[0];
const store = useStore();
const router = useRouter();

onBeforeMount(() => {
  store.state.hideConfigButton = true;
  store.state.showNavbar = false;
  store.state.showSidenav = false;
  store.state.showFooter = false;
  body.classList.remove("bg-gray-100");
});

onBeforeUnmount(() => {
  store.state.hideConfigButton = false;
  store.state.showNavbar = true;
  store.state.showSidenav = true;
  store.state.showFooter = true;
  body.classList.add("bg-gray-100");
});

const profileImage = ref(null);
const previewUrl = ref("");
const passwordError = ref("");

const onFileChange = (e) => {
  const file = e.target.files[0];
  if (file) {
    if (!file.type.startsWith("image/")) {
      alert("이미지 파일만 업로드 가능합니다.");
      return;
    }
    profileImage.value = file;
    previewUrl.value = URL.createObjectURL(file);
  }
};

// ✅ 폼 데이터 관리
const signInForm = reactive({
  accountId: "",
  password: "",
  confirmPassword: "",
  name: "",
  email: "",
  birthDate: "",
  phoneNumber: "",
  role: "", // FARMER | BUYER
});

// ✅ 유효성 검사
const validateForm = () => {
  // accountId
  if (!signInForm.accountId.trim()) 
    return { success: false, message: "아이디를 입력하세요." };
  if (signInForm.accountId.length > 20) 
    return { success: false, message: "아이디는 최대 20자리까지 가능합니다." };

  // 비밀번호
  if (!signInForm.password.trim()) 
    return { success: false, message: "비밀번호를 입력하세요." };
  if (signInForm.password.length < 8) 
    return { success: false, message: "비밀번호는 최소 8자리 이상이어야 합니다." };
  if (!/^[A-Z]/.test(signInForm.password))
    return { success: false, message: "비밀번호의 첫 글자는 대문자여야 합니다." };
  if (signInForm.password !== signInForm.confirmPassword) 
    return { success: false, message: "비밀번호가 일치하지 않습니다." };

  // 이름
  if (!signInForm.name.trim()) 
    return { success: false, message: "이름을 입력하세요." };
  if (signInForm.name.length > 30) 
    return { success: false, message: "이름은 최대 30자리까지 가능합니다." };

  // 이메일
  if (!signInForm.email.trim()) 
    return { success: false, message: "이메일을 입력하세요." };
  if (!/\S+@\S+\.\S+/.test(signInForm.email)) 
    return { success: false, message: "올바른 이메일을 입력하세요." };

  // 생년월일
  if (!signInForm.birthDate) 
    return { success: false, message: "생년월일을 선택하세요." };
  if (new Date(signInForm.birthDate) >= new Date()) 
    return { success: false, message: "생년월일은 과거 날짜여야 합니다." };

  // 전화번호
  if (!signInForm.phoneNumber.trim()) 
    return { success: false, message: "휴대폰 번호를 입력하세요." };
  if (!/^\+?[0-9\\-]{9,15}$/.test(signInForm.phoneNumber)) 
    return { success: false, message: "전화번호 형식이 올바르지 않습니다." };

  // role
  if (!signInForm.role) 
    return { success: false, message: "회원 유형을 선택하세요." };

  return { success: true, message: null };
};

watch(
  () => [signInForm.password, signInForm.confirmPassword],
  ([newPassword, newConfirmPassword]) => {
    if (newConfirmPassword === "") {
      passwordError.value = "";
      return;
    }
    
    if (newPassword !== newConfirmPassword) {
      passwordError.value = "비밀번호가 일치하지 않습니다.";
    } else {
      passwordError.value = "";
    }
  }
);

// ✅ 회원가입 처리
// import axios from "axios";

const onSubmit = async () => {
  const errorMsg = validateForm();
  if (!errorMsg.success) {
    alert(errorMsg.message);
    return;
  }

  try {
    const formData = new FormData();
    const signupInfo = {
      accountId: signInForm.accountId,
      password: signInForm.password,
      name: signInForm.name,
      email: signInForm.email,
      birthDate: signInForm.birthDate,
      phoneNumber: signInForm.phoneNumber,
      role: signInForm.role === 1 ? "FARMER" : "BUYER",
    };

    formData.append(
      "signupInfo",
      new Blob([JSON.stringify(signupInfo)], { type: "application/json" })
    );

    // 프로필 이미지 없으면 기본 이미지로 대체
    if (profileImage.value) {
      formData.append("profileImageUrl", profileImage.value);
    } else {
      const defaultImageUrl = "https://avatars.githubusercontent.com/u/149382180?v=4";
      const response = await fetch(defaultImageUrl);
      const blob = await response.blob();
      formData.append("profileImageUrl", blob, "default-profile.png");
    }

    const res = await api.signUp(formData);

    console.log("서버 응답:", res);
    alert("회원가입이 완료되었습니다!");
    router.push("/signin"); 
  } catch (err) {
    console.error("회원가입 오류:", err);
    alert("회원가입 중 오류가 발생했습니다.");
  }
};

</script>

<template>
  <div class="container top-0 position-sticky z-index-sticky">
    <div class="row">
      <div class="col-12">
        <navbar isBtn="bg-gradient-light" />
      </div>
    </div>
  </div>
  <main class="main-content mt-0">
    <div
      class="page-header align-items-start min-vh-50 pt-5 pb-11 m-3 border-radius-lg"
      style="
        background-image: url('https://raw.githubusercontent.com/creativetimofficial/public-assets/master/argon-dashboard-pro/assets/img/signup-cover.jpg');
        background-position: top;
      "
    >
      <span class="mask bg-gradient-dark opacity-6"></span>
      <div class="container text-center">
        <h1 class="text-white mb-2 mt-5">어서오세요!</h1>
        <p class="text-lead text-white">
          이 멋진 양식을 사용하여 계정을 만드세요.
        </p>
      </div>
    </div>

    <div class="container">
      <div class="row mt-lg-n10 justify-content-center">
        <div class="col-xl-4 col-lg-5 col-md-7 mx-auto">
          <div class="card z-index-0">
            <div class="card-header text-center pt-4">
              <h5>회원가입</h5>
            </div>
            <div class="card-body">
              <form @submit.prevent="onSubmit">
                <!-- 프로필 이미지 -->
                <div class="text-center mb-4">
                  <label for="profile-upload" style="cursor: pointer;">
                    <img
                      :src="previewUrl || 'https://avatars.githubusercontent.com/u/149382180?v=4'"
                      alt="Profile Preview"
                      class="rounded-circle"
                      style="width: 120px; height: 120px; object-fit: cover;"
                    />
                  </label>
                  <input
                    id="profile-upload"
                    type="file"
                    accept="image/*"
                    @change="onFileChange"
                    style="display: none;"
                  />
                  <p v-if="!previewUrl" class="mt-2 text-secondary">
                    프로필 이미지를 업로드하세요
                  </p>
                </div>

                <label class="form-label fw-bold">이메일</label>
                <argon-input
                  id="email"
                  type="email"
                  placeholder="이메일 입력"
                  v-model="signInForm.email"
                />

                <label class="form-label fw-bold">아이디</label>
                <argon-input
                  id="accountId"
                  type="text"
                  placeholder="아이디 입력"
                  v-model="signInForm.accountId"
                />

                <label class="form-label fw-bold">비밀번호</label>
                <argon-input
                  id="password"
                  type="password"
                  placeholder="비밀번호 입력"
                  v-model="signInForm.password"
                />

                <p>
                  <label v-if="!password" class="text-secondary mb-2">
                    • 비밀번호는 최소 8자리 이상이어야 합니다.<br>
                    • 첫 글자는 대문자여야 합니다.<br>
                    • 비밀번호 확인과 일치해야 합니다.
                  </label>
                </p>

                <label class="form-label fw-bold">비밀번호 확인</label>
                <argon-input
                  id="confirmPassword"
                  type="password"
                  placeholder="비밀번호 확인"
                  v-model="signInForm.confirmPassword"
                />

                <div>
                  <!-- 비밀번호 오류 메시지 표시 -->
                  <label v-if="passwordError" class="text-success text-gradient font-weight-bold">
                    {{ passwordError }}
                  </label>
                </div>
 
                <label class="form-label fw-bold">이름</label>
                <argon-input
                  id="name"
                  type="text"
                  placeholder="이름"
                  v-model="signInForm.name"
                />

                <label class="form-label fw-bold">생년월일</label>
                <BirthDay class="mb-3" v-model="signInForm.birthDate" />

                <label class="form-label fw-bold">휴대폰 번호</label>
                <CellPhone class="mb-3" v-model="signInForm.phoneNumber" />

                <label class="form-label fw-bold">회원 유형 선택</label>
                <SelectPosition class="mb-3" v-model="signInForm.role" />

                <argon-checkbox class="mt-4" checked>
                  <label>
                    <a class="text-dark font-weight-bolder">
                      약관에 동의합니다.
                    </a>
                  </label>
                </argon-checkbox>

                <div class="text-center">
                  <argon-button type="submit" fullWidth color="dark" variant="gradient" class="my-4">
                    회원가입
                  </argon-button>
                </div>
              </form>
              <p class="text-sm text-center mt-3">
                이미 계정이 있나요?
                <a href="/signin" class="text-success text-gradient font-weight-bold">로그인</a>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
  <app-footer />
</template>
