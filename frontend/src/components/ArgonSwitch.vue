<script setup>
import { defineProps, defineEmits } from "vue";

defineProps({
  name: { type: String, required: true },
  id: { type: String, required: true },
  modelValue: { type: Boolean, default: false }, // v-model용
  labelClass: { type: String, default: "" },
  inputClass: { type: String, default: "" },
});

const emit = defineEmits(["update:modelValue"]);

const toggle = (event) => {
  emit("update:modelValue", event.target.checked);
};
</script>

<template>
  <div class="form-check form-switch ps-0">
    <input
      :id="id"
      class="form-check-input ms-0"
      :class="inputClass"
      type="checkbox"
      :name="name"
      :checked="modelValue"
      @change="toggle"
    />
    <label class="form-check-label" :class="labelClass" :for="id">
      <slot />
    </label>
  </div>
</template>
