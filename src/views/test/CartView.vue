<!-- src/views/CartView.vue -->
<template>
  <div class="container py-4">
    <h2 class="mb-3">🛒 장바구니</h2>

    <!-- 장바구니 불러오기 -->
    <!-- <div class="mb-3">
      <button class="btn btn-primary" @click="loadCarts">장바구니 불러오기</button>
    </div> -->

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
    <div class="input-group mb-3">
      <input
        v-model.number="cropMgtId"
        type="number"
        class="form-control"
        placeholder="Crop Mgt ID 입력"
      />
      <button class="btn btn-success" @click="addCartItem">장바구니 담기</button>
    </div>
  </div>
</template>

<script setup>
// src/views/CartView.vue (script setup)
import { ref, onMounted, nextTick } from "vue";
import { useCartStore } from "../../store/test/cartStore";
import { useUserStore } from "../../store/users/useUserStore";

const userStore = useUserStore();
const cartStore = useCartStore();

const cropMgtId = ref(null);

onMounted(async () => {
  userStore.checkLogin();
  await cartStore.fetchCarts(Number(userStore.getUserId()));
  console.log(cartStore.carts);
  await nextTick();
  console.log("user(after restore):", { ...userStore.user }); // Proxy 말고 전개해서 보기
});

// const loadCarts = async () => {
//   if (!userStore.getUserId()) {
//     alert("로그인이 필요합니다.");
//     return;
//   }
//   await cartStore.fetchCarts(Number(userStore.getUserId()));
// };

console.log(cartStore.carts);

const addCartItem = async () => {
  if (!userStore.getUserId()) {
    alert("로그인이 필요합니다.");
    return;
  }
  if (!cropMgtId.value) {
    alert("Crop Mgt ID를 입력하세요.");
    return;
  }
  const cartData = {
    userId: Number(userStore.getUserId()),
    quantity: 1,
  };
  await cartStore.addToCart(Number(cropMgtId.value), cartData);
  cropMgtId.value = null;
};

</script>
