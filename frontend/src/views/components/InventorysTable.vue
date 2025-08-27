<script setup>
import Modal from '@/views/components/Modal.vue'
import { defineProps, ref } from 'vue'

const props = defineProps(['inventories'])

//선택된 재고 ID
const selectedInventoryId = ref(null)

//편집 버튼 눌렀을때 실행할 함수
const inventory_edit = (inventoryId) => {
  selectedInventoryId.value = inventoryId
}

//판매 상태 값 매핑
const getSaleStatus = (saleStatus) => {
  switch (saleStatus) {
    case 'NOT_AVAILABLE':
      return '재고 없음'
    case 'AVAILABLE':
      return '판매중'
    case 'SOLD_OUT':
      return '판매완료'
    case 'DISCONTINUED':
      return '폐기'
    default:
      return ''
  }
}
</script>

<template>
  <div class="mt-4 row">
    <div class="col-12">
      <!-- 재고 상세 페이지 및 수정 페이지 -->
      <modal :inventoryId="selectedInventoryId" />

      <!-- 재고 관리 테이블 컴포넌트-->
      <div class="card">
        <div class="card-header pb-0">
          <h6>재고 관리</h6>
        </div>

        <div class="card-body px-0 pt-0 pb-2">
          <div class="table-responsive p-0">
            <table class="table align-items-center mb-0">
              <thead>
                <tr>
                  <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">재고 ID</th>
                  <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">작물 종류</th>
                  <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">작물 상태</th>
                  <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">총 주문 요청량</th>
                  <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">판매 상태</th>
                  <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">예측 산출량</th>
                  <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">최대 산출량</th>
                  <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">예측 수확일</th>
                  <th class="text-secondary opacity-7"></th>
                </tr>
              </thead>
              <tbody v-if="props.inventories">
                <!-- <tr v-for="inventory in props.inventories" :key="inventory.id" :class="{ 'table-secondary': inventory.saleStatus === 'SOLD_OUT' }">  -->
                <tr v-for="inventory in props.inventories" :key="inventory.id">
                  <td>
                    <div class="d-flex px-2 py-1">
                      <div class="d-flex flex-column justify-content-center">
                        <p class="text-xs font-weight-bold mb-0">{{ inventory.id }}</p>
                      </div>
                    </div>
                  </td>
                  <td>
                    <p class="text-xs font-weight-bold mb-0">{{ inventory.type }}</p>
                  </td>
                  <td class="text-sm">
                    <span
                      :class="{
                        'badge badge-sm bg-gradient-secondary-green': inventory.status === '양호',
                        'badge badge-sm bg-gradient-secondary-yellow': inventory.status === '보통',
                        'badge badge-sm bg-gradient-secondary-red': inventory.status === '불량',
                      }"
                      >{{ inventory.status }}</span
                    >
                  </td>
                  <td>
                    <p class="text-xs font-weight-bold mb-0">{{ inventory.orderQuantity }} /㎡</p>
                  </td>
                  <td>
                    <p class="text-xs font-weight-bold mb-0">{{ getSaleStatus(inventory.saleStatus) }}</p>
                  </td>
                  <td class="align-middle text-center text-xs font-weight-bold">
                    <span>{{ inventory.expectedQuantity }} /㎡</span>
                  </td>
                  <td class="align-middle text-center text-xs font-weight-bold">
                    <span>{{ inventory.maxExpectedQuantity }} /㎡</span>
                  </td>
                  <td class="align-middle text-center">
                    <span class="text-secondary text-xs font-weight-bold">{{ inventory.expectedHarvestDate }}</span>
                  </td>
                  <td class="align-middle">
                    <a href="javascript:;" class="text-secondary font-weight-bold text-xs" data-toggle="tooltip" data-original-title="Edit user" data-bs-toggle="modal" data-bs-target="#exampleModal">
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-square edit_btn" viewBox="0 0 16 16" @click="inventory_edit(inventory.id)">
                        <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z" />
                        <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z" />
                      </svg>
                    </a>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scope>
.bg-gradient-secondary-red {
  background-image: linear-gradient(310deg, #ff0000 0%, #ffa07a 100%);
}

.bg-gradient-secondary-green {
  background-image: linear-gradient(310deg, #228b22 0%, #32cd32 100%);
}

.bg-gradient-secondary-pink {
  background-image: linear-gradient(310deg, #ff69b4 0%, #ffc0cb 100%);
}

.bg-gradient-secondary-yellow {
  background-image: linear-gradient(310deg, #ffd700 0%, #ffff00 100%);
}
</style>
