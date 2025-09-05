<script setup>
import { onMounted } from "vue";
import { farmListFilter } from "@/views/components/farm/farmListFilter.js";
import { farmListScroll } from "@/views/components/farm/farmListScroll.js";
import { farmCrops } from "@/views/components/farm/FarmCrops.js";
import FarmToolbar from "@/views/components/farm/FarmToolbar.vue";
import FarmGrid from "@/views/components/farm/FarmGrid.vue";
import DEFAULT_FARM_IMG from "@/assets/img/default-farm-image.png";

const { farmData, flatCrops, farmImageUrl, load } = farmCrops();

const {
  q,
  selectedCrop,
  selectedRegion,
  selectedCultivate,
  cropTypeOptions,
  regionOptions,
  cultivateOptions,
  filtered,
} = farmListFilter(flatCrops);

const { visibleItems, canLoadMore, loadMore, sentinel, loadingMore } =
  farmListScroll(filtered, { batchSize: 30, threshold: 0.2 });

onMounted(load);
</script>

<template>
  <div class="container-narrow mx-auto">
    <div class="card mb-4">
      <FarmToolbar
        title="상품 목록"
        :crop-options="cropTypeOptions"
        :region-options="regionOptions"
        :cultivate-options="cultivateOptions"
        v-model:q="q"
        v-model:selected-crop="selectedCrop"
        v-model:selected-region="selectedRegion"
        v-model:selected-cultivate="selectedCultivate"
      />

      <div class="card-body px-4 pt-2 pb-4">
        <div class="mt-4">
          <FarmGrid
            :items="visibleItems"
            :farm-image-url="farmImageUrl"
            :default-image="DEFAULT_FARM_IMG"
          >
            <template #empty>
              <div
                v-if="farmData.list.length"
                class="text-center text-secondary py-5"
              >
                해당 조건의 농장이 없습니다.
              </div>
            </template>
          </FarmGrid>
        </div>

        <div class="d-flex justify-content-center">
          <div ref="sentinel" class="infinite-sentinel my-4"></div>
        </div>

        <div class="d-flex justify-content-center">
          <button
            v-if="canLoadMore"
            class="btn btn-outline-secondary btn-sm"
            :disabled="loadingMore"
            @click="loadMore"
          >
            {{ loadingMore ? "불러오는 중…" : "더 보기" }}
          </button>
        </div>
      </div>
    </div>
  </div>

  <router-view />
</template>

<style scoped>
.container-narrow {
  max-width: 1200px;
}
.infinite-sentinel {
  min-height: 24px;
}
</style>
