<script setup>
import { ref, onMounted } from 'vue';
import api from '@/api/order/userlist.js';
import ArgonBadge from '@/components/ArgonBadge.vue';

const farmerData = ref([]);
const pageNumber = ref(1);
const pageSize = ref(10);
const totalPages = ref(1);

const statusMap = { PREPARING:'상품준비중', SHIPPED:'배송중', DELIVERED:'배송완료', CANCELED:'환불', EXCHANGED:'교환' };
const getBadgeColor = (s) => ({DELIVERED:'info',CANCELED:'danger',SHIPPED:'primary',EXCHANGED:'warning',PREPARING:'success'}[s] ?? 'dark');

async function load(page = 1) {
  pageNumber.value = page;
  const res = await api.farmerList({ page, size: pageSize.value });
  const d = res.data;
  farmerData.value = d.content;
  totalPages.value = d.totalPages;
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
                  <router-link
                    :to="{ name: 'OrderDetail', params: { orderId: item.id } }"
                    class="badge text-xs badge-sm bg-gradient-success text-white"
                    style="text-decoration: none;"
                  >상세보기</router-link>
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
