<template>
  <div>
    <h2>결제</h2>
    <button @click="doPayment">결제 검증</button>
    <div v-if="paymentStore.paymentResult !== null">
      결제 성공 여부: {{ paymentStore.paymentResult }}
    </div>
  </div>
</template>

<script setup>
import { usePaymentStore } from '../../store/test/paymentStore';
import PortOne from "@portone/browser-sdk/v2"

const paymentStore = usePaymentStore();

const randomId = () => {
    return [...crypto.getRandomValues(new Uint32Array(2))]
        .map((word) => word.toString(16).padStart(8, "0"))
        .join("")
}

const doPayment = async () => {

  const paymentId = randomId()

  const payment = await PortOne.requestPayment({
      storeId: "store-3513dda2-1134-48b8-bc99-e3d1487021d7", // 본인 계정의 포트원 스트어 ID, 결제 정보-> 연동 정보 -> 오른쪽 상단에 있는 ID
      channelKey: "channel-key-68a00971-dcc7-4450-8470-28f4a47b2018", // 본인이 가한 채널의 ID, 결제 정보-> 연동 정보 -> 채널 선택 -> 채널 키
      paymentId, // 랜덤한 ID값, 결제 내역을 구분할 때 사용
      orderName: "딸기", // 주문 이름, 상품의 이름이 아니라 주문, 일반적으로 여러 상품을 주문할 때 첫번째 상품의 이름을 사용
      totalAmount: 10000, // 결제할 금액
      currency: "KRW", // 결제할 통화
      payMethod: "EASY_PAY", // 결제 수단
      customData: {
        productIdxList: [1]
      },
    })

  paymentStore.validatePayment({ paymentId: payment.paymentId, orderId: 1 });
};
</script>
