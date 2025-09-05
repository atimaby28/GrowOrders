<script setup>
import api from "@/api/inventory";
import Modal from "@/views/components/Modal.vue";
import InventoryHeader from "@/views/components/InventoryHeader.vue";
import { defineEmits, reactive, ref, watch } from "vue";
import { useRoute } from "vue-router";
import { useInventoryStore } from "@/store/inventories/useInventoryStore.js";

const emits = defineEmits(["updateCropList", "getInventoryList"]);
const route = useRoute();
const inventoryStore = useInventoryStore();

//부모의 재고 목록 조회 api 호출 메소드 실행
const getInventoryList = (farmId) => {
  emits("getInventoryList", farmId);
};

//선택된 재고 ID
const selectedInventoryId = ref(null);

//편집 버튼 눌렀을때 실행할 함수
const inventory_edit = (inventoryId) => {
  selectedInventoryId.value = inventoryId;
};

//판매 상태 값 매핑
const getSaleStatus = (saleStatus) => {
  switch (saleStatus) {
    case "NOT_AVAILABLE":
      return "재고 없음";
    case "AVAILABLE":
      return "판매중";
    case "SOLD_OUT":
      return "판매완료";
    case "DISCONTINUED":
      return "폐기";
    default:
      return "";
  }
};

//검색 선택값
const searchForm = reactive({
  type: "",
  status: "",
  saleStatus: "",
  order: "",
});

//검색 변경 감지시 api 호출
watch(searchForm, async () => {
  const data = await api.searchInventory(route.query.farmId, searchForm);

  if (data) {
    if (data.success) {
      emits("updateCropList", data.data);
    } else {
      console.log("데이터가 없습니다.");
    }
  }
});
</script>

<template>
  <div class="mt-4 row">
    <div class="col-12">
      <!-- 재고 상세 페이지 및 수정 페이지 -->
      <modal
        :inventoryId="selectedInventoryId"
        @getInventoryList="getInventoryList"
      />

      <!-- 재고 관리 테이블 컴포넌트-->
      <div class="card">
        <div
          class="form-group card-header pb-0 d-flex align-items-center justify-content-between"
        >
          <h6 class="mb-0">재고 관리</h6>
          <!-- 검색 필터 : 작물 종류 -->
          <div class="d-flex gap-2">
            <select
              v-model="searchForm.type"
              id="cropTypeFilter"
              class="form-select form-select-sm"
              style="width: 120px"
            >
              <option value="">작물 종류</option>
              <option value="토마토">토마토</option>
              <option value="딸기">딸기</option>
              <option value="파프리카">파프리카</option>
            </select>
            <!-- 검색 필터 : 작물 상태 -->
            <select
              v-model="searchForm.status"
              id="cropStatusFilter"
              class="form-select form-select-sm"
              style="width: 120px"
            >
              <option value="">작물 상태</option>
              <option value="BEST">양호</option>
              <option value="NORMAL">보통</option>
              <option value="BAD">불량</option>
            </select>
            <!-- 검색 필터 : 판매 상태 -->
            <select
              v-model="searchForm.saleStatus"
              id="saleStatusFilter"
              class="form-select form-select-sm"
              style="width: 120px"
            >
              <option value="">판매 상태</option>
              <option value="NOT_AVAILABLE">재고 없음</option>
              <option value="AVAILABLE">판매중</option>
              <option value="SOLD_OUT">판매완료</option>
              <option value="DISCONTINUED">폐기</option>
            </select>
            <!-- 정렬 -->
            <select
              v-model="searchForm.order"
              id="orderFilter"
              class="form-select form-select-sm"
              style="width: 120px"
            >
              <option value="">정렬</option>
              <option value="ID">재고ID</option>
              <option value="LASTEST">최신순</option>
              <option value="POPULAR">인기순</option>
              <option value="RECOMMENDED">추천순</option>
            </select>
          </div>
        </div>
        <!-- 재고 관리 테이블 -->
        <div class="card-body px-0 pt-0 pb-2">
          <div class="table-responsive p-0">
            <table class="table align-items-center mb-0">
              <!-- 테이블 헤더 -->
              <inventory-header />
              <!-- 테이블 바디 -->
              <tbody v-if="inventoryStore.farmInfo.cropList">
                <tr
                  v-for="inventory in inventoryStore.farmInfo.cropList"
                  :key="inventory.id"
                >
                  <!-- 재고 아이디 -->
                  <td>
                    <div class="d-flex px-2 py-1">
                      <div class="d-flex flex-column justify-content-center">
                        <p class="text-xs font-weight-bold mb-0">
                          {{ inventory.id }}
                        </p>
                      </div>
                    </div>
                  </td>
                  <!-- 작물 종류 -->
                  <td>
                    <p class="text-xs font-weight-bold mb-0">
                      {{ inventory.type }}
                    </p>
                  </td>
                  <!-- 작물 상태 -->
                  <td class="text-sm">
                    <span
                      :class="{
                        'badge badge-sm bg-gradient-secondary-green':
                          inventory.status === '양호',
                        'badge badge-sm bg-gradient-secondary-yellow':
                          inventory.status === '보통',
                        'badge badge-sm bg-gradient-danger':
                          inventory.status === '불량',
                      }"
                      >{{ inventory.status }}</span
                    >
                  </td>
                  <!-- 총 주문 요청량 -->
                  <td>
                    <p class="text-xs font-weight-bold mb-0">
                      {{ inventory.orderQuantity }} /㎡
                    </p>
                  </td>
                  <!-- 판매 상태 -->
                  <td class="text-sm text-center">
                    <p
                      class="mb-0"
                      :class="{
                        'badge badge-sm bg-gradient-info':
                          inventory.saleStatus === 'AVAILABLE',
                        'badge badge-sm bg-gradient-secondary':
                          inventory.saleStatus === 'SOLD_OUT',
                        'badge badge-sm bg-gradient-danger':
                          inventory.saleStatus === 'DISCONTINUED',
                        'badge badge-sm bg-gradient-warning':
                          inventory.saleStatus === 'NOT_AVAILABLE',
                      }"
                    >
                      {{ getSaleStatus(inventory.saleStatus) }}
                    </p>
                  </td>
                  <!-- 예측 산출량 -->
                  <td class="align-middle text-center text-xs font-weight-bold">
                    <span>{{ inventory.expectedQuantity }} /㎡</span>
                  </td>
                  <!-- 최대 예측 산출량 -->
                  <td class="align-middle text-center text-xs font-weight-bold">
                    <span>{{ inventory.maxExpectedQuantity }} /㎡</span>
                  </td>
                  <!-- 예측 수확일 -->
                  <td class="align-middle text-center">
                    <span class="text-secondary text-xs font-weight-bold">{{
                      inventory.expectedHarvestDate
                    }}</span>
                  </td>
                  <!-- 편집 버튼 -->
                  <td class="align-middle">
                    <a
                      href="javascript:;"
                      class="text-secondary font-weight-bold text-xs"
                      data-toggle="tooltip"
                      data-original-title="Edit user"
                      data-bs-toggle="modal"
                      data-bs-target="#exampleModal"
                    >
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width="16"
                        height="16"
                        fill="currentColor"
                        class="bi bi-pencil-square edit_btn"
                        viewBox="0 0 16 16"
                        @click="inventory_edit(inventory.id)"
                      >
                        <path
                          d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"
                        />
                        <path
                          fill-rule="evenodd"
                          d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"
                        />
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
