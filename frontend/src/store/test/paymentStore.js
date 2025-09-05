import { ref } from 'vue';
import { defineStore } from 'pinia';
import api from '../../plugins/axiosinterceptor';

export const usePaymentStore = defineStore('payment', () => {
  // state
  const paymentResult = ref(null);

  // actions
  const validatePayment = async (paymentData) => {
    const res = await api.post('/payment/validation', paymentData, {
      withCredentials: true
    });
    paymentResult.value = res.data.data;
  };

  return {
    paymentResult,
    validatePayment
  };
});
