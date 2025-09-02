<script setup>
defineProps({
  readOnly: { type: Boolean, default: false },
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
</script>

<template>
  <div class="row mb-3">
    <div class="col-md-12">
      <label class="form-control-label">농장 이름</label>
      <input
        :value="farmName"
        @input="emit('update:farmName', $event.target.value)"
        type="text"
        class="form-control"
        :disabled="readOnly"
        placeholder="농장 이름을 입력하세요"
      />
    </div>
  </div>

  <div class="row mb-3">
    <div class="col-md-12">
      <label class="form-control-label">농장 소재 지역</label>
      <select
        id="location-select"
        class="form-control"
        :value="selectedLocation"
        @change="emit('update:selectedLocation', $event.target.value)"
        :disabled="readOnly"
      >
        <option value="">-- 소재 지역 선택 --</option>
        <option v-for="loc in locationOptions" :key="loc" :value="loc">
          {{ loc }}
        </option>
      </select>
    </div>
  </div>

  <div class="row mb-3">
    <div class="col-md-12">
      <label class="form-control-label">농장 상세 주소</label>
      <input
        :value="addressDetail"
        @input="emit('update:addressDetail', $event.target.value)"
        type="text"
        class="form-control"
        :disabled="readOnly"
        placeholder="농장 상세 주소를 입력하세요"
      />
    </div>
  </div>

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
          :disabled="readOnly"
          placeholder="면적을 입력하세요"
          step="any"
        />
        <span class="input-group-text">㎡</span>
      </div>
    </div>
  </div>

  <div class="row mb-3">
    <div class="col-md-12">
      <label for="farm-description" class="form-control-label">농장 소개</label>
      <textarea
        :value="description"
        @input="emit('update:description', $event.target.value)"
        id="farm-description"
        class="form-control"
        rows="4"
        :disabled="readOnly"
        placeholder="농장 소개글을 입력하세요"
      />
    </div>
  </div>
</template>
