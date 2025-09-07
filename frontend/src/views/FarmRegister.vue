<script setup>
import { ref, onMounted, onBeforeMount, onBeforeUnmount } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { useUserStore } from "@/store/users/useUserStore.js";
import axios from "@/plugins/axiosinterceptor.js";
import setNavPills from "@/assets/js/nav-pills.js";
import setTooltip from "@/assets/js/tooltip.js";
import FarmHeader from "@/views/components/farm/FarmHeader.vue";
import FarmForm from "@/views/components/farm/FarmForm.vue";
import FarmImageUploader from "@/views/components/farm/FarmImageUploader.vue";

const router = useRouter();
const store = useStore();
const userStore = useUserStore();

onMounted(() => userStore.checkLogin());

const farmName = ref("");
const selectedLocation = ref("");
const addressDetail = ref("");
const area = ref("");
const description = ref("");
const profileFile = ref(null);

const LOCATION_OPTIONS = [
  "서울특별시",
  "부산광역시",
  "대구광역시",
  "인천광역시",
  "광주광역시",
  "대전광역시",
  "울산광역시",
  "세종특별자치시",
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

async function handleCreate() {
  const dto = {
    name: farmName.value,
    region: selectedLocation.value,
    address: addressDetail.value,
    size: area.value ? Number(area.value) : null,
    contents: description.value,
    farmImage: profileFile,
  };
  const fd = new FormData();
  fd.append(
    "dto",
    new Blob([JSON.stringify(dto)], { type: "application/json" })
  );
  if (profileFile.value) fd.append("farmImageUrl", profileFile.value); // 백엔드 @RequestPart("image")

  const res = await axios.post("/farms/register", fd); //농장 등록 api 호출
  const created = res.data?.data ?? res.data; //응답 데이터
  const newId = created.ownedFarm[created.ownedFarm.length - 1].id;

  userStore.user.ownedFarm = created.ownedFarm;

  router.push({ name: "FarmDetail", params: { id: newId } });
  alert("농장이 등록되었습니다.");
}

const body = document.getElementsByTagName("body")[0];
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
          marginLeft: '-25%',
        }"
      >
        <span class="mask bg-gradient-success opacity-6"></span>
      </div>
      <div class="card shadow-lg mt-n6"></div>
    </div>
    <div class="py-4 container-fluid">
      <div class="row">
        <div class="col-md-8">
          <div class="card">
            <FarmHeader
              mode="register"
              :canEdit="false"
              @submit="handleCreate"
            />
            <div class="card-body">
              <FarmForm
                :readOnly="false"
                v-model:farmName="farmName"
                v-model:selectedLocation="selectedLocation"
                v-model:addressDetail="addressDetail"
                v-model:area="area"
                v-model:description="description"
                :locationOptions="LOCATION_OPTIONS"
              />
              <div class="row">
                <div class="col-md-12">
                  <FarmImageUploader
                    v-model:file="profileFile"
                    :disabled="false"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>
