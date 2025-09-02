<script setup>
import { computed } from "vue";

const props = defineProps({
  farm: { type: Object, required: true },
  crop: { type: Object, default: null },
  imageUrl: { type: String, required: true },
  defaultImage: { type: String, required: true },
});

const toOrderRoute = computed(() => ({
  name: "OrderCreate",
  params: { FarmId: props.farm?.id },
}));
</script>

<template>
  <div class="card h-100 farm-card">
    <div class="card-body">
      <div class="d-flex align-items-center mb-3">
        <img
          :src="imageUrl"
          :alt="`farm-${farm?.id}`"
          class="farm-card-img"
          loading="lazy"
          @error="(e) => (e.target.src = defaultImage)"
        />
      </div>
      <div class="mb-2">
        <h6 class="d-block">{{ farm?.name }}</h6>
      </div>
      <div class="mb-2">
        <div class="text-sm">지역 : {{ farm?.region }}</div>
      </div>
      <div class="mb-2">
        <small class="text-secondary d-block">작물 : {{ crop?.type ?? '-' }}</small>
      </div>
      <div class="mb-2">
        <small class="text-secondary d-block">재배 방식 : {{ crop?.cultivateType ?? '-' }}</small>
      </div>
      <div class="mb-3">
        <small class="text-secondary d-block">예측 생산량 (㎏/10a) : {{ crop?.expectedQuantity ?? '-' }}</small>
      </div>
      <div class="d-flex justify-content-center">
        <router-link :to="toOrderRoute" class="btn btn-sm bg-gradient-success text-white order-btn">
          주문
        </router-link>
      </div>
    </div>
  </div>
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
