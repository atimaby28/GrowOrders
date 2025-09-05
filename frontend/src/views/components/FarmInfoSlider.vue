<script setup>
import { useRoute, useRouter } from "vue-router";
import { useUserStore } from "@/store/users/useUserStore.js";
import { useInventoryStore } from "@/store/inventories/useInventoryStore.js";

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const inventoryStore = useInventoryStore();

const currentFarmIndex = userStore.user.ownedFarm.findIndex(
  (f) => f.id == route.query.farmId
); //현재 농장의 인덱스 번호

//이전 슬라이드 농장 정보
const changeFarmPrev = () => {
  if (currentFarmIndex - 1 < 0) {
    router.push({
      path: "/inventory", // 이동할 라우트 path
      query: {
        farmId:
          userStore.user.ownedFarm[userStore.user.ownedFarm.length - 1].id,
      }, // 쿼리 파라미터
    });
  } else {
    router.push({
      path: "/inventory",
      query: { farmId: userStore.user.ownedFarm[currentFarmIndex - 1].id },
    });
  }
};

//다음 슬라이드 농장 정보
const changeFarmNext = () => {
  if (currentFarmIndex + 1 > userStore.user.ownedFarm.length - 1) {
    router.push({
      path: "/inventory",
      query: { farmId: userStore.user.ownedFarm[0].id },
    });
  } else {
    router.push({
      path: "/inventory",
      query: { farmId: userStore.user.ownedFarm[currentFarmIndex + 1].id },
    });
  }
};

//농장 보기 버튼 클릭시
const farmDetail = (farmId) => {
  router.push(`/farms/${farmId}`);
};
</script>

<template>
  <div class="row">
    <div class="col-12">
      <div class="carousel-wrapper">
        <div id="farmCarousel" class="carousel slide">
          <!-- active : 활성화 되어 보이는 슬라이드 표시 -->
          <div class="carousel-indicators">
            <button
              v-for="(farm, farmIndex) in userStore.user.ownedFarm"
              :key="farm.id"
              type="button"
              data-bs-target="#farmCarousel"
              :data-bs-slide-to="farmIndex"
              :class="{ active: farm.id == route.query.farmId }"
              :aria-label="`Slide ${farmIndex + 1}`"
            ></button>
          </div>
          <!-- 캐러셀 내용 -->
          <div class="carousel-inner">
            <div
              v-for="farm in userStore.user.ownedFarm"
              :key="farm.id"
              :class="[
                'carousel-item',
                { active: farm.id == route.query.farmId },
              ]"
            >
              <div class="farm-card">
                <img :src="inventoryStore.farmInfo.farmImage" alt="" />
                <!-- 블러 처리-->
                <div class="overlay"></div>
                <div class="carousel-caption text-start">
                  <h5 class="farm-title">{{ inventoryStore.farmInfo.name }}</h5>
                  <p class="farm-contents">
                    {{ inventoryStore.farmInfo.contents }}
                  </p>
                  <button
                    class="btn bg-gradient-success"
                    @click="farmDetail(farm.id)"
                  >
                    농장 보기
                  </button>
                </div>
              </div>
            </div>
          </div>
          <!-- 전/후 화살표 버튼 -->
          <button
            class="carousel-control-prev custom-control"
            type="button"
            data-bs-target="#farmCarousel"
            data-bs-slide="prev"
            @click.prevent="changeFarmPrev()"
          >
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
          </button>
          <button
            class="carousel-control-next custom-control"
            type="button"
            data-bs-target="#farmCarousel"
            data-bs-slide="next"
            @click.prevent="changeFarmNext()"
          >
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped src="@/assets/css/inventory/farmInfo.css"></style>
