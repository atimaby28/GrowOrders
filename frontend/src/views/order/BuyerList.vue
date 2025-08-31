<script setup>
import { ref, onMounted } from 'vue';
import api from '../../api/order/userlist.js'; // 위에서 export default { buyerList } 한 모듈
import ArgonBadge from '../../components/ArgonBadge.vue';

const buyerData = ref([]);
const pageNumber = ref(1);      // 화면용 1-based
const pageSize = ref(10);
const totalPages = ref(1);

const statusMap = { PREPARING:'상품준비중', SHIPPED:'배송중', DELIVERED:'배송완료', CANCELED:'환불', EXCHANGED:'교환' };
const getBadgeColor = (s) => ({DELIVERED:'info',CANCELED:'danger',SHIPPED:'primary',EXCHANGED:'warning',PREPARING:'success'}[s] ?? 'dark');

async function load(page = 1) {
  // ★ 여기서 보낸 page를 바로 화면 상태에 반영 (서버 응답의 pageNumber가 1로 고정돼 와도 UI는 원하는 페이지를 표시)
  pageNumber.value = page;

  const res = await api.buyerList({ page, size: pageSize.value }); // 내부에서 page-1 처리됨
  const d = res.data; // 백엔드가 반환한 data 객체

  buyerData.value = d.content;
  totalPages.value = d.totalPages;

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
          <select id="sortOption" onchange="sortTable()" 
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
                  <argon-badge variant="gradient" :color="getBadgeColor(item.shippingStatus)">
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
