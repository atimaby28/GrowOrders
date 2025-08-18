<script setup>
import { computed } from "vue";
import ArgonInput from "@/components/ArgonInput.vue";

const props = defineProps({
  readOnly: { type: Boolean, default: false },
  // v-model: 개별 필드 바인딩
  userId: { type: [String, Number], default: '' },
  farmName: { type: String, default: "" },
  selectedLocation: { type: String, default: "" },
  addressDetail: { type: String, default: "" },
  area: { type: [String, Number], default: "" },
  description: { type: String, default: "" },
  locationOptions: { type: Array, default: () => [] },
});
const emit = defineEmits([
  "update:farmName",
  "update:selectedLocation",
  "update:addressDetail",
  "update:area",
  "update:description",
]);

const roCls = computed(() => (props.readOnly ? "bg-light text-muted" : ""));
</script>

<template>
  <!-- 사용자 아이디 (예시 값 그대로 유지) -->
  <div class="row">
    <div class="col-md-12">
      <label class="form-control-label">사용자 아이디</label>
      <argon-input :model-value="String(userId ?? '')" readonly class="bg-light text-muted" />
    </div>
  </div>

  <!-- 농장 이름 -->
  <div class="row mb-3">
    <div class="col-md-12">
      <label class="form-control-label">농장 이름</label>
      <input
        :value="farmName"
        @input="emit('update:farmName', $event.target.value)"
        type="text"
        class="form-control"
        :readonly="readOnly"
        :class="roCls"
        placeholder="농장 이름을 입력하세요"
      />
    </div>
  </div>

  <!-- 지역 -->
  <div class="row mb-3">
    <div class="col-md-12">
      <label class="form-control-label">농장 소재 지역</label>
      <select
        id="location-select"
        class="form-control"
        :value="selectedLocation"
        @change="emit('update:selectedLocation', $event.target.value)"
        :disabled="readOnly"
        :class="roCls"
      >
        <option value="">-- 소재 지역 선택 --</option>
        <option v-for="loc in locationOptions" :key="loc" :value="loc">
          {{ loc }}
        </option>
      </select>
    </div>
  </div>

  <!-- 상세 주소 -->
  <div class="row mb-3">
    <div class="col-md-12">
      <label class="form-control-label">농장 상세 주소</label>
      <input
        :value="addressDetail"
        @input="emit('update:addressDetail', $event.target.value)"
        type="text"
        class="form-control"
        :readonly="readOnly"
        :class="roCls"
        placeholder="농장 상세 주소를 입력하세요"
      />
    </div>
  </div>

  <!-- 면적 -->
  <div class="row mb-3">
    <div class="col-md-12">
      <label class="form-control-label">농장 면적</label>
      <div class="input-group">
        <input
          :value="area"
          @input="emit('update:area', $event.target.value)"
          type="number"
          class="form-control"
          id="cultivation-area"
          :readonly="readOnly"
          :class="roCls"
          placeholder="면적을 입력하세요"
        />
        <span class="input-group-text">㎡</span>
      </div>
    </div>
  </div>

  <!-- 소개 -->
  <div class="row mb-3">
    <div class="col-md-12">
      <label for="farm-description" class="form-control-label">농장 소개</label>
      <textarea
        :value="description"
        @input="emit('update:description', $event.target.value)"
        id="farm-description"
        class="form-control"
        rows="4"
        :readonly="readOnly"
        :class="roCls"
        placeholder="농장 소개글을 입력하세요"
      />
    </div>
  </div>
</template>
