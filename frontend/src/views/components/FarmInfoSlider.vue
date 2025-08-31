<script setup>
import { defineProps } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/users/useUserStore.js'

const props = defineProps(['farmInfo'])
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const currentFarmIndex = userStore.user.ownedFarm.findIndex((f) => f.id == route.query.farmId) //현재 농장의 인덱스 번호

/* 이전 슬라이드 농장 정보 */
const changeFarmPrev = () => {
  //이전 농장이 더 없으면 마지막 농장 슬라이드로
  if (currentFarmIndex - 1 < 0) {
    router.push({
      path: '/inventory', // 이동할 라우트 path
      query: { farmId: userStore.user.ownedFarm[userStore.user.ownedFarm.length - 1].id }, // 쿼리 파라미터
    })
  }
  //있으면 이전 농장 슬라이드로
  else {
    router.push({
      path: '/inventory',
      query: { farmId: userStore.user.ownedFarm[currentFarmIndex - 1].id },
    })
  }
}

/* 다음 슬라이드 농장 정보 */
const changeFarmNext = () => {
  //다음 농장이 더 없으면 처음 농장 슬라이드로
  if (currentFarmIndex + 1 > userStore.user.ownedFarm.length - 1) {
    router.push({
      path: '/inventory',
      query: { farmId: userStore.user.ownedFarm[0].id },
    })
  }
  //있으면 다음 농장 슬라이드로
  else {
    router.push({
      path: '/inventory',
      query: { farmId: userStore.user.ownedFarm[currentFarmIndex + 1].id },
    })
  }
}
</script>

<template>
  <div class="row">
    <div class="col-12">
      <div class="carousel-wrapper">
        <div id="carouselExampleCaptions" class="carousel slide">
          <div class="carousel-indicators">
            <!-- active : 활성화 되어 보이는 슬라이드 -->
            <button v-for="(farm, farmIndex) in userStore.user.ownedFarm" :key="farm.id" type="button" data-bs-target="#carouselExampleCaptions" :data-bs-slide-to="farmIndex" :class="{ active: farm.id == route.query.farmId }" :aria-label="`Slide ${farmIndex + 1}`"></button>
          </div>

          <div class="carousel-inner">
            <div v-for="farm in userStore.user.ownedFarm" :key="farm.id" :class="['carousel-item', { active: farm.id == route.query.farmId }]">
              <img :src="props.farmInfo.profile_image_url" class="d-block w-100" alt="" />
              <div class="carousel-caption d-none d-md-block">
                <h5>{{ props.farmInfo.name }}</h5>
                <p>{{ props.farmInfo.contents }}</p>
              </div>
            </div>
          </div>

          <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="prev" @click.prevent="changeFarmPrev()">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
          </button>
          <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="next" @click.prevent="changeFarmNext()">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.carousel-wrapper {
  width: 60%;
  margin: 0 auto;
}

.carousel img {
  max-height: 500px;
  object-fit: cover;
  border-radius: 1rem;
}

.carousel-inner h5 {
  font-size: 36px;
  color: #ffff00;
}
</style>
