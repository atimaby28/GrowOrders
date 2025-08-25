<script setup>
import ArgonButton from "@/components/ArgonButton.vue";

const { mode, canEdit } = defineProps({
  mode: { type: String, required: true },      
  canEdit: { type: Boolean, default: false }  
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
