<script setup>
import api from "@/api/inventory";
import { useRoute } from "vue-router";
import { reactive, watch, computed, defineProps, defineEmits } from "vue";

const props = defineProps(["inventoryId"]);
const emits = defineEmits(["getInventoryList"]);
const route = useRoute();

// 화면에 렌더링할 재고 상세 데이터
let inventory = reactive({
  id: null, //재고 ID 및 작물 ID
  cropType: "", //작물 종류
  area: null, //재배 면적,
  cultivateType: "", //재배 방식
  expectedHarvestDate: "", //예상 수확일
  expectedQuantity: null, //예상 수확량
  maxExpectedQuantity: null, //최대 예상 수확량
  sowingStartDate: "", //파종 시작일
});

// 에러 메세지
const cropDetailFormError = reactive({
  area: {
    errorMessage: "",
    isValid: false,
  },
  sowingStartDate: {
    errorMessage: "",
    isValid: false,
  },
  expectedHarvestDate: {
    errorMessage: "",
    isValid: false,
  },
  expectedQuantity: {
    errorMessage: "",
    isValid: false,
  },
  maxExpectedQuantity: {
    errorMessage: "",
    isValid: false,
  },
});

// 모든 필드 입력값 검증
const isFormValid = computed(() => {
  return (
    cropDetailFormError.area.isValid &&
    cropDetailFormError.sowingStartDate.isValid &&
    cropDetailFormError.expectedHarvestDate.isValid &&
    cropDetailFormError.expectedQuantity.isValid &&
    cropDetailFormError.maxExpectedQuantity.isValid
  );
});

// 재배 면적 입력 검증
const areaRules = [
  (event) => {
    if (event.target.value) {
      cropDetailFormError.area.errorMessage = null;
      cropDetailFormError.area.isValid = true;
      return true;
    } else {
      cropDetailFormError.area.errorMessage = "재배 면적을 입력 해주세요.";
      cropDetailFormError.area.isValid = false;
      return false;
    }
  },
];

// 파종 시작일 선택 검증
const sowingStartDateRules = [
  (event) => {
    if (event.target.value) {
      cropDetailFormError.sowingStartDate.errorMessage = null;
      cropDetailFormError.sowingStartDate.isValid = true;
      return true;
    } else {
      cropDetailFormError.sowingStartDate.errorMessage =
        "파종 시작일을 선택 해주세요.";
      cropDetailFormError.sowingStartDate.isValid = false;
      return false;
    }
  },
];

// 예측 수확일 선택 검증
const expectedHarvestDateRules = [
  (event) => {
    if (event.target.value) {
      cropDetailFormError.expectedHarvestDate.errorMessage = null;
      cropDetailFormError.expectedHarvestDate.isValid = true;
      return true;
    } else {
      cropDetailFormError.expectedHarvestDate.errorMessage =
        "예측 수확일을 선택 해주세요.";
      cropDetailFormError.expectedHarvestDate.isValid = false;
      return false;
    }
  },
];

// 예측 산출량 입력 검증
const expectedQuantityRules = [
  (event) => {
    if (event.target.value) {
      cropDetailFormError.expectedQuantity.errorMessage = null;
      cropDetailFormError.expectedQuantity.isValid = true;
      return true;
    } else {
      cropDetailFormError.expectedQuantity.errorMessage =
        "예측 산출량을 입력 해주세요.";
      cropDetailFormError.expectedQuantity.isValid = false;
      return false;
    }
  },
];

// 최대 예측 산출량 입력 검증
const maxExpectedQuantityRules = [
  (event) => {
    if (event.target.value) {
      cropDetailFormError.maxExpectedQuantity.errorMessage = null;
      cropDetailFormError.maxExpectedQuantity.isValid = true;
      return true;
    } else {
      cropDetailFormError.maxExpectedQuantity.errorMessage =
        "최대 예측 산출량을 입력 해주세요.";
      cropDetailFormError.maxExpectedQuantity.isValid = false;
      return false;
    }
  },
];

// 재고 수정 요청 데이터
const inventoryEditForm = reactive({
  cropId: null,
});

// 수정 버튼 클릭시, 수정 api 호출
const updateInventory = async () => {
  Object.assign(inventoryEditForm, inventory); //reactive 객체 내부 값 업데이트
  inventoryEditForm.cropId = props.inventoryId; //전달받은 선택한 재고 아이디 값

  const data = await api.updateInventory(inventoryEditForm);
  if (data && data.success) {
    // 모달 닫기

    // 재고 목록 새로고침
    emits("getInventoryList", route.query.farmId);
  } else {
    alert("재고를 수정하지 못하였습니다.");
  }
};

// 재고 상세 조회 api 호출
const getInventoryDetail = async () => {
  const data = await api.getInventoryDetail(props.inventoryId);

  if (data && data.success) {
    Object.assign(inventory, data.data);

    //필드 값이 있으면 isValid를 true로 설정
    cropDetailFormError.area.isValid = !!data.data.area;
    cropDetailFormError.sowingStartDate.isValid = !!data.data.sowingStartDate;
    cropDetailFormError.expectedHarvestDate.isValid =
      !!data.data.expectedHarvestDate;
    cropDetailFormError.expectedQuantity.isValid = !!data.data.expectedQuantity;
    cropDetailFormError.maxExpectedQuantity.isValid =
      !!data.data.maxExpectedQuantity;
  } else {
    alert("데이터를 불러오지 못하였습니다.");
  }
};

// 선택한 inventoryId 값이 바뀔 때마다 상세 조회 함수 호출
watch(
  () => props.inventoryId,
  (newInventoryId) => {
    getInventoryDetail(newInventoryId);
  }
);
</script>

<template>
  <div
    class="modal fade"
    id="exampleModal"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">재고 수정</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>

        <div class="modal-body container-fluid py-4">
          <div class="row">
            <div class="col-md-12">
              <div class="card">
                <div class="card-body">
                  <p class="text-uppercase text-sm">재고 정보</p>
                  <div class="row">
                    <div class="col-md-6">
                      <div class="form-group">
                        <label
                          for="example-text-input"
                          class="form-control-label"
                          >재고 ID</label
                        >
                        <input
                          class="form-control"
                          type="text"
                          :placeholder="inventory.id"
                          disabled
                        />
                      </div>
                    </div>
                    <div class="col-md-6">
                      <div class="form-group">
                        <label
                          for="example-text-input"
                          class="form-control-label"
                          >작물 종류</label
                        >
                        <input
                          class="form-control"
                          type="text"
                          :placeholder="inventory.type"
                          disabled
                        />
                      </div>
                    </div>
                    <!-- 재배 면적 -->
                    <div class="col-md-6">
                      <div class="form-group">
                        <label
                          for="example-text-input"
                          class="form-control-label"
                          >재배 면적</label
                        >
                        <div class="input-group">
                          <input
                            class="form-control"
                            type="text"
                            v-model="inventory.area"
                            @change="areaRules"
                          />
                          <span class="input-group-text form-control-20"
                            >/㎡</span
                          >
                        </div>
                        <!-- 경고 문구 -->
                        <div v-if="cropDetailFormError.area.errorMessage">
                          <label class="form-control-label text-danger">{{
                            cropDetailFormError.area.errorMessage
                          }}</label>
                        </div>
                      </div>
                    </div>
                    <!-- 재배 방식 -->
                    <div class="col-md-6">
                      <div class="form-group">
                        <label
                          for="example-text-input"
                          class="form-control-label"
                          >재배 방식</label
                        >
                        <input
                          class="form-control"
                          type="text"
                          :placeholder="inventory.cultivateType"
                          disabled
                        />
                      </div>
                    </div>
                    <!-- 파종 시작일 -->
                    <div class="col-md-6">
                      <div class="form-group">
                        <label
                          for="example-text-input"
                          class="form-control-label"
                          >파종 시작일</label
                        >
                        <input
                          class="form-control"
                          type="date"
                          v-model="inventory.sowingStartDate"
                          @change="sowingStartDateRules"
                        />
                        <!-- 경고 문구 -->
                        <div
                          v-if="
                            cropDetailFormError.sowingStartDate.errorMessage
                          "
                        >
                          <label class="form-control-label text-danger">{{
                            cropDetailFormError.sowingStartDate.errorMessage
                          }}</label>
                        </div>
                      </div>
                    </div>
                    <!-- 예측 수확일 -->
                    <div class="col-md-6">
                      <div class="form-group">
                        <label
                          for="example-text-input"
                          class="form-control-label"
                          >예측 수확일</label
                        >
                        <input
                          class="form-control"
                          type="date"
                          v-model="inventory.expectedHarvestDate"
                          @change="expectedHarvestDateRules"
                        />
                        <!-- 경고 문구 -->
                        <div
                          v-if="
                            cropDetailFormError.expectedHarvestDate.errorMessage
                          "
                        >
                          <label class="form-control-label text-danger">{{
                            cropDetailFormError.expectedHarvestDate.errorMessage
                          }}</label>
                        </div>
                      </div>
                    </div>
                    <!-- 예측 산출량 -->
                    <div class="col-md-6">
                      <div class="form-group">
                        <label
                          for="example-text-input"
                          class="form-control-label"
                          >예측 산출량</label
                        >
                        <div class="input-group">
                          <input
                            class="form-control"
                            type="text"
                            v-model="inventory.expectedQuantity"
                            @change="expectedQuantityRules"
                          />
                          <span class="input-group-text form-control-20"
                            >/㎡</span
                          >
                        </div>
                        <!-- 경고 문구 -->
                        <div
                          v-if="
                            cropDetailFormError.expectedQuantity.errorMessage
                          "
                        >
                          <label class="form-control-label text-danger">{{
                            cropDetailFormError.expectedQuantity.errorMessage
                          }}</label>
                        </div>
                      </div>
                    </div>
                    <!-- 최대 예측 산출량 -->
                    <div class="col-md-6">
                      <div class="form-group">
                        <label
                          for="example-text-input"
                          class="form-control-label"
                          >최대 예측 산출량</label
                        >
                        <div class="input-group">
                          <input
                            class="form-control"
                            type="text"
                            v-model="inventory.maxExpectedQuantity"
                            @change="maxExpectedQuantityRules"
                          />
                          <span class="input-group-text form-control-20"
                            >/㎡</span
                          >
                        </div>
                        <!-- 경고 문구 -->
                        <div
                          v-if="
                            cropDetailFormError.maxExpectedQuantity.errorMessage
                          "
                        >
                          <label class="form-control-label text-danger">{{
                            cropDetailFormError.maxExpectedQuantity.errorMessage
                          }}</label>
                        </div>
                      </div>
                    </div>
                  </div>

                  <hr class="horizontal dark" />
                  <p class="text-uppercase text-sm">추가 정보</p>
                  <div class="row">
                    <div class="col-md-12">
                      <div class="form-group">
                        <label
                          for="example-text-input"
                          class="form-control-label"
                          >메모</label
                        >
                        <input
                          class="form-control"
                          type="text"
                          placeholder="재고에 관련하여 필요한 메모 작성하세요."
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            취소
          </button>
          <button
            type="button"
            class="btn btn-primary"
            :style="{ color: !isFormValid ? 'white' : '' }"
            @click="updateInventory()"
            :disabled="!isFormValid"
          >
            저장
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
