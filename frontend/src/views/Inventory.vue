<script setup>
import api from '@/api/inventory'
import InventorysTable from '@/views/components/InventorysTable.vue'
import FarmInfoSlider from '@/views/components/FarmInfoSlider.vue'
import { useUserStore } from '@/store/users/useUserStore.js'
import { reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const userStore = useUserStore()

//response Data 저장
let farmInfo = reactive({
  user_id: null,
  id: null,
  name: '',
  region: '',
  address: '',
  size: null,
  contents: '',
  profile_image_url: null,
  cropList: [],
})

//재고 검색 필터 변경시 재고 값 변경
const updateCropList = (cropList) => {
  farmInfo.cropList = cropList
}

//재고 목록 조회 api 호출
const getInventoryList = async (farmId) => {
  const data = await api.getInventory(farmId)

  if (data && data.success) {
    Object.assign(farmInfo, data.data) //reactive 객체 내부 값만 업데이트
  } else {
    alert('데이터를 불러오지 못하였습니다.')
  }
}

//페이지 로드시 데이터 가져오기
onMounted(() => {
  getInventoryList(route.query.farmId)
})

//쿼리 param 변경 감지
watch(
  () => route.query.farmId,
  (newParamFarmId) => {
    getInventoryList(newParamFarmId)
  },
)
</script>

<template>
  <div v-if="userStore.user.ownedFarm" class="py-4 container-fluid">
    <FarmInfoSlider :key="route.query.farmId" :currentFarmId="route.query.farmId" :farmInfo="farmInfo" />
    <InventorysTable :inventories="farmInfo.cropList" @updateCropList="updateCropList" @change="updateCropList" />
  </div>
</template>
