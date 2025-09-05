<script setup>
import { ref, onMounted } from "vue";
import { getMyCarts } from "@/api/product/cart.js"; // ✅ named import

const carts = ref([]);
const error = ref("");
const loading = ref(false);

// 👉 실제 로그인한 사용자 ID로 교체 필요
const userId = 1;

onMounted(async () => {
  loading.value = true;
  try {
    const res = await getMyCarts(userId);
    carts.value = res;
  } catch (e) {
    error.value = "장바구니를 불러올 수 없습니다.";
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="container my-5" style="max-width: 800px;">
    <h4 class="mb-4">내 장바구니</h4>

    <div v-if="loading" class="alert alert-info">불러오는 중...</div>
    <div v-else-if="error" class="alert alert-danger">{{ error }}</div>

    <table v-else class="table table-striped">
      <thead>
        <tr>
          <th>Cart ID</th>
          <th>상품명</th>
          <th>수량</th>
          <th>총 가격</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in carts" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.cropType }}</td>
          <td>{{ item.quantity }}</td>
          <td>{{ item.totalPrice.toLocaleString() }}원</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>