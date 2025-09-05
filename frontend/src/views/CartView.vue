<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// 장바구니 상품 데이터 (실제로는 store나 API에서 가져올 예정)
const cartItems = ref([
  {
    id: 1,
    name: '신선한 토마토',
    farm: '김씨 농장',
    price: 5000,
    quantity: 2,
    unit: 'kg',
    selected: true
  },
  {
    id: 2,
    name: '달콤한 딸기',
    farm: '이씨 농장',
    price: 8000,
    quantity: 1,
    unit: '팩',
    selected: true
  },
  {
    id: 3,
    name: '신선한 파프리카',
    farm: '박씨 농장',
    price: 3000,
    quantity: 3,
    unit: '개',
    selected: true
  }
]);

// 전체 선택/해제 상태
const selectAll = ref(true);

// 결제 확인 상태
const paymentConfirmed = ref(false);

// 전체 선택/해제 토글

// 전체 선택/해제 토글
const toggleSelectAll = () => {
  selectAll.value = !selectAll.value;
  cartItems.value.forEach(item => {
    item.selected = selectAll.value;
  });
};

// 개별 상품 선택/해제
const toggleItemSelection = (itemId) => {
  const item = cartItems.value.find(item => item.id === itemId);
  if (item) {
    item.selected = !item.selected;
    // 전체 선택 상태 업데이트
    selectAll.value = cartItems.value.every(item => item.selected);
  }
};

// 수량 변경 함수
const updateQuantity = (itemId, newQuantity) => {
  if (newQuantity <= 0) {
    cartItems.value = cartItems.value.filter(item => item.id !== itemId);
  } else {
    const item = cartItems.value.find(item => item.id === itemId);
    if (item) {
      item.quantity = newQuantity;
    }
  }
};

// 상품 삭제 함수
const removeItem = (itemId) => {
  cartItems.value = cartItems.value.filter(item => item.id !== itemId);
};

// 선택된 상품만 필터링
const selectedItems = computed(() => {
  return cartItems.value.filter(item => item.selected);
});

// 선택된 상품의 총 금액 계산
const selectedTotalAmount = computed(() => {
  return selectedItems.value.reduce((total, item) => {
    return total + (item.price * item.quantity);
  }, 0);
});

// 선택된 상품의 총 개수
const selectedTotalItems = computed(() => {
  return selectedItems.value.reduce((total, item) => {
    return total + item.quantity;
  }, 0);
});

// 결제하기 함수
const proceedToPayment = () => {
  if (selectedItems.value.length > 0 && paymentConfirmed.value) {
    // 선택된 상품만 결제 완료 페이지로 이동
    router.push('/order-cart-complete');
  } else if (!paymentConfirmed.value) {
    alert('체크된 항목만 결제하시겠습니까? 체크박스를 확인해주세요.');
  }
};

// 쇼핑 계속하기
const continueShopping = () => {
  router.push('/farmlist');
};
</script>

<template>
  <div class="container-fluid py-4">
    <div class="row">
      <div class="col-12">
        <div class="card mb-4">
          <div class="card-header pb-0">
            <div class="row">
              <div class="col-6">
                <h6 class="mb-0">장바구니</h6>
                <p class="text-sm mb-0">
                  총 {{ selectedTotalItems }}개의 상품이 선택되었습니다
                </p>
              </div>
              <div class="col-6 text-end">
                <argon-button
                  color="info"
                  size="sm"
                  @click="continueShopping"
                >
                  쇼핑 계속하기
                </argon-button>
              </div>
            </div>
          </div>
          
          <div class="card-body px-0 pt-0 pb-2">
            <div v-if="cartItems.length === 0" class="text-center py-5">
              <i class="fas fa-shopping-cart fa-3x text-muted mb-3"></i>
              <h5 class="text-muted">장바구니가 비어있습니다</h5>
              <p class="text-muted">상품을 추가해보세요!</p>
              <argon-button
                color="success"
                @click="continueShopping"
              >
                쇼핑하러 가기
              </argon-button>
            </div>
            
            <div v-else>
              <!-- 장바구니 상품 목록 -->
              <div class="table-responsive p-0">
                <table class="table align-items-center mb-0">
                  <thead>
                    <tr>
                      <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7" style="width: 60px;">
                        <div class="form-check d-flex justify-content-center">
                          <input
                            class="form-check-input"
                            type="checkbox"
                            :checked="selectAll"
                            @change="toggleSelectAll"
                            id="selectAll"
                          >
                          <label class="form-check-label ms-1" for="selectAll" style="font-size: 0.7rem;">
                            전체
                          </label>
                        </div>
                      </th>
                      <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">상품</th>
                      <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">농장</th>
                      <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">수량</th>
                      <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">단가</th>
                      <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">금액</th>
                      <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">삭제</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="item in cartItems" :key="item.id">
                      <td class="align-middle text-center" style="width: 60px;">
                        <div class="form-check d-flex justify-content-center">
                          <input
                            class="form-check-input"
                            type="checkbox"
                            :checked="item.selected"
                            @change="toggleItemSelection(item.id)"
                            :id="'item-' + item.id"
                          >
                        </div>
                      </td>
                      <td>
                        <div class="d-flex px-2 py-1">
                          <div class="d-flex flex-column justify-content-center">
                            <h6 class="mb-0 text-sm">{{ item.name }}</h6>
                            <p class="text-xs text-secondary mb-0">{{ item.unit }}</p>
                          </div>
                        </div>
                      </td>
                      <td>
                        <p class="text-xs font-weight-bold mb-0">{{ item.farm }}</p>
                      </td>
                      <td class="align-middle text-center text-sm">
                        <div class="d-flex align-items-center justify-content-center">
                          <argon-button
                            color="secondary"
                            size="sm"
                            @click="updateQuantity(item.id, item.quantity - 1)"
                            class="me-2"
                          >
                            <i class="fas fa-minus"></i>
                          </argon-button>
                          <span class="mx-3">{{ item.quantity }}</span>
                          <argon-button
                            color="secondary"
                            size="sm"
                            @click="updateQuantity(item.id, item.quantity + 1)"
                            class="ms-2"
                          >
                            <i class="fas fa-plus"></i>
                          </argon-button>
                        </div>
                      </td>
                      <td class="align-middle text-center">
                        <span class="text-secondary text-xs font-weight-bold">{{ item.price.toLocaleString() }} ₩</span>
                      </td>
                      <td class="align-middle text-center">
                        <span class="text-secondary text-xs font-weight-bold">{{ (item.price * item.quantity).toLocaleString() }} ₩</span>
                      </td>
                      <td class="align-middle text-center">
                        <argon-button
                          color="danger"
                          size="sm"
                          @click="removeItem(item.id)"
                        >
                          <i class="fas fa-trash"></i>
                        </argon-button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
              
              <!-- 주문 요약 -->
              <div class="row mt-4">
                <div class="col-md-6">
                  <!-- 결제 확인 체크박스 -->
                  <div class="d-flex align-items-center justify-content-center">
                    <div class="form-check payment-confirm-button">
                      <input
                        class="form-check-input"
                        type="checkbox"
                        :checked="paymentConfirmed"
                        @change="paymentConfirmed = !paymentConfirmed"
                        id="paymentConfirm"
                      >
                      <label class="form-check-label" for="paymentConfirm">
                        체크된 항목만 결제하시겠습니까?
                      </label>
                    </div>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="card">
                    <div class="card-body">
                      <h6 class="mb-3">주문 요약</h6>
                      <div class="d-flex justify-content-between mb-2">
                        <span>상품 수량:</span>
                        <span>{{ selectedTotalItems }}개</span>
                      </div>
                      <div class="d-flex justify-content-between mb-2">
                        <span>상품 금액:</span>
                        <span>{{ selectedTotalAmount.toLocaleString() }} ₩</span>
                      </div>
                      <div class="d-flex justify-content-between mb-2">
                        <span>배송비:</span>
                        <span>{{ selectedTotalAmount > 50000 ? '무료' : '3,000 ₩' }}</span>
                      </div>
                      <hr>
                      <div class="d-flex justify-content-between mb-3">
                        <strong>총 결제금액:</strong>
                        <strong class="text-success">{{ (selectedTotalAmount + (selectedTotalAmount > 50000 ? 0 : 3000)).toLocaleString() }} ₩</strong>
                      </div>
                      <div class="mt-4">
                        <argon-button
                          color="success"
                          size="lg"
                          class="w-100 py-3"
                          @click="proceedToPayment"
                          :disabled="selectedItems.length === 0"
                          style="font-size: 1.1rem; font-weight: 600; border-radius: 8px; box-shadow: 0 4px 12px rgba(45, 206, 137, 0.3);"
                        >
                          💳 결제하기
                        </argon-button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.avatar {
  width: 40px;
  height: 40px;
  object-fit: cover;
  border-radius: 8px;
}

.table th {
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.025em;
  color: #6c757d;
  border-bottom: 1px solid #e9ecef;
}

.table td {
  vertical-align: middle;
  border-bottom: 1px solid #e9ecef;
}

.card {
  border: 0;
  box-shadow: 0 0 2rem 0 rgba(136, 152, 170, 0.15);
  border-radius: 0.5rem;
}

.card-header {
  background-color: transparent;
  border-bottom: 1px solid #e9ecef;
  padding: 1.5rem;
}

/* 체크박스 컬럼 스타일링 */
.form-check {
  margin: 0;
  padding: 0;
}

.form-check-input {
  margin: 0 !important;
  cursor: pointer !important;
  width: 20px !important;
  height: 20px !important;
  border: 2px solid #6c757d !important;
  border-radius: 4px !important;
  background-color: white !important;
  position: relative !important;
  appearance: none !important;
  -webkit-appearance: none !important;
  -moz-appearance: none !important;
  transition: all 0.2s ease !important;
}

.form-check-input:checked {
  background-color: #5e72e4 !important;
  border-color: #5e72e4 !important;
  box-shadow: 0 2px 4px rgba(94, 114, 228, 0.3) !important;
}

.form-check-input:checked::after {
  content: '✓' !important;
  position: absolute !important;
  top: 50% !important;
  left: 50% !important;
  transform: translate(-50%, -50%) !important;
  color: white !important;
  font-size: 14px !important;
  font-weight: bold !important;
  line-height: 1 !important;
}

.form-check-input:hover {
  border-color: #5e72e4 !important;
  box-shadow: 0 0 0 2px rgba(94, 114, 228, 0.1) !important;
}

.form-check-label {
  cursor: pointer;
  margin: 0;
  margin-left: 6px;
  font-weight: 500;
}

/* 테이블 첫 번째 컬럼 (체크박스) 최소 너비 설정 */
.table th:first-child,
.table td:first-child {
  width: 60px;
  min-width: 60px;
  max-width: 60px;
  padding: 0.5rem 0.25rem;
}

/* 결제 확인 버튼 스타일링 */
.payment-confirm-button {
  border: 2px solid #5e72e4;
  border-radius: 8px;
  padding: 12px 20px;
  background-color: white;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 2px 4px rgba(94, 114, 228, 0.1);
  min-width: 280px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.payment-confirm-button:hover {
  background-color: #f8f9ff;
  box-shadow: 0 4px 8px rgba(94, 114, 228, 0.2);
  transform: translateY(-1px);
}

.payment-confirm-button:has(.form-check-input:checked) {
  background-color: #5e72e4;
  color: white;
  box-shadow: 0 4px 12px rgba(94, 114, 228, 0.3);
}

.payment-confirm-button:has(.form-check-input:checked) .form-check-label {
  color: white;
  font-weight: 600;
}

.payment-confirm-button .form-check-input {
  margin-right: 6px;
}
</style>
