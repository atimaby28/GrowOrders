// src/store/test/cartStore.js
import { defineStore } from "pinia";
import { ref, computed } from "vue";
import api from "@/plugins/axiosinterceptor";

export const useCartStore = defineStore("cart", () => {
  // ✅ state
  const carts = ref([]);

  // ✅ computed
  const totalQuantity = computed(() =>
    carts.value.reduce((sum, cart) => sum + (cart.quantity || 0), 0)
  );

  const totalPrice = computed(() =>
    carts.value.reduce((sum, cart) => sum + (cart.totalPrice || 0), 0)
  );

  // ✅ actions
  const addToCart = async (cropMgtId, cartData) => {
    try {
      const res = await api.post(`/cart/add/${cropMgtId}`, cartData, {
        withCredentials: true,
      });
      return res.data;
    } catch (error) {
      console.error("장바구니 추가 실패:", error.response || error);
      throw error;
    }
  };

  const fetchCarts = async (userId) => {
    try {
      const res = await api.get(`/cart/${userId}`);
      carts.value = res.data;
    } catch (error) {
      console.error("장바구니 불러오기 실패:", error.response || error);
    }
  };

  return {
    // state
    carts,
    // computed
    totalQuantity,
    totalPrice,
    // actions
    addToCart,
    fetchCarts,
  };
});
