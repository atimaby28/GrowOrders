<script setup>
import { computed } from "vue";

const props = defineProps({
  farm: { type: Object, required: true },
  crop: { type: Object, default: null },
  imageUrl: { type: String, required: true },
  defaultImage: { type: String, required: true },
  detailRouteName: { type: String, default: "FarmDetail" },
});

const detailTo = computed(() =>
  props.farm?.id
    ? { name: props.detailRouteName, params: { id: props.farm.id } }
    : null
);

const toOrderRoute = computed(() => ({
  name: "OrderCreate",
  params: { FarmId: props.farm?.id }, // 기존 로직 유지
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

      <!-- 제목 줄: 이름 + 아이콘만 클릭 가능 -->
      <div class="mb-2 d-flex align-items-center">
        <template v-if="detailTo">
          <router-link :to="detailTo" class="farm-name-link">
            {{ farm?.name }}
          </router-link>
          <router-link
            :to="detailTo"
            class="farm-link-icon ms-2"
            title="농장 상세 보기"
            aria-label="농장 상세 보기"
          >
            <i class="fas fa-external-link-alt"></i>
          </router-link>
        </template>
        <h6 v-else class="mb-0">{{ farm?.name }}</h6>
      </div>

      <div class="mb-2">
        <small class="text-sm">작물 : {{ crop?.type ?? '-' }}</small>
      </div>
      <div class="mb-2">
        <small class="text-secondary d-block">지역 : {{ farm?.region }}</small>
      </div>
      <div class="mb-2">
        <small class="text-secondary d-block">재배 방식 : {{ crop?.cultivateType ?? '-' }}</small>
      </div>
      <div class="mb-3">
        <small class="text-secondary d-block">예측 생산량 (㎏/10a) : {{ crop?.expectedQuantity ?? '-' }}</small>
      </div>

      <div class="d-flex justify-content-center">
        <router-link :to="toOrderRoute" class="btn btn-sm bg-gradient-success text-white order-btn">
          주 문
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
  .farm-card-img { height: 220px; }
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

/* 이름/아이콘만 클릭 가능하게 */
.farm-name-link {
  font-weight: 500;
  color: #212529;
  text-decoration: none;             /* 기본은 밑줄 없음 */
}
.farm-name-link:hover,
.farm-name-link:focus-visible,
.farm-name-link:active {
  color: inherit;                    /* 색상은 그대로 유지 */
  text-decoration: underline;        /* 밑줄 표시 */
  text-underline-offset: 3px;        /* 밑줄과 글자 간격 */
  text-decoration-thickness: 1px;    /* 밑줄 두께 */
}
.farm-link-icon {
  color: #6c757d;
  text-decoration: none;
  line-height: 1;
}
.fa-external-link-alt { font-size: 0.8em; }


.order-btn { display: block; width: 100%; margin: 0 auto; }
</style>
