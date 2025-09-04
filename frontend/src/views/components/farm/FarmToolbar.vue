<script setup>
import { computed } from "vue";

const props = defineProps({
  q: { type: String, default: "" },
  selectedCrop: { type: String, default: "" },
  selectedRegion: { type: String, default: "" },
  selectedCultivate: { type: String, default: "" },
  cropOptions: { type: Array, default: () => [] },
  regionOptions: { type: Array, default: () => [] },
  cultivateOptions: { type: Array, default: () => [] },
  title: { type: String, default: "농장 목록" },
});

const emit = defineEmits([
  "update:q",
  "update:selectedCrop",
  "update:selectedRegion",
  "update:selectedCultivate",
]);

const qModel = computed({
  get: () => props.q,
  set: v => emit("update:q", v),
});
const cropModel = computed({
  get: () => props.selectedCrop,
  set: v => emit("update:selectedCrop", v),
});
const regionModel = computed({
  get: () => props.selectedRegion,
  set: v => emit("update:selectedRegion", v),
});
const cultivateModel = computed({
  get: () => props.selectedCultivate,

  set: v => emit("update:selectedCultivate", v),
});
</script>

<template>
  <div class="card-header pb-0">
    <div class="d-flex align-items-center flex-wrap flex-md-nowrap">
      <h6 class="mb-0 me-2" style="white-space: nowrap">{{ title }}</h6>

      <div class="d-flex align-items-center flex-wrap gap-3 ms-3 ms-md-4">
        <!-- 작물 -->
        <select v-model="cropModel" class="form-select form-select-sm w-auto select-fix">
          <option value="">작물 전체</option>
          <option v-for="t in cropOptions" :key="t" :value="t">{{ t }}</option>
        </select>

        <!-- 지역 -->
        <select v-model="regionModel" class="form-select form-select-sm w-auto select-fix">
          <option value="">지역 전체</option>
          <option v-for="r in regionOptions" :key="r" :value="r">{{ r }}</option>
        </select>

        <!-- 재배방식 -->
        <select v-model="cultivateModel" class="form-select form-select-sm w-auto select-fix">
          <option value="">재배방식 전체</option>
          <option
            v-for="c in cultivateOptions"
            :key="c || '__none__'"
            :value="c || '__none__'"
          >
            {{ c || '미정' }}
          </option>
        </select>
      </div>

      <!-- 검색 (우측) -->
      <div class="ms-auto d-flex align-items-center">
        <div class="input-group input-group-sm search-box">
          <span class="input-group-text text-body px-2">
            <i class="fas fa-search" aria-hidden="true"></i>
          </span>
          <input
            v-model="qModel"
            type="text"
            class="form-control"
            placeholder="농장/지역/작물 검색..."
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.search-box { max-width: 260px; }
@media (max-width: 576px) { .search-box { max-width: 100%; } }

.select-fix{
  min-width: 130px;
  padding-right: 2rem !important;                     
  background-position: right .5rem center !important; 
  background-size: 14px 10px;
  height: 32px;                                      
  line-height: 1.25;
}
.search-box .form-control,
.search-box .input-group-text { height: 32px; }
</style>
