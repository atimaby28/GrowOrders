<script setup>
import FarmCard from "@/views/components/farm/FarmCard.vue";

defineProps({
  items: { type: Array, required: true },
  farmImageUrl: { type: Function, required: true },
  defaultImage: { type: String, default: "" },
  detailRouteName: { type: String, default: "FarmDetail" },
});
</script>

<template>
  <div class="row g-3 mt-2" v-if="items.length">
    <div
      class="col-12 col-sm-6 col-lg-3"
      v-for="(item, index) in items"
      :key="`${item.farm?.id}-${item.crop?.id ?? index}`"
    >
      <FarmCard
        :farm="item.farm"
        :crop="item.crop"
        :image-url="farmImageUrl(item.farm)"
        :default-image="defaultImage"
        :detail-route-name="detailRouteName"
      />
    </div>
  </div>
  <div v-else>
    <slot name="empty" />
  </div>
</template>
