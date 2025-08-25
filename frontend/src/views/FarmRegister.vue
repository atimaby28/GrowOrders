<script setup>
import { ref, computed, onBeforeMount, onMounted, onBeforeUnmount } from "vue";
import { useStore } from "vuex";
import setNavPills from "@/assets/js/nav-pills.js";
import setTooltip from "@/assets/js/tooltip.js";
import CropCard from "@/components/CropCard.vue";
import { useUserStore } from "@/store/users/login.js";
import { storeToRefs } from 'pinia';
import axios from '@/plugins/axiosinterceptor.js';

import FarmHeader from "@/views/components/farm/FarmHeader.vue"
import FarmForm from "@/views/components/farm/FarmForm.vue";
import ImageUploader from "@/views/components/farm/ImageUploader.vue";



// --- 상태 ---
const mode = ref("register"); // 'register' | 'detail'
const userStore = useUserStore();
const { userInfo } = storeToRefs(userStore);
const userId = computed(() => userInfo.value?.id ?? userInfo.value?.accountId ?? '');
//const role = computed(() => userStore.userInfo.type); // '1'이면 농부
const isReadOnly = computed(() => mode.value !== "register");

// 폼 상태
const farmName = ref("");
const selectedLocation = ref("");
const addressDetail = ref("");
const area = ref("");
const description = ref("");

// 파일
const profileFile = ref(null);

// 우측 카드(필요 시)
const selectedCropInfo = ref(null);

// 상수
const LOCATION_OPTIONS = [
  "경기도",
  "강원도",
  "충청북도",
  "충청남도",
  "전라북도",
  "전라남도",
  "경상북도",
  "경상남도",
  "제주특별자치도",
];

// 동작
async function handleSubmit() {
  const dto = {
    name: farmName.value,
    region: selectedLocation.value,
    address: addressDetail.value,
    size: area.value ? Number(area.value) : null,
    contents: description.value,
  };
  const fd = new FormData();
  fd.append('dto', new Blob([JSON.stringify(dto)], { type: 'application/json' }));
  if (profileFile.value) fd.append('image', profileFile.value);

  const res = await axios.post('/farms/register', fd);
  const created = res.data?.data;
  console.log('created farm:', created);

  mode.value = "detail";
  alert("농장이 등록되었습니다.");
}
function enterEditMode() {
  mode.value = "register";
}

// 레이아웃/효과
const body = document.getElementsByTagName("body")[0];
const store = useStore();

onMounted(() => {
  store.state.isAbsolute = true;
  setNavPills();
  setTooltip();
});
onBeforeMount(() => {
  store.state.imageLayout = "profile-overview";
  store.state.showNavbar = false;
  store.state.showFooter = true;
  store.state.hideConfigButton = true;
  body.classList.add("profile-overview");
});
onBeforeUnmount(() => {
  store.state.isAbsolute = false;
  store.state.imageLayout = "default";
  store.state.showNavbar = true;
  store.state.showFooter = true;
  store.state.hideConfigButton = false;
  body.classList.remove("profile-overview");
});
</script>

<template>
  <main>
    <div class="container-fluid">
      <div
        class="page-header min-height-300"
        :style="{
          backgroundImage: `url(${require('@/assets/img/farm_register.jpg')})`,
          marginRight: '-24px',
          marginLeft: '-25%'
        }"
      >
        <span class="mask bg-gradient-success opacity-6"></span>
      </div>
      <div class="card shadow-lg mt-n6"></div>
    </div>

    <div class="py-4 container-fluid">
      <div class="row">
        <!-- 왼쪽: 등록/상세 카드 -->
        <div class="col-md-8">
          <div class="card">
            <!-- 상단 헤더/버튼 -->
            <FarmHeader
              :mode="mode"
              :canEdit="mode === 'detail'"
              @submit="handleSubmit"
              @edit="enterEditMode"
            />

            <!-- 상단 헤더/버튼 -->
            <!-- <FarmHeaderActions
              :mode="mode"
              :canEdit="mode === 'detail' && role === '1'"
              @submit="handleSubmit"
              @edit="enterEditMode"
            /> -->

            <!-- 농부만 폼 표시 -->
            <!-- <div class="card-body" v-if="role === '1'"> -->
            <div class="card-body">  
              <FarmForm
                :readOnly="isReadOnly"
                :userId="userId"
                v-model:farmName="farmName"
                v-model:selectedLocation="selectedLocation"
                v-model:addressDetail="addressDetail"
                v-model:area="area"
                v-model:description="description"
                :locationOptions="LOCATION_OPTIONS"
              />

              <div class="row">
                <div class="col-md-12">
                  <ImageUploader
                    v-model:file="profileFile"
                    :disabled="isReadOnly"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 오른쪽: 품목 카드(있을 때만) -->
        <div class="col-md-4" v-if="selectedCropInfo">
          <CropCard :crop="selectedCropInfo" />
        </div>
      </div>
    </div>
  </main>
</template>
