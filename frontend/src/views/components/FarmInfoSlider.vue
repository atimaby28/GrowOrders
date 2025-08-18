<script setup>
import { defineProps } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import tomato from '@/assets/img/orderlabs/tomato.jpg'

//farm : 하나의 농장 정보, farmsId : 사용자 토큰에 있는 농장 아이디 객체
const props = defineProps(['farmInfo', 'farmsId'])
const route = useRoute()
const router = useRouter()

const changeFarm = (farmId) => {
  //[해야할 것] : 맨처음, 맨마지막에서 더 넘어가려고 할 때 처리 구현

  const index = props.farmsId.findIndex((f) => f.id == farmId)
  if (index != -1) {
    router.push({
      path: '/inventory', // 이동할 라우트 path
      query: { farmId: props.farmsId[index].id }, // 쿼리 파라미터
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
            <button v-for="(farmId, farmIndex) in props.farmsId" :key="farmId.id" type="button" data-bs-target="#carouselExampleCaptions" :data-bs-slide-to="farmIndex" :class="{ active: farmId.id == route.query.farmId }" :aria-label="`Slide ${farmIndex + 1}`"></button>
          </div>

          <div class="carousel-inner">
            <div v-for="farmId in props.farmsId" :key="farmId.id" :class="['carousel-item', { active: farmId.id == route.query.farmId }]">
              <img :src="tomato" class="d-block w-100" alt="" />
              <div class="carousel-caption d-none d-md-block">
                <h5>{{ props.farmInfo.name }}</h5>
                <p>{{ props.farmInfo.contents }}</p>
              </div>
            </div>
          </div>

          <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="prev" @click.prevent="changeFarm(Number(route.query.farmId) - 1)">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
          </button>
          <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="next" @click.prevent="changeFarm(Number(route.query.farmId) + 1)">
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
