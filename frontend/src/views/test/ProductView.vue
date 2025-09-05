<!-- src/views/product/ProductDetail.vue -->
<template>
  <div class="container py-4">
    <h2 class="mb-3">🛒 상품 상세</h2>

    <div class="card p-3">
      <h4 class="mb-2">{{ product.name }}</h4>
      <p class="text-muted mb-3">{{ product.description }}</p>

      <div class="row g-3 mb-3">
        <!-- 사이즈 선택 -->
        <div class="col-md-6">
          <label class="form-label">사이즈</label>
          <select v-model="selectedSize" class="form-select">
            <option disabled value="">사이즈 선택</option>
            <option v-for="s in sizeOptions" :key="s" :value="s">{{ s }}</option>
          </select>
        </div>

        <!-- 색상 선택 -->
        <div class="col-md-6">
          <label class="form-label">색상</label>
          <select v-model="selectedColor" class="form-select">
            <option disabled value="">색상 선택</option>
            <option v-for="c in colorOptions" :key="c" :value="c">{{ c }}</option>
          </select>
        </div>
      </div>

      <div class="mb-3">
        <label class="form-label">수량</label>
        <input v-model.number="quantity" type="number" min="1" class="form-control" />
      </div>

      <div class="d-flex align-items-center gap-3 mb-3">
        <div v-if="matchedVariant">
          <div>선택된 옵션 가격: {{ matchedVariant.price.toLocaleString() }}원</div>
          <div class="text-muted">재고: {{ matchedVariant.stock ?? '정보없음' }}</div>
        </div>
        <div v-else class="text-danger">
          해당 옵션 조합의 상품이 없습니다.
        </div>
      </div>

      <div class="d-flex gap-2">
        <button
          class="btn btn-success"
          :disabled="!canAdd"
          @click="addToCart"
        >
          장바구니 담기
        </button>
        <button class="btn btn-outline-secondary" @click="goCart">
          장바구니로 이동
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
// src/views/product/ProductDetail.vue
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useCartStore } from '@/store/test/cartStore';
import { useUserStore } from '@/store/users/useUserStore';
import api from '@/plugins/axiosinterceptor';

const router = useRouter();
const route = useRoute();
const cartStore = useCartStore();
const userStore = useUserStore();

const productId = Number(route.params.id); // /product/:id 같은 라우팅 가정

// 상품/옵션(variants) 데이터
const product = ref({ id: productId, name: '', description: '' });
const variants = ref([]); // [{ id: cropMgtId, size, color, price, stock }, ...]

// 선택값
const selectedSize = ref('');
const selectedColor = ref('');
const quantity = ref(1);

// 옵션 목록 (중복 제거)
const sizeOptions = computed(() => [...new Set(variants.value.map(v => v.size))]);
const colorOptions = computed(() => [...new Set(
  variants.value
    .filter(v => (selectedSize.value ? v.size === selectedSize.value : true))
    .map(v => v.color)
)]);

// 현재 선택으로 매칭되는 variant(= cropMgtId 대상)
const matchedVariant = computed(() => {
  if (!selectedSize.value || !selectedColor.value) return null;
  return variants.value.find(v => v.size === selectedSize.value && v.color === selectedColor.value) || null;
});

const canAdd = computed(() => !!matchedVariant.value && quantity.value > 0);

onMounted(async () => {
  userStore.checkLogin();
  // 실제 API로 교체하세요.
  // 예: const { data } = await api.get(`/products/${productId}`);
  // product.value = data.product;
  // variants.value = data.variants;

  // 데모용 목업
  product.value = { id: productId, name: '신선한 토마토', description: '싱싱한 토마토입니다.' };
  variants.value = [
    { id: 1, size: 'S', color: 'Red', price: 5000, stock: 20 },
    { id: 2, size: 'M', color: 'Red', price: 7000, stock: 15 },
    { id: 3, size: 'L', color: 'Red', price: 9000, stock: 10 },
    { id: 4, size: 'S', color: 'Yellow', price: 5200, stock: 5 },
  ];
});

const addToCart = async () => {
  if (!userStore.getUserId?.() && !userStore.user?.id) {
    alert('로그인이 필요합니다.');
    return;
  }
  if (!matchedVariant.value) {
    alert('옵션을 선택하세요.');
    return;
  }

  const cropMgtId = matchedVariant.value.id; // 여기서 자동 결정!
  const cartData = {
    userId: Number(userStore.getUserId?.() ?? userStore.user?.id),
    quantity: Number(quantity.value || 1),
  };

  try {
    await cartStore.addToCart(Number(cropMgtId), cartData);
    // 장바구니로 이동
    router.push({ name: 'CartView' }); // 라우터에 name 설정되어 있어야 함
  } catch (e) {
    console.error(e);
    alert('장바구니 담기에 실패했습니다.');
  }
};

const goCart = () => router.push({ name: 'CartView' });
</script>
