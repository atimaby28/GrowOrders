<script setup>
import { ref, computed, onMounted } from "vue";
import { farmCrops } from "@/views/components/farm/FarmCrops.js";
import FarmCard from "@/views/components/farm/FarmCard.vue";

const { farmData, flatCrops, farmImageUrl, load, DEFAULT_FARM_IMG } = farmCrops();
const q = ref("");
const sortKey = ref("crop");

const filtered = computed(() => {
  const items = flatCrops.value.filter(({ farm, crop }) => {
    const text = `${farm?.name || ""} ${farm?.region || ""} ${crop?.type || ""} ${crop?.cultivateType || ""}`.toLowerCase();
    return text.includes(q.value.toLowerCase());
  });

  const byStr = (get) => (a, b) => (get(a) || "").localeCompare(get(b) || "");
  const byNumDesc = (get) => (a, b) => (Number(get(b)) || 0) - (Number(get(a)) || 0);

  switch (sortKey.value) {
    case "farm":
      return items.sort(byStr((x) => x.farm?.name));
    case "region":
      return items.sort(byStr((x) => x.farm?.region));
    case "yield":
      return items.sort(byNumDesc((x) => x.crop?.expectedQuantity));
    case "crop":
    default:
      return items.sort(byStr((x) => x.crop?.type));
  }
});
onMounted(load);
</script>

<template>
  <div class="card mb-4">
    <div class="card-header pb-0">
      <div class="d-flex align-items-center justify-content-between">
        <div class="d-flex align-items-center">
          <h6 class="mb-0 me-3" style="white-space: nowrap">농장 목록</h6>
          <div class="input-group input-group-sm ms-3" style="max-width: 250px">
            <span class="input-group-text text-body px-2">
              <i class="fas fa-search" aria-hidden="true"></i>
            </span>
            <input v-model="q" type="text" class="form-control" placeholder="Type here..." />
          </div>
        </div>
        <label class="position-absolute end-2 top-1 mt-1 me-3 text-xs">
          정렬 기준:
          <select v-model="sortKey" id="sortOption" class="form-select form-select-sm d-inline w-auto ms-1">
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
          v-for="(item, index) in filtered"
          :key="`${item.farm?.id}-${item.crop?.id ?? index}`"
        >
          <FarmCard
            :farm="item.farm"
            :crop="item.crop"
            :image-url="farmImageUrl(item.farm)"
            :default-image="DEFAULT_FARM_IMG"
          />
        </div>
        <div v-if="farmData.list.length === 0" class="col-12">
          <div class="text-center text-secondary text-xs py-4">표시할 농장이 없습니다.</div>
        </div>
      </div>
    </div>
  </div>
  <router-view />
</template>

<style scoped>

</style>
