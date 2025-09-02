<script setup>
import ArgonButton from "@/components/ArgonButton.vue";

const { mode, canEdit } = defineProps({
  mode: { type: String, required: true },        // 'register' | 'detail' | 'edit'
  canEdit: { type: Boolean, default: false }
});

const emit = defineEmits(["submit", "edit" ]);
</script>

<template>
  <div class="card-header pb-0">
    <div class="d-flex align-items-center w-100">
      <p class="mb-0">
        {{ mode === 'register' ? '농장 등록'
          : mode === 'edit' ? '농장 수정'
          : '농장 상세' }}
      </p>

      <div class="ms-auto d-flex gap-2">
        <argon-button
          v-if="mode === 'detail' && canEdit"
          color="warning"
          size="sm"
          @click="emit('edit')"
        >수정</argon-button>

        <argon-button
          v-if="mode === 'register' || mode === 'edit'"
          color="success"
          size="sm"
          @click="emit('submit')" 
        >{{ mode === 'edit' ? '저장' : '등록' }}</argon-button>
      </div>
    </div>
  </div>
</template>
