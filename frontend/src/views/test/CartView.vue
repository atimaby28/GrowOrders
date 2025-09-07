<!-- src/views/CartView.vue -->
<template>
  <div class="container-fluid py-4">
    <div class="row">
      <div class="col-12">
        <div class="card">
          <div class="card-header pb-0">
            <div class="d-flex align-items-center">
              <h6 class="mb-0">장바구니</h6>
              <p class="text-sm mb-0 ms-2">총 {{ cartStore.carts.length }}개의 상품이 있습니다</p>
              <div class="ms-auto">
                <button class="btn btn-outline-primary btn-sm me-2" @click="loadCarts">
                  <i class="fas fa-sync-alt me-1"></i>새로고침
                </button>
                <button class="btn btn-success btn-sm" @click="continueShopping">
                  <i class="fas fa-plus me-1"></i>쇼핑 계속하기
                </button>
              </div>
            </div>
          </div>
          
          <div class="card-body px-0 pt-0 pb-2">
            <div v-if="cartStore.carts.length === 0" class="text-center py-5">
              <i class="fas fa-shopping-cart fa-3x text-muted mb-3"></i>
              <h5 class="text-muted">장바구니가 비어있습니다</h5>
              <p class="text-muted">상품을 추가해보세요!</p>
              <button class="btn btn-success" @click="continueShopping">
                쇼핑하러 가기
              </button>
            </div>
            
            <div v-else>
              <!-- 전체 선택 체크박스 -->
              <div class="px-3 py-2 border-bottom">
                <div class="form-check d-flex align-items-center">
                  <div class="custom-checkbox" @click="toggleSelectAll">
                    <div class="checkbox-box" :class="{ 'checked': selectAll }">
                      <i v-if="selectAll" class="fas fa-check"></i>
                    </div>
                    <span class="checkbox-label ms-2">전체선택</span>
                  </div>
                  <input 
                    type="checkbox" 
                    id="selectAll"
                    v-model="selectAll"
                    style="display: none;"
                  >
                </div>
              </div>

              <!-- 장바구니 상품 테이블 -->
              <div class="table-responsive">
                <table class="table align-items-center mb-0">
                  <thead>
                    <tr>
                      <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">선택</th>
                      <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">상품</th>
                      <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">농장</th>
                      <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">수량</th>
                      <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">단가</th>
                      <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">금액</th>
                      <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">삭제</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="cart in cartStore.carts" :key="cart.id">
                      <!-- 선택 체크박스 -->
                      <td class="text-center align-middle">
                        <div class="custom-checkbox justify-content-center" @click="toggleCartItem(cart.id)">
                          <div class="checkbox-box checkbox-large" :class="{ 'checked': selectedItems.includes(cart.id) }">
                            <i v-if="selectedItems.includes(cart.id)" class="fas fa-check"></i>
                          </div>
                        </div>
                      </td>
                      
                      <!-- 상품 정보 -->
                      <td>
                        <div class="d-flex px-2 py-1">
                          <div class="d-flex flex-column justify-content-center">
                            <div class="avatar avatar-sm me-3 bg-gradient-success d-flex align-items-center justify-content-center">
                              <i class="fas fa-seedling text-white"></i>
                            </div>
                          </div>
                          <div class="d-flex flex-column justify-content-center">
                            <h6 class="mb-0 text-sm">{{ getProductName(cart.cropType) }}</h6>
                            <p class="text-xs text-secondary mb-0">{{ cart.cropType || '기타' }}</p>
                          </div>
                        </div>
                      </td>
                      
                      <!-- 농장 정보 -->
                      <td>
                        <p class="text-xs font-weight-bold mb-0">{{ getFarmName(cart.cropType) }}</p>
                        <p class="text-xs text-secondary mb-0">{{ cart.id }}번 농장</p>
                      </td>
                      
                      <!-- 수량 조절 -->
                      <td class="align-middle text-center">
                        <div class="d-flex align-items-center justify-content-center">
                          <button 
                            class="btn btn-link text-dark p-0 me-2" 
                            @click="decreaseQuantity(cart)"
                            :disabled="cart.quantity <= 1"
                          >
                            <i class="fas fa-minus"></i>
                          </button>
                          <span class="mx-2 font-weight-bold">{{ cart.quantity }}</span>
                          <button 
                            class="btn btn-link text-dark p-0 ms-2" 
                            @click="increaseQuantity(cart)"
                          >
                            <i class="fas fa-plus"></i>
                          </button>
                        </div>
                      </td>
                      
                      <!-- 단가 -->
                      <td class="align-middle text-center">
                        <span class="text-secondary text-xs font-weight-bold">{{ getUnitPrice(cart).toLocaleString() }}원</span>
                      </td>
                      
                      <!-- 총 금액 -->
                      <td class="align-middle text-center">
                        <span class="text-secondary text-sm font-weight-bold">{{ cart.totalPrice.toLocaleString() }}원</span>
                      </td>
                      
                      <!-- 삭제 버튼 -->
                      <td class="align-middle text-center">
                        <button 
                          class="btn btn-link text-danger p-0" 
                          @click="removeCartItem(cart)"
                          title="삭제"
                        >
                          <i class="fas fa-trash"></i>
                        </button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 주문 요약 -->
    <div class="row mt-4" v-if="cartStore.carts.length > 0">
      <div class="col-lg-8">
        <!-- 체크된 항목 결제 확인 -->
        <div class="card">
          <div class="card-body">
            <div class="form-check d-flex align-items-center">
                             <div class="custom-checkbox" @click="handlePaymentConfirmation">
                 <div class="checkbox-box" :class="{ 'checked': confirmPayment }">
                   <i v-if="confirmPayment" class="fas fa-check"></i>
                 </div>
                 <span class="checkbox-label ms-3 font-weight-bold">체크된 항목만 결제하시겠습니까?</span>
               </div>
              <input 
                type="checkbox" 
                id="confirmPayment"
                v-model="confirmPayment"
                style="display: none;"
              >
            </div>
          </div>
        </div>
      </div>
      <div class="col-lg-4">
        <div class="card">
          <div class="card-header pb-0">
            <h6 class="mb-0">주문 요약</h6>
          </div>
          <div class="card-body">
            <div class="d-flex justify-content-between mb-2">
              <span>상품 수량:</span>
              <span class="font-weight-bold">{{ selectedItemsCount }}개</span>
            </div>
            <div class="d-flex justify-content-between mb-2">
              <span>상품 금액:</span>
              <span class="font-weight-bold">{{ selectedItemsTotal.toLocaleString() }}원</span>
            </div>
            <hr>
            <div class="d-flex justify-content-between mb-3">
              <span class="text-lg font-weight-bold">총 결제금액:</span>
              <span class="text-lg font-weight-bold text-success">{{ selectedItemsTotal.toLocaleString() }}원</span>
            </div>
            <button 
              class="btn btn-success w-100"
              :disabled="selectedItems.length === 0"
              @click="proceedToOrder"
            >
              <i class="fas fa-credit-card me-1"></i>
              결제하기
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 장바구니 담기 (관리자용) -->
    <div class="row mt-4" v-if="showAdminControls">
      <div class="col-12">
        <div class="card">
          <div class="card-header pb-0">
            <h6 class="mb-0">관리자 - 장바구니 담기</h6>
          </div>
          <div class="card-body">
            <div class="input-group">
      <input
        v-model.number="cropMgtId"
        type="number"
        class="form-control"
        placeholder="Crop Mgt ID 입력"
      />
      <button class="btn btn-success" @click="addCartItem">장바구니 담기</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// src/views/CartView.vue (script setup)
import { ref, onMounted, nextTick, computed } from "vue";
import { useRouter } from "vue-router";
import { useCartStore } from "../../store/test/cartStore";
import { useUserStore } from "../../store/users/useUserStore";
import { useOrderStore } from "../../store/test/orderStore";
import { usePaymentStore } from "../../store/test/paymentStore";
import PortOne from "@portone/browser-sdk/v2";

const router = useRouter();
const userStore = useUserStore();
const cartStore = useCartStore();
const orderStore = useOrderStore();
const paymentStore = usePaymentStore();

// 반응형 변수들
const cropMgtId = ref(null);
const selectedItems = ref([]);
const selectAll = ref(false);
const confirmPayment = ref(false);
const showAdminControls = ref(false); // 개발/테스트용

onMounted(async () => {
  userStore.checkLogin();
  await nextTick();
  
  // 장바구니 자동 로드
  await loadCarts();
});

// 계산된 속성들
const selectedItemsCount = computed(() => {
  return cartStore.carts
    .filter(cart => selectedItems.value.includes(cart.id))
    .reduce((total, cart) => total + cart.quantity, 0);
});

const selectedItemsTotal = computed(() => {
  return cartStore.carts
    .filter(cart => selectedItems.value.includes(cart.id))
    .reduce((total, cart) => total + cart.totalPrice, 0);
});

// 함수들
const loadCarts = async () => {
  if (!userStore.user.id) {
    console.log("로그인이 필요합니다.");
    return;
  }
  try {
  await cartStore.fetchCarts(Number(userStore.user.id));
    console.log("장바구니 로드 완료:", cartStore.carts);
  } catch (error) {
    console.error("장바구니 로드 실패:", error);
  }
};

const toggleSelectAll = async () => {
  selectAll.value = !selectAll.value;
  if (selectAll.value) {
    selectedItems.value = cartStore.carts.map(cart => cart.id);
    
    // 전체 선택 시 모든 상품으로 임시주문 생성
    try {
      await orderStore.createOrder(selectedItems.value);
      console.log("전체 상품 임시주문 생성 완료:", selectedItems.value);
    } catch (error) {
      console.error("전체 임시주문 생성 실패:", error);
      // 실패 시 선택 상태 되돌리기
      selectAll.value = false;
      selectedItems.value = [];
      alert("임시주문 생성에 실패했습니다.");
    }
  } else {
    selectedItems.value = [];
  }
};

const toggleCartItem = async (cartId) => {
  const index = selectedItems.value.indexOf(cartId);
  if (index > -1) {
    selectedItems.value.splice(index, 1);
  } else {
    selectedItems.value.push(cartId);
    
    // 체크박스 선택 시 임시주문 생성 (OrderView의 createOrder와 동일)
    try {
      await orderStore.createOrder([cartId]);
      console.log(`상품 ${cartId} 임시주문 생성 완료`);
    } catch (error) {
      console.error("임시주문 생성 실패:", error);
      // 실패 시 선택 상태 되돌리기
      selectedItems.value.splice(selectedItems.value.indexOf(cartId), 1);
      alert("임시주문 생성에 실패했습니다.");
    }
  }
  
  // 전체 선택 상태 업데이트
  selectAll.value = selectedItems.value.length === cartStore.carts.length;
};

const addCartItem = async () => {
  if (!userStore.user.id) {
    alert("로그인이 필요합니다.");
    return;
  }
  if (!cropMgtId.value) {
    alert("Crop Mgt ID를 입력하세요.");
    return;
  }
  const cartData = {
    userId: Number(userStore.user.id),
    quantity: 1,
  };
  try {
  await cartStore.addToCart(Number(cropMgtId.value), cartData);
  cropMgtId.value = null;
    alert("장바구니에 추가되었습니다.");
  } catch (error) {
    console.error("장바구니 추가 실패:", error);
    alert("장바구니 추가에 실패했습니다.");
  }
};

const increaseQuantity = async (cart) => {
  // 수량 증가 로직 (실제 API 호출 필요)
  cart.quantity += 1;
  cart.totalPrice = getUnitPrice(cart) * cart.quantity;
  console.log(`${cart.cropType} 수량 증가:`, cart.quantity);
};

const decreaseQuantity = async (cart) => {
  if (cart.quantity > 1) {
    cart.quantity -= 1;
    cart.totalPrice = getUnitPrice(cart) * cart.quantity;
    console.log(`${cart.cropType} 수량 감소:`, cart.quantity);
  }
};

const removeCartItem = async (cart) => {
  if (confirm(`${getProductName(cart.cropType)}을(를) 장바구니에서 삭제하시겠습니까?`)) {
    // 실제 삭제 API 호출 필요
    const index = cartStore.carts.findIndex(item => item.id === cart.id);
    if (index > -1) {
      cartStore.carts.splice(index, 1);
      selectedItems.value = selectedItems.value.filter(id => id !== cart.id);
      console.log("장바구니 아이템 삭제:", cart);
    }
  }
};

const continueShopping = () => {
  router.push('/farmlist');
};

// PaymentView의 randomId 함수
const randomId = () => {
  return [...crypto.getRandomValues(new Uint32Array(2))]
    .map((word) => word.toString(16).padStart(8, "0"))
    .join("");
};

// 체크된 항목만 결제하시겠습니까 체크박스 처리
const handlePaymentConfirmation = async () => {
  if (selectedItems.value.length === 0) {
    alert("결제할 상품을 먼저 선택해주세요.");
    return;
  }
  
  // 체크박스가 체크되지 않은 상태에서 클릭한 경우에만 주문 확정 진행
  if (!confirmPayment.value) {
    try {
      // 주문 확정 API 호출 (OrderView의 confirmOrder와 동일)
      const orderData = {
        address: '서울시 강남구', // 실제로는 사용자 주소 정보 사용
        receiver: '홍길동', // 실제로는 사용자 이름 사용
        cartIds: selectedItems.value // 선택된 장바구니 아이템들
      };
      
      await orderStore.confirmOrder(orderData);
      console.log("주문 확정 완료:", selectedItems.value);
      
      // 체크박스 상태 변경
      confirmPayment.value = true;
      
    } catch (error) {
      console.error("주문 확정 실패:", error);
      alert("주문 확정에 실패했습니다. 다시 시도해주세요.");
    }
  } else {
    // 이미 체크된 상태에서 클릭한 경우 체크 해제
    confirmPayment.value = false;
  }
};

const proceedToOrder = async () => {
  if (selectedItems.value.length === 0) {
    alert("주문할 상품을 선택해주세요.");
    return;
  }
  
  if (!confirmPayment.value) {
    alert("먼저 '체크된 항목만 결제하시겠습니까?' 체크박스를 클릭하여 주문을 확정해주세요.");
    return;
  }
  
  // PaymentView의 doPayment와 완전히 동일한 처리
  try {
    const paymentId = randomId();

    const payment = await PortOne.requestPayment({
      storeId: "store-3513dda2-1134-48b8-bc99-e3d1487021d7", // PaymentView와 동일
      channelKey: "channel-key-68a00971-dcc7-4450-8470-28f4a47b2018", // PaymentView와 동일
      paymentId, // PaymentView와 동일
      orderName: "딸기", // PaymentView와 동일
      totalAmount: 10000, // PaymentView와 동일
      currency: "KRW", // PaymentView와 동일
      payMethod: "EASY_PAY", // PaymentView와 동일
      customData: {
        productIdxList: [1] // PaymentView와 동일
      },
    });

    // PaymentView에서는 결제 성공 후 바로 검증 호출
    if (payment && payment.paymentId) {
      console.log("결제 성공! 검증 시작:", {
        paymentId: payment.paymentId,
        status: payment.status,
        payment: payment
      });
      
      // PaymentView와 동일하게 await 없이 호출
      paymentStore.validatePayment({ paymentId: payment.paymentId, orderId: 1 });
      
      console.log("결제 검증 호출 완료");
      
      // 결제 성공으로 간주하고 처리 (PaymentView와 동일한 방식)
      alert("결제가 성공적으로 완료되었습니다!");
      
      // 장바구니 비우기
      selectedItems.value = [];
      selectAll.value = false;
      confirmPayment.value = false;
      
      // 장바구니 새로고침
      await loadCarts();
      
      // 주문 완료 페이지로 이동
      router.push('/order-cart-complete');
    } else {
      console.error("결제 응답이 올바르지 않습니다:", payment);
      alert("결제 응답이 올바르지 않습니다.");
    }
    
  } catch (error) {
    console.error("결제 처리 실패:", error);
    alert("결제 처리 중 오류가 발생했습니다: " + error.message);
  }
};

// 유틸리티 함수들
const getProductName = (cropType) => {
  const productNames = {
    'TOMATO': '신선한 토마토',
    'PEPPER': '달콤한 딸기',
    'PAPRIKA': '신선한 파프리카',
    'STRAWBERRY': '달콤한 딸기'
  };
  return productNames[cropType] || cropType || '신선한 농산물';
};

const getFarmName = (cropType) => {
  const farmNames = {
    'TOMATO': '김씨 농장',
    'PEPPER': '이씨 농장', 
    'PAPRIKA': '박씨 농장',
    'STRAWBERRY': '최씨 농장'
  };
  return farmNames[cropType] || '농장';
};

const getUnitPrice = (cart) => {
  return Math.round(cart.totalPrice / cart.quantity);
};

</script>

<style scoped>
/* 완전 커스텀 체크박스 스타일 */
.custom-checkbox {
  display: flex;
  align-items: center;
  cursor: pointer;
  user-select: none;
}

.checkbox-box {
  width: 20px;
  height: 20px;
  border: 2px solid #dee2e6;
  border-radius: 4px;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  position: relative;
}

/* 테이블 안의 큰 체크박스 */
.checkbox-large {
  width: 24px;
  height: 24px;
  border: 2px solid #dee2e6;
  border-radius: 5px;
}

.checkbox-box.checked {
  background-color: #28a745;
  border-color: #28a745;
}

.checkbox-box i {
  color: white;
  font-size: 12px;
  font-weight: bold;
}

.checkbox-large i {
  font-size: 14px;
  font-weight: bold;
}

.checkbox-box:hover {
  border-color: #28a745;
  box-shadow: 0 0 0 0.1rem rgba(40, 167, 69, 0.25);
}

.checkbox-label {
  color: #495057;
  font-size: 14px;
  cursor: pointer;
}

/* 숨겨진 실제 체크박스는 완전히 숨김 */
input[type="checkbox"] {
  display: none !important;
  visibility: hidden !important;
  opacity: 0 !important;
  position: absolute !important;
  left: -9999px !important;
}
</style>
