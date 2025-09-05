<script setup>
import { ref, onMounted } from 'vue';
// import api from '@/api/order/userlist.js'; // 백엔드 연결 주석 처리
import ArgonBadge from '@/components/ArgonBadge.vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const farmerData = ref([]);
const pageNumber = ref(1);
// const pageSize = ref(10); // 사용하지 않으므로 주석 처리
const totalPages = ref(1);

const statusMap = { PREPARING:'상품준비중', SHIPPED:'배송중', DELIVERED:'배송완료', CANCELED:'환불', EXCHANGED:'교환' };
const getBadgeColor = (s) => ({DELIVERED:'info',CANCELED:'danger',SHIPPED:'primary',EXCHANGED:'warning',PREPARING:'success'}[s] ?? 'dark');

// 상세보기 클릭 처리 함수
const handleDetailClick = (item) => {
  if (item.shippingStatus === 'CANCELED' || item.shippingStatus === 'EXCHANGED') {
    // 환불 또는 교환 상태인 경우 OrderModifyComplete로 이동
    router.push(`/order-modify-complete/${item.id}/${item.shippingStatus}`);
  } else {
    // 기타 상태인 경우 기존 OrderDetail로 이동
    router.push({ name: 'OrderDetail', params: { orderId: item.id } });
  }
};

// 임시 데이터 추가
const mockData = [
  {
    id: 1,
    userId: 101,
    name: '김구매',
    phoneNumber: '010-1234-5678',
    cropName: '신선한 토마토',
    quantity: 5,
    totalPrice: 25000,
    shippingStatus: 'PREPARING',
    orderDate: '2024-09-01T10:30:00'
  },
  {
    id: 2,
    userId: 102,
    name: '이소비자',
    phoneNumber: '010-2345-6789',
    cropName: '달콤한 딸기',
    quantity: 3,
    totalPrice: 24000,
    shippingStatus: 'SHIPPED',
    orderDate: '2024-08-31T14:20:00'
  },
  {
    id: 3,
    userId: 103,
    name: '박고객',
    phoneNumber: '010-3456-7890',
    cropName: '신선한 파프리카',
    quantity: 8,
    totalPrice: 24000,
    shippingStatus: 'DELIVERED',
    orderDate: '2024-08-30T09:15:00'
  },
  {
    id: 4,
    userId: 104,
    name: '최주문자',
    phoneNumber: '010-4567-8901',
    cropName: '유기농 배추',
    quantity: 2,
    totalPrice: 16000,
    shippingStatus: 'CANCELED',
    orderDate: '2024-08-29T16:45:00'
  },
  {
    id: 5,
    userId: 105,
    name: '정고객',
    phoneNumber: '010-5678-9012',
    cropName: '신선한 상추',
    quantity: 4,
    totalPrice: 12000,
    shippingStatus: 'EXCHANGED',
    orderDate: '2024-08-28T11:30:00'
  },
  {
    id: 6,
    userId: 106,
    name: '한소비자',
    phoneNumber: '010-6789-0123',
    cropName: '유기농 당근',
    quantity: 6,
    totalPrice: 18000,
    shippingStatus: 'PREPARING',
    orderDate: '2024-08-27T13:20:00'
  },
  {
    id: 7,
    userId: 107,
    name: '윤구매자',
    phoneNumber: '010-7890-1234',
    cropName: '신선한 양파',
    quantity: 10,
    totalPrice: 20000,
    shippingStatus: 'SHIPPED',
    orderDate: '2024-08-26T15:10:00'
  },
  {
    id: 8,
    userId: 108,
    name: '임고객',
    phoneNumber: '010-8901-2345',
    cropName: '유기농 감자',
    quantity: 7,
    totalPrice: 21000,
    shippingStatus: 'DELIVERED',
    orderDate: '2024-08-25T10:45:00'
  },
  {
    id: 9,
    userId: 109,
    name: '강주문자',
    phoneNumber: '010-9012-3456',
    cropName: '신선한 마늘',
    quantity: 3,
    totalPrice: 9000,
    shippingStatus: 'PREPARING',
    orderDate: '2024-08-24T14:30:00'
  },
  {
    id: 10,
    userId: 110,
    name: '조소비자',
    phoneNumber: '010-0123-4567',
    cropName: '유기농 양배추',
    quantity: 5,
    totalPrice: 25000,
    shippingStatus: 'SHIPPED',
    orderDate: '2024-08-23T09:20:00'
  }
];

async function load(page = 1) {
  pageNumber.value = page;
  // 백엔드 API 호출 주석 처리
  // const res = await api.farmerList({ page, size: pageSize.value });
  // const d = res.data;
  // farmerData.value = d.content;
  // totalPages.value = d.totalPages;
  
  // 임시 데이터 사용
  farmerData.value = mockData;
  totalPages.value = 1;
}

function prev() { if (pageNumber.value > 1) load(pageNumber.value - 1); }
function next() { if (pageNumber.value < totalPages.value) load(pageNumber.value + 1); }
function goto(p) { if (p !== pageNumber.value) load(p); }

function pageRange() {
  const range = [];
  const maxVisible = 5;
  let start = Math.max(1, pageNumber.value - Math.floor(maxVisible / 2));
  let end = Math.min(totalPages.value, start + maxVisible - 1);
  if (end - start < maxVisible - 1) start = Math.max(1, end - maxVisible + 1);
  for (let i = start; i <= end; i++) range.push(i);
  return range;
}

onMounted(() => load(1));
</script>

<template>
  <div class="card mb-4">
    <div class="card-header pb-0">
      <div class="d-flex align-items-center justify-content-between">
        <h6 class="mb-0 me-3">작물 구매자 목록</h6>
      </div>

      <div class="card-body px-5 pt-0 pb-2">
        <div class="table-responsive p-0">
          <table class="table align-items-center mb-0">
            <thead>
              <tr>
                <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">구매자 / 전화번호</th>
                <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-5 ps-2">작물 이름</th>
                <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-5 ps-2">주문량</th>
                <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-5 ps-2">금액</th>
                <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-3 ps-2">주문 상태</th>
                <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-3 ps-2">주문 날짜</th>
                <th class="text-secondary opacity-7 pe-4 ps-2"></th>
              </tr>
            </thead>

            <tbody>
              <tr v-for="(item, idx) in farmerData" :key="item.id ?? idx">
                <td>
                  <div class="d-flex px-2 py-1">
                    <img :src="`https://api.dicebear.com/8.x/pixel-art/svg?seed=${item.userId ?? idx}`"
                        class="avatar avatar-sm me-3" alt="buyer" />
                    <div class="d-flex flex-column justify-content-center">
                      <!-- ★ DTO 필드에 맞추기 -->
                      <h6 class="mb-0 text-sm">{{ item.name }}</h6>
                      <p class="text-xs text-secondary mb-0">{{ item.phoneNumber }}</p>
                    </div>
                  </div>
                </td>

                <td><p class="text-xs font-weight-bold mb-0">{{ item.cropName }}</p></td>
                <td><p class="text-xs font-weight-bold mb-0">{{ item.quantity }}개</p></td>
                <td><p class="text-xs font-weight-bold mb-0">{{ Number(item.totalPrice).toLocaleString() }}원</p></td>

                <td>
                  <ArgonBadge variant="gradient" :color="getBadgeColor(item.shippingStatus)">
                    {{ statusMap[item.shippingStatus] }}
                  </ArgonBadge>
                </td>

                <td>
                  <p class="text-xs font-weight-bold mb-0">
                    {{ new Date(item.orderDate).toLocaleDateString('ko-KR', { year: 'numeric', month: 'long', day: 'numeric' }) }}
                  </p>
                </td>

                <td class="align-middle text-center text-sm">
                  <button
                    @click="handleDetailClick(item)"
                    class="badge text-xs badge-sm bg-gradient-success text-white"
                    style="text-decoration: none; border: none; cursor: pointer;"
                  >상세보기</button>
                </td>
              </tr>

              <tr v-if="farmerData.length === 0">
                <td colspan="7" class="text-center text-secondary py-4">표시할 주문이 없습니다.</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 페이지네이션 (BuyerList와 동일 스타일) -->
        <div class="d-flex justify-content-center mt-3">
          <button class="btn btn-sm btn-outline-primary me-2" @click="prev" :disabled="pageNumber===1">이전</button>
          <button v-for="p in pageRange()" :key="p"
                  class="btn btn-sm"
                  :class="p===pageNumber ? 'btn-primary' : 'btn-outline-primary'"
                  @click="goto(p)">
            {{ p }}
          </button>
          <button class="btn btn-sm btn-outline-primary ms-2" @click="next" :disabled="pageNumber===totalPages">다음</button>
        </div>
      </div>
    </div>
  </div>
</template>
