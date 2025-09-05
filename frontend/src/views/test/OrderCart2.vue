<template>
  <div class="container-fluid py-4">
    <div class="row justify-content-center">
      <div class="col-lg-8">
        <!-- 로딩 -->
        <div v-if="loading" class="card shadow-sm">
          <div class="card-body p-4">
            <h5 class="mb-2">주문 확정 중...</h5>
            <p class="text-muted mb-0">잠시만 기다려주세요.</p>
          </div>
        </div>

        <!-- 에러 -->
        <div v-else-if="error" class="card border-danger shadow-sm">
          <div class="card-body p-4">
            <h5 class="text-danger mb-2">주문 확정 실패</h5>
            <p class="mb-3">{{ errorMessage }}</p>

            <div class="d-flex gap-2">
              <argon-button color="secondary" @click="goBack">뒤로가기</argon-button>
              <argon-button color="primary" @click="retry">다시 시도</argon-button>
            </div>

            <hr class="my-4" />
            <details>
              <summary class="text-muted">디버그 정보</summary>
              <pre class="small bg-light p-3 rounded mt-2">{{ debugInfo }}</pre>
            </details>
          </div>
        </div>

        <!-- 성공 -->
        <div v-else class="card shadow-sm">
          <div class="card-body p-4">
            <div class="d-flex align-items-center justify-content-between">
              <h4 class="mb-0">주문이 완료되었습니다! 🎉</h4>
              <span class="badge text-bg-success" v-if="response.paymentStatus">{{ paymentStatusLabel }}</span>
            </div>
            <p class="text-muted mt-2 mb-4">주문 상세를 확인하세요.</p>

            <div class="row g-3">
              <div class="col-md-6">
                <div class="p-3 border rounded-3 h-100">
                  <h6 class="text-uppercase text-muted small mb-3">주문 정보</h6>
                    <ul class="list-unstyled mb-0">
                    <li class="mb-2"><strong>주문번호</strong> : {{ response.orderId || 'ORD-2024-001' }}</li>
                    <li class="mb-2"><strong>거래번호</strong> : {{ response.merchantUid || 'TXN-2024-001' }}</li>
                    <li class="mb-2"><strong>주문일시</strong> : {{ fmtDate(response.orderDay) }}</li>
                    <li class="mb-2"><strong>결제수단</strong> : {{ payMethodLabel }}</li>
                    <li class="mb-0"><strong>결제상태</strong> : {{ paymentStatusLabel }}</li>
                    </ul>
                </div>
              </div>

              <div class="col-md-6">
                <div class="p-3 border rounded-3 h-100">
                  <h6 class="text-uppercase text-muted small mb-3">수취/배송 정보</h6>
                    <ul class="list-unstyled mb-0">
                    <li class="mb-2"><strong>수취인</strong> : {{ response.ordererName || '홍길동' }}</li>
                    <li class="mb-2"><strong>연락처</strong> : {{ response.phoneNumber || '010-1234-5678' }}</li>
                    <li class="mb-2"><strong>우편번호</strong> : {{ response.postCode || '12345' }}</li>
                    <li class="mb-2"><strong>주소</strong> : {{ response.address || '서울시 강남구' }}</li>
                    <li class="mb-0"><strong>상세주소</strong> : {{ response.detailAddress || '역삼동 123-45' }}</li>
                    </ul>
                </div>
              </div>

              <div class="col-12">
                <div class="p-3 border rounded-3">
                  <h6 class="text-uppercase text-muted small mb-3">주문 상품 목록</h6>
                  <div class="table-responsive">
                    <table class="table table-sm">
                      <thead>
                        <tr>
                          <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">상품명</th>
                          <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">농장</th>
                          <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">수량</th>
                          <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">단가</th>
                          <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">금액</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="item in mockOrderItems" :key="item.id">
                          <td>
                            <span class="text-xs font-weight-bold">{{ item.name }}</span>
                          </td>
                          <td>
                            <span class="text-xs text-secondary">{{ item.farm }}</span>
                          </td>
                          <td>
                            <span class="text-xs font-weight-bold">{{ item.quantity }}{{ item.unit }}</span>
                          </td>
                          <td>
                            <span class="text-xs text-secondary">{{ item.price.toLocaleString() }}원</span>
                          </td>
                          <td>
                            <span class="text-xs font-weight-bold">{{ (item.price * item.quantity).toLocaleString() }}원</span>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>

              <div class="col-12">
                <div class="p-3 border rounded-3">
                  <h6 class="text-uppercase text-muted small mb-3">결제 금액</h6>
                    <div class="d-flex align-items-center justify-content-between">
                    <span class="fw-semibold">총 결제금액</span>
                    <span class="fs-5 fw-bold text-success">{{ fmtCurrency(response.totalPrice) }}</span>
                  </div>
                </div>
              </div>
            </div>

            <div class="d-flex gap-2 mt-4">
              <argon-button color="primary" @click="goBuyerOrders">내 주문 목록으로</argon-button>
              <argon-button color="secondary" @click="goHome">홈으로</argon-button>
            </div>

            <p class="text-muted small mt-3 mb-0">
              * 품목 상세는 "내 주문 상세" 화면에서 확인할 수 있어요.
            </p>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
// import axios from 'axios'

const router = useRouter()
const mockOrderData = {
  orderId: 'ORD-2024-1234',
  merchantUid: 'TXN-2024-5678',
  ordererName: '김구매',
  phoneNumber: '010-9876-5432',
  postCode: '06123',
  address: '서울특별시 강남구',
  detailAddress: '테헤란로 123, 45층',
  payMethod: 'CARD',
  totalPrice: 45000,
  orderDay: new Date('2024-09-02T14:30:00'),
  paymentStatus: 'PAID'
};

// 임시 주문 상품 데이터
const mockOrderItems = [
  {
    id: 1,
    name: '신선한 토마토',
    farm: '김씨 농장',
    price: 8000,
    quantity: 2,
    unit: 'kg'
  },
  {
    id: 2,
    name: '달콤한 딸기',
    farm: '이씨 농장',
    price: 12000,
    quantity: 1,
    unit: '팩'
  },
  {
    id: 3,
    name: '신선한 파프리카',
    farm: '박씨 농장',
    price: 6000,
    quantity: 2,
    unit: '개'
  }
];

const loading = ref(true)
const error = ref(false)
const errorMessage = ref('')
const debugInfo = ref('')

const response = reactive({
  orderId: mockOrderData.orderId,
  merchantUid: mockOrderData.merchantUid,
  ordererName: mockOrderData.ordererName,
  address: mockOrderData.address,
  detailAddress: mockOrderData.detailAddress,
  postCode: mockOrderData.postCode,
  phoneNumber: mockOrderData.phoneNumber,
  payMethod: mockOrderData.payMethod,
  totalPrice: mockOrderData.totalPrice,
  orderDay: mockOrderData.orderDay,
  paymentStatus: mockOrderData.paymentStatus,
});

// 라벨 매핑
const payMethodLabel = computed(() => {
  const map = {
    CARD: '카드',
    BANK_TRANSFER: '계좌이체',
    VIRTUAL_ACCOUNT: '가상계좌',
    MOBILE: '휴대폰결제',
    KAKAO_PAY: '카카오페이',
    NAVER_PAY: '네이버페이',
  }
  return map[response.payMethod] || response.payMethod || '카드'
})

const paymentStatusLabel = computed(() => {
  const map = {
    READY: '결제대기',
    PAID: '결제완료',
    CANCELLED: '결제취소',
    FAILED: '결제실패',
    REFUND: '환불',
  }
  return map[response.paymentStatus] || response.paymentStatus || '결제완료'
})

// 날짜/통화 포맷
function fmtDate(dt) {
  if (!dt) return new Date().toLocaleString('ko-KR')
  try {
    const d = new Date(dt)
    if (Number.isNaN(d.getTime())) return dt
    return (
      `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ` +
      `${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
    )
  } catch (e) {
    return dt
  }
}

function fmtCurrency(n) {
  if (n == null) return '16,000 ₩'
  return Number(n).toLocaleString('ko-KR') + ' ₩'
}

// 이전 페이지에서 state로 받은 OrderDto.Request
// function getConfirmRequest() {
//   // 1순위: router.push({ state: { confirmRequest } })로 전달된 값
//   const stateReq = route && 'state' in route ? route.state?.confirmRequest : undefined
//   if (stateReq) return stateReq

//   // 2순위: 브라우저 history.state (일부 환경에서 route.state가 노출되지 않을 수 있음)
//   const historyReq = typeof window !== 'undefined' ? window.history.state?.confirmRequest : undefined
//   if (historyReq) return historyReq

//   // 3순위: localStorage(직전 단계에서 저장해둔 경우)
//   const raw = typeof window !== 'undefined' ? localStorage.getItem('orderConfirmRequest') : null
//   if (raw) {
//     try { 
//       return JSON.parse(raw) 
//     } catch (e) {
//       console.error('Failed to parse localStorage data:', e)
//     }
//   }

//   // 4순위: 예비 폼 (가능하면 실제 값으로 대체)
//   return {
//     postCode: '12345',
//     address: '서울시 강남구',
//     detailAddress: '역삼동 123-45',
//     ordererName: '홍길동',
//     phoneNumber: '010-1234-5678',
//     payMethod: 'CARD',
//   }
// }

async function runConfirm() {
  loading.value = true
  error.value = false
  errorMessage.value = ''
  debugInfo.value = ''

  // 임시로 백엔드 API 호출을 주석 처리하고 임시 데이터 사용
  // const payload = getConfirmRequest()

  // try {
  //   // ② 직접 axios 호출 (동일 세션 유지)
  //   const { data } = await axios.post(
  //     'http://localhost:8080/order/confirm',
  //     payload,
  //     { withCredentials: true },
  //   )

  //   // 서버가 BaseResponse<{...}> 형태인지, 바로 DTO인지 모두 대응
  //   const body = data?.data ?? data

  //   // DTO 필드 매핑 (널 병합으로 안전하게)
  //   response.orderId       = body.orderId ?? 'ORD-2024-001'
  //   response.merchantUid   = body.merchantUid ?? 'TXN-2024-001'
  //   response.ordererName   = body.ordererName ?? '홍길동'
  //   response.address       = body.address ?? '서울시 강남구'
  //   response.detailAddress = body.detailAddress ?? '역삼동 123-45'
  //   response.postCode      = body.postCode ?? '12345'
  //   response.phoneNumber   = body.phoneNumber ?? '010-1234-5678'
  //   response.payMethod     = body.payMethod ?? 'CARD'
  //   response.totalPrice    = body.totalPrice ?? 16000
  //   response.orderDay      = body.orderDay ?? new Date()
  //   response.paymentStatus = body.paymentStatus ?? 'PAID'
  // } catch (e) {
  //   error.value = true
  //   errorMessage.value =
  //     e?.response?.data?.message || e?.message || '알 수 없는 오류가 발생했습니다.'
  //   debugInfo.value = JSON.stringify(
  //     {
  //       status: e?.response?.status,
  //       data: e?.response?.data,
  //       payload,
  //     },
  //     null,
  //     2,
  //   )
  // } finally {
  //   loading.value = false
  // }

  // 임시 데이터 사용
  setTimeout(() => {
    loading.value = false
  }, 1000)
}

// 버튼 액션
function goBuyerOrders() {
  router.push('/buyerlist')
}

function goHome() {
  router.push('/buyer/dashboard')
}

function goBack() {
  router.back()
}

function retry() {
  runConfirm()
}

onMounted(() => {
  runConfirm()
})
</script>

<style scoped>
.card {
  border-radius: 1rem; 
  border: 0;
  box-shadow: 0 0 2rem 0 rgba(136, 152, 170, 0.15);
}

.card-body {
  padding: 1.5rem;
}

.border {
  border: 1px solid #e9ecef !important;
}

.rounded-3 {
  border-radius: 0.5rem !important;
}

.text-success {
  color: #2dce89 !important;
}

.badge.text-bg-success {
  background-color: #2dce89 !important;
  color: white !important;
}
</style>
