<script setup>
import ArgonButton from "@/components/ArgonButton.vue";

// ✅ props 객체를 변수에 담지 말고 구조 분해해서 사용
const { mode, canEdit } = defineProps({
  mode: { type: String, required: true },        // 'register' | 'detail'
  canEdit: { type: Boolean, default: false }     // 수정 버튼 노출 여부
});

const emit = defineEmits(["submit", "edit"]);
</script>

<template>
  <div class="card-header pb-0">
    <div class="d-flex align-items-center">
      <p class="mb-0" v-if="mode === 'register'">농장 등록</p>
      <p class="mb-0" v-else>농장 상세</p>

      <argon-button
        v-if="mode === 'register'"
        color="success"
        size="sm"
        class="ms-auto"
        @click="emit('submit')"
      >
        등록
      </argon-button>

      <argon-button
        v-if="mode === 'detail' && canEdit"
        color="warning"
        size="sm"
        class="ms-auto"
        @click="emit('edit')"
      >
        수정
      </argon-button>
    </div>
  </div>
</template>
