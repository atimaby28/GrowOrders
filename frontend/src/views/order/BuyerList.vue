<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import api from '../../api/order/userlist.js'; // 위에서 export default { buyerList } 한 모듈
import ArgonBadge from '../../components/ArgonBadge.vue';

const router = useRouter();

const buyerData = ref([]);
const pageNumber = ref(1);      // 화면용 1-based
// const pageSize = ref(10);
const totalPages = ref(1);

// 임시 데이터 (백엔드 데이터가 없을 때 사용)
const mockData = [
  {
    id: 1,
    farmName: '김씨 농장',
    farmOwnerPhone: '010-1234-5678',
    cropName: '토마토',
    quantity: 2,
    totalPrice: 12000,
    shippingStatus: 'PREPARING',
    orderDate: '2024-09-01'
  },
  {
    id: 2,
    farmName: '이씨 농장',
    farmOwnerPhone: '010-2345-6789',
    cropName: '딸기',
    quantity: 1,
    totalPrice: 8000,
    shippingStatus: 'SHIPPED',
    orderDate: '2024-08-30'
  },
  {
    id: 3,
    farmName: '박씨 농장',
    farmOwnerPhone: '010-3456-7890',
    cropName: '파프리카',
    quantity: 3,
    totalPrice: 9000,
    shippingStatus: 'DELIVERED',
    orderDate: '2024-08-29'
  },
  {
    id: 4,
    farmName: '최씨 농장',
    farmOwnerPhone: '010-4567-8901',
    cropName: '오이',
    quantity: 2,
    totalPrice: 5000,
    shippingStatus: 'CANCELED',
    orderDate: '2024-08-28'
  },
  {
    id: 5,
    farmName: '정씨 농장',
    farmOwnerPhone: '010-5678-9012',
    cropName: '양파',
    quantity: 4,
    totalPrice: 7200,
    shippingStatus: 'EXCHANGED',
    orderDate: '2024-08-27'
  },
  {
    id: 6,
    farmName: '한씨 농장',
    farmOwnerPhone: '010-6789-0123',
    cropName: '마늘',
    quantity: 1,
    totalPrice: 3500,
    shippingStatus: 'PREPARING',
    orderDate: '2024-08-26'
  },
  {
    id: 7,
    farmName: '강씨 농장',
    farmOwnerPhone: '010-7890-1234',
    cropName: '당근',
    quantity: 3,
    totalPrice: 6000,
    shippingStatus: 'SHIPPED',
    orderDate: '2024-08-25'
  },
  {
    id: 8,
    farmName: '조씨 농장',
    farmOwnerPhone: '010-8901-2345',
    cropName: '감자',
    quantity: 5,
    totalPrice: 15000,
    shippingStatus: 'DELIVERED',
    orderDate: '2024-08-24'
  },
  {
    id: 9,
    farmName: '윤씨 농장',
    farmOwnerPhone: '010-9012-3456',
    cropName: '고구마',
    quantity: 2,
    totalPrice: 4000,
    shippingStatus: 'PREPARING',
    orderDate: '2024-08-23'
  },
  {
    id: 10,
    farmName: '임씨 농장',
    farmOwnerPhone: '010-0123-4567',
    cropName: '브로콜리',
    quantity: 1,
    totalPrice: 4500,
    shippingStatus: 'SHIPPED',
    orderDate: '2024-08-22'
  }
];

const statusMap = { PREPARING:'상품준비중', SHIPPED:'배송중', DELIVERED:'배송완료', CANCELED:'환불', EXCHANGED:'교환' };
const getBadgeColor = (s) => ({DELIVERED:'info',CANCELED:'danger',SHIPPED:'primary',EXCHANGED:'warning',PREPARING:'success'}[s] ?? 'dark');

// 주문 상태 클릭 처리
function handleStatusClick(item) {
  if (item.shippingStatus === 'PREPARING' || item.shippingStatus === 'SHIPPED' || item.shippingStatus === 'DELIVERED') {
    // 상품준비중, 배송중, 배송완료 상태 - 수정 페이지로 이동
    router.push(`/order-modify/${item.id}`);
  } else if (item.shippingStatus === 'CANCELED' || item.shippingStatus === 'EXCHANGED') {
    // 환불/교환 상태 - 수정 완료 페이지로 이동
    router.push(`/order-modify-complete/${item.id}/${item.shippingStatus}`);
  }
}

async function load(page = 1) {
  // ★ 여기서 보낸 page를 바로 화면 상태에 반영 (서버 응답의 pageNumber가 1로 고정돼 와도 UI는 원하는 페이지를 표시)
  pageNumber.value = page;

  // 임시로 백엔드 API 호출을 주석 처리하고 임시 데이터 사용
  try {
    const d = await api.buyerList({ page, size: 10 });
    buyerData.value = d.content;
    totalPages.value = d.totalPages;
  } catch (error) {
    console.error('백엔드 데이터 로드 실패, 임시 데이터 사용:', error);
    // 백엔드 데이터가 없을 때 임시 데이터 사용
    buyerData.value = [];
    totalPages.value = 1;
  }

  // 임시 데이터 사용
  buyerData.value = mockData;
  totalPages.value = 1;

  // (선택) 서버가 정확한 pageNumber를 주면 그걸로 맞춰도 됨:
  // pageNumber.value = d.pageNumber;
}

function prev() { if (pageNumber.value > 1) load(pageNumber.value - 1); }
function next() { if (pageNumber.value < totalPages.value) load(pageNumber.value + 1); }
function goto(p) { if (p !== pageNumber.value) load(p); }

// 5개씩 페이징 버튼
function pageRange() {
  const range = [];
  const maxVisible = 5;
  let start = Math.max(1, pageNumber.value - Math.floor(maxVisible / 2));
  let end = Math.min(totalPages.value, start + maxVisible - 1);
  if (end - start < maxVisible - 1) start = Math.max(1, end - maxVisible + 1);
  for (let i = start; i <= end; i++) range.push(i);
  return range;
}
function sortTable(e) {
  const by = e?.target?.value;
  const arr = [...buyerData.value];
  if (by === 'year') { // 농장 이름
    arr.sort((a,b) => (a.farmName ?? '').localeCompare(b.farmName ?? ''));
  } else if (by === 'crop') {
    arr.sort((a,b) => (a.cropName ?? '').localeCompare(b.cropName ?? ''));
  } else if (by === 'status') {
    const rank = { PREPARING:1, SHIPPED:2, DELIVERED:3, CANCELED:4, EXCHANGED:5 };
    arr.sort((a,b) => (rank[a.shippingStatus] ?? 99) - (rank[b.shippingStatus] ?? 99));
  }
  buyerData.value = arr;
}

onMounted(() => load(1));
</script>

<template>
  <div class="card mb-4">
    <div class="card-header pb-0">
      <div class="d-flex align-items-center justify-content-between">
        <div class="d-flex align-items-center">
          <h6 class="mb-0 me-3" style="white-space: nowrap;">구매자님의 주문관리 기록</h6>
          <div class="input-group input-group-sm ms-3" style="max-width: 250px;">
            <span class="input-group-text text-body px-2">
              <i class="fas fa-search" aria-hidden="true"></i>
            </span>
            <input type="text" class="form-control" placeholder="Type here...">
          </div>
        </div>
        <label class="position-absolute end-2 top-5 mt-1 me-3 text-xs">
          정렬 기준:
          <select id="sortOption" @change="sortTable"
                  class="form-select form-select-sm d-inline w-auto ms-1"
                  style="width: 160px !important; height: 40px !important; font-size: 14px;">
            <option value="year">농장 이름</option>
            <option value="crop">작물 이름</option>
            <option value="status">주문 상태</option>
          </select>
        </label>
      </div>

      <div class="card-body px-5 pt-0 pb-2">
        <div class="table-responsive p-0">
          <table class="table align-items-center mb-0" id="predictionTable">
            <thead>
              <tr>
                <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">농장 이름 / 전화번호</th>
                <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-5 ps-2">작물 이름</th>
                <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-5 ps-2">주문량</th>
                <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-5 ps-2">금액</th>
                <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-3 ps-2">주문 상태</th>
                <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-3 ps-2">주문 날짜</th>
                <th class="text-secondary opacity-7 pe-4 ps-2"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in buyerData" :key="index">
                <td>
                  <div class="d-flex px-2 py-1">
                    <div class="d-flex flex-column justify-content-center">
                      <img 
                        :src="`https://api.dicebear.com/8.x/pixel-art/svg?seed=${Math.random().toString(36).substring(2, 10)}`" 
                        class="avatar avatar-sm me-3" alt="user" />
                    </div>
                    <div class="d-flex flex-column justify-content-center">
                      <h6 class="mb-0 text-sm">{{ item.farmName }}</h6>
                      <p class="text-xs text-secondary mb-0">{{ item.farmOwnerPhone }}</p>
                    </div>
                  </div>
                </td>
                <td>
                  <p class="text-xs font-weight-bold mb-0">{{ item.cropName }}</p>
                </td>
                <td>
                  <p class="text-xs font-weight-bold mb-0">{{ item.quantity }}Kg</p>
                </td>
                <td>
                  <p class="text-xs font-weight-bold mb-0">{{ item.totalPrice }}원</p>
                </td>
                <td>
                  <argon-badge 
                    variant="gradient" 
                    :color="getBadgeColor(item.shippingStatus)"
                    @click="handleStatusClick(item)"
                    style="cursor: pointer;"
                  >
                    {{ statusMap[item.shippingStatus] }}
                  </argon-badge>
                </td>
                <td>
                  <p class="text-xs font-weight-bold mb-0">
                    {{ new Date(item.orderDate).toLocaleDateString("ko-KR", { year: "numeric", month: "long", day: "numeric" }) }}
                  </p>
                </td>
                <td class="align-middle text-center text-sm">
                  <router-link 
                    :to="{ name: 'OrderDetail', params: { orderId: item.id } }" 
                    class="badge text-xs badge-sm bg-gradient-success text-white" 
                    style="text-decoration: none;">
                    상세보기
                  </router-link>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
  <div class="d-flex justify-content-center mt-3">
    <button class="btn btn-sm btn-outline-primary me-2"
            @click="prev" :disabled="pageNumber===1">이전</button>

    <button v-for="p in pageRange()" :key="p"
            class="btn btn-sm"
            :class="p===pageNumber ? 'btn-primary' : 'btn-outline-primary'"
            @click="goto(p)">
      {{ p }}
    </button>

    <button class="btn btn-sm btn-outline-primary ms-2"
            @click="next" :disabled="pageNumber===totalPages">다음</button>
  </div>
      </div>
    </div>
    <router-view></router-view>
  </div>
</template>
