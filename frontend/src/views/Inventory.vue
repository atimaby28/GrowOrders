<script setup>
import api from '@/api/inventory'
import InventorysTable from '@/views/components/InventorysTable.vue'
import FarmInfoSlider from './components/FarmInfoSlider.vue'
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

//response Data 저장
const farmInfo = ref([]) //url param으로 받은 농장의 정보
let inventoriesData = ref([]) //농장의 재고들 데이터 정보

//사용자 토큰에서 가져온 농장 아이디 정보
const farmsIdList = reactive([{ id: 1 }, { id: 2 }])

const getInventoryList = async (farmId) => {
  const data = await api.getInventory(farmId)

  if (data && data.success) {
    farmInfo.value = data.data
    inventoriesData.value = data.data.crops
  } else {
    alert('데이터를 불러오지 못하였습니다.')
  }
}

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
  <div v-if="farmsIdList" class="py-4 container-fluid">
    <FarmInfoSlider :key="route.query.farmId" :currentFarmId="route.query.farmId" :farmInfo="farmInfo" :farmsId="farmsIdList" />
    <InventorysTable :inventories="inventoriesData" />
  </div>
</template>
