<script setup>
import { ref, watch, onBeforeUnmount } from "vue";

const props = defineProps({
  disabled: { type: Boolean, default: false },
  file: { type: File, default: null },
  initialUrl: { type: String, default: null },
});
const emit = defineEmits(["update:file"]);

const previewUrl = ref(null);
let lastObjectUrl = null;

function updatePreview(file) {
  if (lastObjectUrl) URL.revokeObjectURL(lastObjectUrl);
  if (file) {
    lastObjectUrl = URL.createObjectURL(file);
    previewUrl.value = lastObjectUrl;
  } else {
    previewUrl.value = null;
  }
}

function onChange(e) {
  const f = e.target.files?.[0] || null;
  emit("update:file", f);
  updatePreview(f);
}

watch(() => props.file, f => updatePreview(f), { immediate: true });
watch(() => props.initialUrl, () => { if (!props.file) updatePreview(null) });

onBeforeUnmount(() => { if (lastObjectUrl) URL.revokeObjectURL(lastObjectUrl) });
</script>

<template>
  <div class="form-group">
    <label for="profile-image" class="form-control-label">농장 사진 등록</label>
    <input
      type="file"
      class="form-control"
      id="profile-image"
      accept="image/*"
      :disabled="disabled"
      @change="onChange"
    />
    <img v-if="previewUrl" :src="previewUrl" class="profile-preview-box mt-3" alt="미리보기" />
  </div>
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