import { ref } from 'vue';
import { defineStore } from 'pinia';
import api from '../../plugins/axiosinterceptor';

export const useOrderStore = defineStore('order', () => {
  // state
  const temporaryOrder = ref(null);
  const completedOrder = ref(null);

  // actions
  const createOrder = async (cartIds) => {
    await api.post('/order/create', { cartIds }, {
      withCredentials: true
    });
    temporaryOrder.value = true;
  };

  const confirmOrder = async (orderData) => {
    const res = await api.post('/order/confirm', orderData, {
      withCredentials: true
    });
    completedOrder.value = res.data;
  };

  return {
    temporaryOrder,
    completedOrder,
    createOrder,
    confirmOrder
  };
});
