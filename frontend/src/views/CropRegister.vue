<script setup>
import api from "@/api/crops";
import ArgonInput from "@/components/ArgonInput.vue"; //입력 컴포넌트
import CropCard from "@/components/CropCard.vue"; //오른쪽 작물 카드 컴포넌트
import ArgonButton from "@/components/ArgonButton.vue"; //버튼 컴포넌트
import { useUserStore } from "../store/users/useUserStore.js";
import { ref, reactive, computed } from "vue";

const mode = ref("register");
const userStore = useUserStore();

// 드롭다운 목록
const cropRegisterForm = reactive({
  farmId: "", // 01. 농장
  type: "", // 02. 품목
  sowingStartDate: "", //03. 파종 시작일
  status: "", //04. 작물 상태
  area: "", //05. 재배 면적
  cultivateType: "", //06. 재배 방식
});

// 농장 선택에 따라 선택 가능한 작물 종류 재계산
const availableCropTypeOptions = computed(() => {
  const allCropTypeOptions = ["토마토", "딸기", "파프리카"]; //const allCropTypeOptions = ["토마토"];

  if (!cropRegisterForm.farmId) return allCropTypeOptions;

  const farm = userStore.user.ownedFarm.find(
    (f) => f.id == cropRegisterForm.farmId
  );
  return allCropTypeOptions.filter((crop) => !farm.cropType.includes(crop));
});

// 품목 프로필 카드
const selectedCropInfo = computed(
  () => cropInfoMap[cropRegisterForm.type] || null
);

// 등록 완료하면 인풋값 수정 못하게 readOnly 처리
const isReadOnly = computed(() => mode.value == "detail");

// 이미지 파일과 미리보기 URL을 상태로 관리
const profileImageFile = ref(null);
const previewUrl = ref("");

// 파일 선택 시 호출되는 함수
const onImageChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    profileImageFile.value = file;
    previewUrl.value = URL.createObjectURL(file);
  } else {
    profileImageFile.value = null;
    previewUrl.value = "";
  }
};

// 품목 프로필 카드
const cropInfoMap = {
  토마토: {
    name: "토마토",
    image: require("@/assets/img/tomato.jpg"),
    description: "과즙이 풍부하고 비타민이 많은 대표 과채류",
  },
  딸기: {
    name: "딸기",
    image: require("@/assets/img/strawberry.jpg"),
    description: "봄철 대표 과일, 당도와 향이 뛰어남",
  },
  파프리카: {
    name: "파프리카",
    image: require("@/assets/img/paprika.jpg"),
    description: "색상이 다양하고 비타민 C가 풍부한 채소",
  },
};

// 에러 메세지
const cropRegisterFormError = reactive({
  farmId: {
    errorMessage: "",
    isValid: false,
  },
  type: {
    errorMessage: "",
    isValid: false,
  },
  status: {
    errorMessage: "",
    isValid: false,
  },
  sowingStartDate: {
    errorMessage: "",
    isValid: false,
  },
  area: {
    errorMessage: "",
    isValid: false,
  },
  cultivateType: {
    errorMessage: "",
    isValid: false,
  },
});

// 모든 필드 입력값 검증
const isFormValid = computed(() => {
  return (
    cropRegisterFormError.farmId.isValid &&
    cropRegisterFormError.type.isValid &&
    cropRegisterFormError.status.isValid &&
    cropRegisterFormError.sowingStartDate.isValid &&
    cropRegisterFormError.area.isValid &&
    cropRegisterFormError.cultivateType.isValid
  );
});

// 농장 선택 바뀌면 내용 초기화
const cropRegisterFormInitial = () => {
  cropRegisterForm.type = "";
  cropRegisterForm.sowingStartDate = "";
  cropRegisterForm.status = "";
  cropRegisterForm.area = "";
  cropRegisterForm.cultivateType = "";
};

// 농장 아이디 선택 검증
const farmIdRules = [
  (event) => {
    if (event.target.value) {
      cropRegisterFormError.farmId.errorMessage = null;
      cropRegisterFormError.farmId.isValid = true;
      cropRegisterFormInitial();
      return true;
    } else {
      cropRegisterFormError.farmId.errorMessage = "농장을 선택 해주세요.";
      cropRegisterFormError.farmId.isValid = false;
      return false;
    }
  },
];

// 작물 종류 선택 검증
const typeRules = [
  (event) => {
    if (event.target.value) {
      cropRegisterFormError.type.errorMessage = null;
      cropRegisterFormError.type.isValid = true;
      return true;
    } else {
      cropRegisterFormError.type.errorMessage = "작물 종류를 선택해주세요.";
      cropRegisterFormError.type.isValid = false;
      return false;
    }
  },
];

// 작물 상태 선택 검증
const statusRules = [
  (event) => {
    if (event.target.value) {
      cropRegisterFormError.status.errorMessage = null;
      cropRegisterFormError.status.isValid = true;
      return true;
    } else {
      cropRegisterFormError.status.errorMessage = "작물 상태를 선택 해주세요.";
      cropRegisterFormError.status.isValid = false;
      return false;
    }
  },
];

// 파종 시작일 선택 검증
const sowingStartDateRules = [
  (event) => {
    if (event.target.value) {
      cropRegisterFormError.sowingStartDate.errorMessage = null;
      cropRegisterFormError.sowingStartDate.isValid = true;
      return true;
    } else {
      cropRegisterFormError.sowingStartDate.errorMessage =
        "파종 시작일을 선택 해주세요.";
      cropRegisterFormError.sowingStartDate.isValid = false;
      return false;
    }
  },
];

// 재배 면적 입력 검증
const areaRules = [
  (event) => {
    if (event.target.value) {
      cropRegisterFormError.area.errorMessage = null;
      cropRegisterFormError.area.isValid = true;
      return true;
    } else {
      cropRegisterFormError.area.errorMessage = "재배 면적을 입력 해주세요.";
      cropRegisterFormError.area.isValid = false;
      return false;
    }
  },
];

// 재배 방식 선택 검증
const cultivateTypeRules = [
  (event) => {
    if (event.target.value) {
      cropRegisterFormError.cultivateType.errorMessage = null;
      cropRegisterFormError.cultivateType.isValid = true;
      return true;
    } else {
      cropRegisterFormError.cultivateType.errorMessage =
        "재배 방식을 선택 해주세요.";
      cropRegisterFormError.cultivateType.isValid = false;
      return false;
    }
  },
];

// 등록 버튼 클릭시, 등록 api 호출
const onSubmit = async () => {
  const data = await api.cropRegister(cropRegisterForm);

  if (data) {
    if (data.success) {
      userStore.user.ownedFarm = data.data.ownedFarm;
      cropRegisterFormInitial();
      alert("작물 등록 성공했습니다.");
    } else {
      for (const key in cropRegisterFormError) {
        cropRegisterFormError[key].errorMessage = data.data[key];
        cropRegisterFormError[key].isValid = false;
      }
    }
  }
};
</script>

<template>
  <form @submit.prevent="onSubmit">
    <main>
      <!-- 페이지 전체 너비를 가득 채우는 컨테이너 -->
      <div class="container-fluid">
        <!--상단 배경 이미지-->
        <div
          class="page-header min-height-300"
          :style="{
            backgroundImage: `url(${require('@/assets/img/crop_register_image.jpg')})`,
            marginRight: '-24px',
            marginLeft: '-25%',
          }"
        >
          <!--반투명한 오버레이 효과-->
          <span class="mask bg-gradient-success opacity-6"></span>
        </div>
        <!--카드 그림자 효과-->
        <div class="card shadow-lg mt-n6"></div>
      </div>

      <!--메인 컨테이너-->
      <div class="py-4 container-fluid">
        <!-- 그리드에서 열들을 한 줄(row)로 배치하는 용도, 왼쪽카드랑 오른쪽 카드 수평유지-->
        <div class="row">
          <!--왼쪽 카드(등록 폼) 너비-->
          <div class="col-md-8">
            <!--왼쪽 카드(등록 폼)-->
            <div class="card">
              <!--왼쪽 카드 헤더-->
              <div class="card-header pb-0">
                <!--가로방향에 따라 유연하게 레이아웃 정렬-->
                <div class="d-flex align-items-center">
                  <!--아래쪽 마진 0, 타이틀-->
                  <p class="mb-0 b" v-if="mode == 'register'">생물 등록</p>
                  <p class="mb-0 b" v-if="mode == 'detail'">생물 상세</p>

                  <!--등록버튼-->
                  <argon-button
                    v-if="mode === 'register'"
                    :style="{ color: !isFormValid ? 'white' : '' }"
                    size="sm"
                    class="ms-auto"
                    :disabled="!isFormValid"
                    >등록</argon-button
                  >
                  <!--수정 버튼-->
                  <argon-button
                    v-if="mode === 'detail'"
                    :color="isFormValid ? 'warning' : 'secondary'"
                    :style="{ color: !isFormValid ? 'white' : '' }"
                    size="sm"
                    class="ms-auto"
                  >
                    수정
                  </argon-button>
                </div>
              </div>

              <!--왼쪽  카드 바디-->
              <div class="card-body">
                <!--1층 : 입력폼 묶어서 수평으로 정렬 -->
                <div class="row">
                  <!--1.농장아이디-->
                  <div class="col-md-6">
                    <div class="form-group">
                      <label for="example-text-input" class="form-control-label"
                        >농장 아이디</label
                      >
                      <select
                        class="form-control"
                        id="farm-select"
                        v-model="cropRegisterForm.farmId"
                        :disabled="isReadOnly"
                        :class="isReadOnly ? 'bg-light text-muted' : ''"
                        @change="farmIdRules"
                      >
                        <option value="">-- 농장 선택 --</option>
                        <option
                          v-for="farm in userStore.user.ownedFarm"
                          :value="farm.id"
                          :key="farm.id"
                        >
                          {{ farm.name }}
                        </option>
                      </select>
                      <!-- 경고 문구 -->
                      <div v-if="cropRegisterFormError.farmId.errorMessage">
                        <label class="form-control-label text-danger">{{
                          cropRegisterFormError.farmId.errorMessage
                        }}</label>
                      </div>
                    </div>
                  </div>

                  <!--2.품목-->
                  <div class="col-md-6">
                    <div class="form-group">
                      <label for="example-text-input" class="form-control-label"
                        >품목</label
                      >
                      <select
                        class="form-control"
                        id="crop-select"
                        v-model="cropRegisterForm.type"
                        :disabled="isReadOnly"
                        :class="isReadOnly ? 'bg-light text-muted' : ''"
                        @change="typeRules"
                      >
                        <option value="">-- 품목 선택 --</option>
                        <option
                          v-for="crop in availableCropTypeOptions"
                          :key="crop"
                          :value="crop"
                        >
                          {{ crop }}
                        </option>
                      </select>
                      <!-- 경고 문구 -->
                      <div v-if="cropRegisterFormError.type.errorMessage">
                        <label class="form-control-label text-danger">{{
                          cropRegisterFormError.type.errorMessage
                        }}</label>
                      </div>
                    </div>
                  </div>
                </div>

                <!--2층-->
                <div class="row">
                  <!--3. 파종일-->
                  <div class="col-md-6">
                    <label for="example-text-input" class="form-control-label"
                      >파종일</label
                    >
                    <argon-input
                      type="date"
                      v-model="cropRegisterForm.sowingStartDate"
                      :readonly="isReadOnly"
                      :class="isReadOnly ? 'bg-light text-muted' : ''"
                      @change="sowingStartDateRules"
                    />
                    <!-- 경고 문구 -->
                    <div
                      v-if="cropRegisterFormError.sowingStartDate.errorMessage"
                    >
                      <label class="form-control-label text-danger">{{
                        cropRegisterFormError.sowingStartDate.errorMessage
                      }}</label>
                    </div>
                  </div>

                  <!-- 4. 작물 상태-->
                  <div class="col-md-6">
                    <div class="form-group">
                      <label for="example-text-input" class="form-control-label"
                        >작물 상태</label
                      >
                      <select
                        class="form-control"
                        id="crop-status-select"
                        v-model="cropRegisterForm.status"
                        :disabled="isReadOnly"
                        :class="isReadOnly ? 'bg-light text-muted' : ''"
                        @change="statusRules"
                      >
                        <option value="">-- 작물 상태 선택 --</option>
                        <option value="BEST">양호</option>
                        <option value="NORMAL">보통</option>
                        <option value="BAD">불량</option>
                      </select>
                      <!-- 경고 문구 -->
                      <div v-if="cropRegisterFormError.status.errorMessage">
                        <label class="form-control-label text-danger">{{
                          cropRegisterFormError.status.errorMessage
                        }}</label>
                      </div>
                    </div>
                  </div>
                </div>

                <!--3층-->
                <div class="row">
                  <!--5. 재배 면적-->
                  <div class="col-md-6">
                    <div class="form-group">
                      <label for="example-text-input" class="form-control-label"
                        >재배 면적</label
                      >
                      <!--단위 고정시키기-->
                      <div class="input-group">
                        <input
                          type="number"
                          class="form-control"
                          id="cultivation-area"
                          v-model="cropRegisterForm.area"
                          :disabled="isReadOnly"
                          :class="isReadOnly ? 'bg-light text-muted' : ''"
                          placeholder="면적을 입력하세요"
                          @change="areaRules"
                        />
                        <span class="input-group-text">㎡</span>
                      </div>
                      <!-- 경고 문구 -->
                      <div v-if="cropRegisterFormError.area.errorMessage">
                        <label class="form-control-label text-danger">{{
                          cropRegisterFormError.area.errorMessage
                        }}</label>
                      </div>
                    </div>
                  </div>

                  <!-- 6. 재배 방식 -->
                  <div class="col-md-6">
                    <div class="form-group">
                      <label for="example-text-input" class="form-control-label"
                        >재배 방식</label
                      >
                      <select
                        class="form-control"
                        id="crop-cultivateType-select"
                        v-model="cropRegisterForm.cultivateType"
                        :disabled="isReadOnly"
                        :class="isReadOnly ? 'bg-light text-muted' : ''"
                        @change="cultivateTypeRules"
                      >
                        <option value="">-- 재배 방식 선택 --</option>
                        <option value="유리">유리</option>
                        <option value="비닐">비닐</option>
                      </select>
                      <!-- 경고 문구 -->
                      <div
                        v-if="cropRegisterFormError.cultivateType.errorMessage"
                      >
                        <label class="form-control-label text-danger">{{
                          cropRegisterFormError.cultivateType.errorMessage
                        }}</label>
                      </div>
                    </div>
                  </div>
                </div>

                <!--4층-->
                <div class="row">
                  <!--11.사진 등록-->
                  <div class="col-md-12">
                    <div class="form-group">
                      <label for="profile-image" class="form-control-label"
                        >사진 등록</label
                      >
                      <!-- 이미지 업로드 -->
                      <!--이미지는 v-model 사용불가 axios로 넘겨줘야함-->
                      <input
                        type="file"
                        class="form-control"
                        id="profile-image"
                        accept="image/*"
                        @change="onImageChange"
                        :readonly="isReadOnly"
                        :class="isReadOnly ? 'bg-light text-muted' : ''"
                      />
                      <!-- 미리보기 -->
                      <img
                        v-if="previewUrl"
                        :src="previewUrl"
                        class="profile-preview-box mt-3"
                        alt="미리보기"
                      />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 오른쪽: 품목 프로필 카드 -->
          <div class="col-md-4" v-if="selectedCropInfo">
            <CropCard :crop="selectedCropInfo" />
          </div>
        </div>
      </div>
    </main>
  </form>
</template>

<style scoped>
.profile-preview-box {
  width: 200px;
  height: 200px;
  object-fit: cover;
  border: 1px solid #ccc;
  border-radius: 8px;
}
</style>
