<template>
  <div>
    <h2>주문</h2>
    <button @click="createOrder">임시 주문 생성</button>
    <div v-if="orderStore.temporaryOrder">임시 주문 생성됨</div>

    <h3>최종 주문</h3>
    <button @click="confirmOrder">주문 확정</button>
    <div v-if="orderStore.completedOrder">
      {{ orderStore.completedOrder }}
    </div>
  </div>
</template>

<script setup>
import { useOrderStore } from '../../store/test/orderStore';

const orderStore = useOrderStore();

const createOrder = () => {
  const cartIds = [1, 2];
  orderStore.createOrder(cartIds);
};

const confirmOrder = () => {
  const orderData = {
    address: '서울시 강남구',
    receiver: '홍길동'
  };
  orderStore.confirmOrder(orderData);
};
</script>
