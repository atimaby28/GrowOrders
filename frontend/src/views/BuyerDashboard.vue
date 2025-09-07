<script setup>
import MasterCard from "@/examples/Cards/MasterCard.vue";
import DefaultInfoCard from "@/examples/Cards/DefaultInfoCard.vue";
import PaymentCard from "./components/PaymentCard.vue";
import InvoiceCard from "./components/InvoiceCard.vue";
import BillingCard from "./components/BillingCard.vue";
import TransactionCard from "./components/TransactionCard.vue";

import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useCartStore } from "../store/test/cartStore";
import { useUserStore } from "../store/users/useUserStore";

const router = useRouter();
const goToCart = () => {
  router.push("/cartview2");
};

const cartStore = useCartStore();
const userStore = useUserStore();

const cartItemCount = ref(0);
const cartTotalPrice = ref(0);

onMounted(async () => {
  await loadCartData();
});

const loadCartData = async () => {
  if (!userStore.user.id) {
    return;
  }

  try {
    await cartStore.fetchCarts(Number(userStore.user.id));
    cartItemCount.value = cartStore.carts.length;
    cartTotalPrice.value = cartStore.carts.reduce((total, cart) => total + cart.totalPrice, 0);
  } catch (error) {
    console.error("장바구니 데이터 로드 실패:", error);
  }
};

/*
const goToCart = async () => {
  await loadCartData(); // 최신 장바구니 데이터 로드
  router.push('/cart');
};
*/
import PushClientSave from "@/views/components/push/PushClientSave.vue"

</script>

<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-lg-8">
        <div class="row mt-4">
          <div class="col-xl-6 mb-xl-0 mb-4">
            <master-card />
          </div>
          <div class="col-xl-6">
            <div class="row">
              <div class="col-md-6">
                <default-info-card
                  :icon="{
                    component: 'fas fa-cart-plus',
                    background: 'bg-gradient-success',
                  }"
                  title="장바구니"
                  :description="`현재 담긴 상품: ${cartItemCount}개`"
                  :value="`총액: ${cartTotalPrice.toLocaleString()} ₩`"
                  @click="goToCart"
                  style="cursor: pointer;"
                />
              </div>
              <div class="col-md-6">
                <default-info-card
                  :icon="{
                    component: 'fas fa-tags',
                    background: 'bg-gradient-success',
                  }"
                  title="쿠폰"
                  description="보유 쿠폰: 3장"
                  value="최대 할인: 5,000 ₩"
                />
              </div>
            </div>
          </div>
          <div class="col-md-12 mb-4">
            <payment-card />
          </div>
        </div>
      </div>
      <div class="col-lg-4">
        <invoice-card class="mt-4" />
      </div>
    </div>
    <div class="row">
      <div class="col-md-7">
        <billing-card />
      </div>
      <div class="col-md-5">
        <transaction-card />
      </div>
      <PushClientSave/>
    </div>
  </div>
</template>
