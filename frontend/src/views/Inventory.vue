<script setup>
import api from "@/api/inventory";
import InventorysTable from "@/views/components/InventorysTable.vue";
import FarmInfoSlider from "@/views/components/FarmInfoSlider.vue";
import PageNation from "@/views/components/PageNation.vue";
import { onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import { useUserStore } from "@/store/users/useUserStore.js";
import { useInventoryStore } from "@/store/inventories/useInventoryStore.js";

const route = useRoute();
const userStore = useUserStore();
const inventoryStore = useInventoryStore();

//재고 검색 필터 변경시 재고 값 변경
const updateCropList = (cropList) => {
  inventoryStore.farmInfo.cropList = cropList;
};

//재고 목록 조회 api 호출
const getInventoryList = async (farmId) => {
  const data = await api.getInventory(farmId);

  if (data && data.success) {
    Object.assign(inventoryStore.farmInfo, data.data); //reactive 객체 내부 값만 업데이트
  } else {
    alert("데이터를 불러오지 못하였습니다.");
  }
};

//쿼리 param 변경 감지시 재고 목록 조회 함수 호출
watch(
    () => route.query.farmId,
    (newParamFarmId) => {
      getInventoryList(newParamFarmId);
    }
);

//페이지 로드시 데이터 가져오기
onMounted(() => {
  getInventoryList(route.query.farmId);
});
</script>

<template>
  <div v-if="userStore.user.ownedFarm" class="py-4 container-fluid">
    <FarmInfoSlider :key="route.query.farmId" />
    <InventorysTable
        @updateCropList="updateCropList"
        @getInventoryList="getInventoryList"
    />
  </div>
  <div class="mt-4 row col-12">
    <PageNation :pageCount="3" />
  </div>
</template>
