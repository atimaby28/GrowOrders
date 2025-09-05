import { defineStore } from 'pinia';
import { ref } from 'vue';
import api from '../../plugins/axiosinterceptor';

export const useCartStore = defineStore('cart', () => {
  // state
  const carts = ref([]);

  // actions
  const fetchCarts = async (userId) => {
    const res = await api.get(`/cart/${userId}`, {
      withCredentials: true
    });
    carts.value = res.data;
  };

  const addToCart = async (cropMgtId, cartData) => {
    await api.post(`/cart/add/${cropMgtId}`, cartData, {
      withCredentials: true
    });
    await fetchCarts(cartData.userId);
  };

  return {
    carts,
    fetchCarts,
    addToCart
  };
});
