<script setup>
import { onBeforeUnmount, onBeforeMount, reactive, ref } from "vue";
import { useUserStore } from "../store/users/useUserStore.js";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

import Navbar from "@/examples/PageLayout/Navbar.vue";
import ArgonInput from "@/components/ArgonInput.vue";
import ArgonSwitch from "@/components/ArgonSwitch.vue";
import ArgonButton from "@/components/ArgonButton.vue";
import SelectPosition from "../components/SelectPosition.vue";
import api from "../api/users/login";

const body = document.body;
const store = useStore();
const router = useRouter();
const userStore = useUserStore();

// 로그인 폼 상태
const signinForm = reactive({
  email: "",
  password: "",
  loginType: null, // 1: farmer, 2: buyer
});

// Remember Me
const rememberMe = ref(false);

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

const typeMap = {
  1: "FARMER",
  2: "BUYER"
};

const onSubmit = async () => {
  if (!signinForm.email || !signinForm.password) {
    alert("이메일과 비밀번호를 입력해주세요.");
    return;
  }

  try {
    const userResp = await api.signIn({
      email: signinForm.email,
      password: signinForm.password,
      role: typeMap[signinForm.loginType] // "FARMER" or "BUYER"
    });

    if (userResp.success) {
      console.log("Remember Me:", rememberMe.value);
      userStore.setWithEncrypt(userResp.data, rememberMe.value); // true → localStorage, false → sessionStorage
      router.push(
          signinForm.loginType === 1 ? "/farmer/dashboard" : "/buyer/dashboard"
      );
    } else {
      alert("로그인에 실패했습니다.");
    }
  } catch (error) {
    console.error("Login error:", error);
    alert("로그인 중 오류가 발생했습니다.");
  }
};
</script>

<template>
  <div class="container top-0 position-sticky z-index-sticky">
    <div class="row">
      <div class="col-12">
        <Navbar
          isBlur="blur border-radius-lg my-3 py-2 start-0 end-0 mx-4 shadow"
          :darkMode="true"
          isBtn="bg-gradient-success"
        />
      </div>
    </div>
  </div>

  <main class="mt-0 main-content">
    <section>
      <div class="page-header min-vh-100">
        <div class="container">
          <div class="row">
            <!-- 로그인 폼 -->
            <div class="mx-auto col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-lg-0">
              <div class="card card-plain">
                <div class="pb-0 card-header text-start">
                  <h4 class="font-weight-bolder">로그인</h4>
                  <p class="mb-0">아이디와 비밀번호를 입력해주세요.</p>
                </div>

                <div class="card-body">
                  <form @submit.prevent="onSubmit">
                    <!-- 이메일 -->
                    <div class="mb-3">
                      <ArgonInput
                        v-model="signinForm.email"
                        type="email"
                        placeholder="이메일"
                        name="email"
                        id="emailInput"
                        size="lg"
                        @keyup.enter="onSubmit"
                      />
                    </div>

                    <!-- 비밀번호 -->
                    <div class="mb-3">
                      <ArgonInput
                        v-model="signinForm.password"
                        type="password"
                        placeholder="비밀번호"
                        name="password"
                        id="passwordInput"
                        size="lg"
                        @keyup.enter="onSubmit"
                      />
                    </div>

                    <!-- 회원 유형 선택 -->
                    <label class="form-label fw-bold text-dark">회원 유형 선택</label>
                    <SelectPosition v-model="signinForm.loginType" />

                    <!-- 기억하기 -->
                    <ArgonSwitch
                      v-model="rememberMe"
                      id="rememberMeSwitch"
                      name="rememberMe"
                    >
                      기억하기
                    </ArgonSwitch>

                    <!-- 로그인 버튼 -->
                    <div class="text-center">
                      <ArgonButton
                        class="mt-4"
                        variant="gradient"
                        color="success"
                        fullWidth
                        size="lg"
                        type="submit"
                      >
                        로그인
                      </ArgonButton>
                    </div>
                  </form>
                </div>

                <!-- 회원가입 -->
                <div class="px-1 pt-0 text-center card-footer px-lg-2">
                  <p class="mx-auto mb-0 text-sm">
                    회원가입을 안하셨나요?
                    <router-link
                      to="/signup"
                      class="text-success text-gradient font-weight-bold"
                    >
                      회원 가입하기
                    </router-link>
                  </p>
                </div>

                <!-- 비밀번호 찾기 -->
                <div class="px-1 pt-0 text-center card-footer px-lg-2">
                  <p class="mx-auto mb-0 text-sm">
                    비밀번호를 잃어버렸나요?
                    <a
                      href="javascript:;"
                      class="text-success text-gradient font-weight-bold"
                    >
                      비밀번호 재발급
                    </a>
                  </p>
                </div>
              </div>
            </div>

            <!-- 오른쪽 이미지 영역 -->
            <div
              class="top-0 my-auto text-center col-6 d-lg-flex d-none h-100 pe-0 position-absolute end-0 justify-content-center flex-column"
            >
              <div
                class="position-relative bg-gradient-primary h-100 m-3 px-7 border-radius-lg d-flex flex-column justify-content-center overflow-hidden"
                style="background-image: url('https://raw.githubusercontent.com/creativetimofficial/public-assets/master/argon-dashboard-pro/assets/img/signin-ill.jpg'); background-size: cover;"
              >
                <span class="mask bg-gradient-success opacity-6"></span>
                <h4 class="mt-5 text-white font-weight-bolder position-relative">
                  "Attention is the new currency"
                </h4>
                <p class="text-white position-relative">
                  The more effortless the writing looks, the more effort the
                  writer actually put into the process.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </main>
</template>
