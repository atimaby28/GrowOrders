<script setup>
import { reactive, onMounted } from 'vue'
import api from '../../api/order'

const farmData = reactive({ list: [] })

const S3_BASE = "https://cowmin-s3.s3.ap-northeast-2.amazonaws.com/" 
const DEFAULT_FARM_IMG = new URL('../../assets/img/default-farm-image.png', import.meta.url).href

const farmImageUrl = (farm) => {
  const key = farm?.farmImage
  if (!key) return DEFAULT_FARM_IMG
  const base = S3_BASE.replace(/\/+$/, '')
  const path = String(key).replace(/^\/+/, '')
  return `${base}/${path}`
}

const cropTypes = (farm) => {
  if (Array.isArray(farm?.crops)) {
    const types = farm.crops.map(c => c?.type).filter(Boolean)
    return [...new Set(types)]
  }
  return farm?.cropType ? [farm.cropType] : []
}

onMounted( async () => {
  try {
    const list = await api.farmList()
    farmData.list = Array.isArray(list) ? list : []
  } catch (error) {
    console.error('API 호출 오류:', error)
    farmData.list = []
  }
})
</script>

<template>
  <div class="card mb-4">
    <div class="card-header pb-0">
      <div class="d-flex align-items-center justify-content-between">
        <div class="d-flex align-items-center">
          <h6 class="mb-0 me-3" style="white-space: nowrap;">농장 목록</h6>
          <div class="input-group input-group-sm ms-3" style="max-width: 250px;">
            <span class="input-group-text text-body px-2">
              <i class="fas fa-search" aria-hidden="true"></i>
            </span>
            <input type="text" class="form-control" placeholder="Type here...">
          </div>
        </div>

        <label class="position-absolute end-2 top-5 mt-1 me-3 text-xs">
          정렬 기준:
          <select id="sortOption" class="form-select form-select-sm d-inline w-auto ms-1">
            <option value="year">재배 방식</option>
            <option value="crop">작물 이름</option>
            <option value="yield">예측 생산량</option>
            <option value="farm">농장 이름</option>
            <option value="region">지역</option>
          </select>
        </label>
      </div>
    </div>

    <div class="card-body px-4 pt-2 pb-4">
      <div class="row g-3 mt-2">
        <div
          class="col-12 col-sm-6 col-lg-3"
          v-for="(farm, index) in farmData.list"
          :key="farm?.id ?? index"
        >
          <div class="card h-100 farm-card">
            <div class="card-body">
              <div class="d-flex align-items-center mb-3">
                <img
                :src="farmImageUrl(farm)"
                :alt="`farm-${farm?.id}`"
                class="farm-card-img"
                loading="lazy"
                @error="e => (e.target.src = DEFAULT_FARM_IMG)"
                >
              </div>        
              <div class="mb-2">
                <h6 class=" d-block">{{ farm?.name ?? '' }}</h6>
              </div>
              <div class="mb-2">
                <div class="text-sm">지역 : {{ farm?.region ?? '-' }}</div>
              </div>
              <div class="mb-2">
                <small class="text-secondary d-block">작물
                  <span
                    v-for="(t, i) in cropTypes(farm)"
                    :key="i"
                    class="badge bg-light text-dark border rounded-pill"
                  >
                    {{ t }}
                  </span>
                  <span v-if="cropTypes(farm).length === 0" class="text-sm">-</span>
                </small>
                <div class="d-flex flex-wrap gap-1">
                </div>
              </div>
              <div class="mb-2">
                <small class="text-secondary d-block">재배 방식 {{ farm?.cropCultivateType ?? farm?.cultivateType ?? '-' }}</small>
              </div>
              <div class="mb-3">
                <small class="text-secondary d-block">예측 생산량 (㎏/10a)  {{ farm?.cropExpectedQuantity ?? farm?.predictedYield ?? '-' }}</small>
              </div>
              <div class="d-flex justify-content-center">
                <router-link
                  :to="{ name: 'OrderCreate', params: { FarmId: farm?.id } }"
                  class="btn btn-sm bg-gradient-success text-white order-btn"
                >
                  주문
                </router-link>
              </div>
            </div>
          </div>
        </div>
        <div v-if="farmData.list.length === 0" class="col-12">
          <div class="text-center text-secondary text-xs py-4">
            표시할 농장이 없습니다.
          </div>
        </div>
      </div>
    </div>
  </div>
  <router-view></router-view>
</template>

<style scoped>
.farm-card-img {
  width: 100%;
  height: 220px;        
  object-fit: cover;
  background: #f8f9fa;
  border-top-left-radius: 0.5rem;   
  border-top-right-radius: 0.5rem;

}
@media (min-width: 992px) {
  .farm-card-img {
    height: 220px;
  }
}

.farm-card {
  border: 2px solid #e1e9ff;
  border-color: #c5dfd3;     
  border-radius: 12px;            
  overflow: hidden;              
}

.farm-card:hover {
  border-color: #98e2b3;
  box-shadow: 0 6px 18px rgba(30, 58, 138, 0.08);
}

.order-btn {
  display: block;     
  width: 100%;        
  margin: 0 auto;     
}
</style>