<!-- src/views/CartView.vue -->
<template>
  <div class="container py-4">
    <h2 class="mb-3">🛒 장바구니</h2>

    <!-- 장바구니 목록 -->
    <div v-if="cartStore.carts.length > 0">
      <ul class="list-group">
        <li
          v-for="(cart, index) in cartStore.carts"
          :key="index"
          class="list-group-item d-flex justify-content-between align-items-center"
        >
          <span>
            📦 {{ cart.cropType || '상품' }}
            (수량: {{ cart.quantity }}, 가격: {{ cart.totalPrice }}원)
          </span>
          <span class="badge bg-secondary">ID: {{ cart.id }}</span>
        </li>
      </ul>
    </div>
    <p v-else class="text-muted">장바구니가 비어있습니다.</p>

    <hr class="my-4" />

    <!-- 장바구니 담기 -->
    <div class="row g-2 align-items-center">
      <div class="col-md-5">
        <input
          v-model.number="cropMgtId"
          type="number"
          class="form-control"
          placeholder="Crop Mgt ID 입력"
        />
      </div>
      <div class="col-md-5">
        <input
          v-model.number="quantity"
          type="number"
          class="form-control"
          placeholder="수량 입력"
          min="1"
        />
      </div>
      <div class="col-md-2">
        <button class="btn btn-success w-100" @click="addCartItem">
          장바구니 담기
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from "vue";
import { useCartStore } from "../../store/test/cartStore";
import { useUserStore } from "../../store/users/useUserStore";

const userStore = useUserStore();
const cartStore = useCartStore();

const cropMgtId = ref(null);
const quantity = ref(1);

onMounted(async () => {
  userStore.checkLogin();
  await cartStore.fetchCarts(Number(userStore.getUserId()));
  await nextTick();
});

const addCartItem = async () => {
  if (!userStore.getUserId()) {
    alert("로그인이 필요합니다.");
    return;
  }
  if (!cropMgtId.value || !quantity.value) {
    alert("상품 ID와 수량을 입력하세요.");
    return;
  }

  const cartData = {
    cropOrderManagementId: Number(cropMgtId.value), // DTO 필드명 맞춤
    quantity: Number(quantity.value),
  };

  await cartStore.addToCart(Number(cropMgtId.value), cartData);

  // 입력값 초기화
  cropMgtId.value = null;
  quantity.value = 1;
};
</script>
