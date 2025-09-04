<script setup>
import { ref, computed, onMounted, reactive } from 'vue'
import api from '../api/push/index.js'
import PushTable from '@/views/components/PushTable.vue'
import MiniStatisticsCard from "@/examples/Cards/MiniStatisticsCard.vue"

// 알림 타입
const TYPE_CODE = {
  '이상 기후 알림': 'ABNORMAL_WEATHER_NOTIFICATION',
  '주문 등록 알림': 'ORDER_REGISTER_NOTIFICATION',
  '주문 상태 변경 알림': 'ORDER_STATUS_CHANGED_NOTIFICATION',
}
const cards = ['전체', ...Object.keys(TYPE_CODE)]

// 알림 타입 카드 선택시 해당 타입의 알림만 출력
const activeType = ref('전체')
const selectType = (type) => { activeType.value = type }
const filteredNotifications = computed(() => {
  const rows = Array.isArray(notiState.list) ? notiState.list : []
  if (activeType.value === '전체') return rows
  const code = TYPE_CODE[activeType.value]
  return code ? rows.filter(n => String(n.pushType) === code) : rows
})

// 알림 데이터 가져오기 
const notiState = reactive({ list: [], loaded: false, error: null })
onMounted(async () => {
  try {
    const res = await api.notification()
    const rows = Array.isArray(res) ? res : (Array.isArray(res?.data) ? res.data : [])
    notiState.list = rows
  } catch (e) {
    console.error('API 호출 오류:', e)
    notiState.error = e
    notiState.list = []
  } finally {
    notiState.loaded = true
  }
})
</script>

<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <div class="col-lg-3 col-sm-6 mb-lg-0 mb-4" v-for="type in cards" :key="type">
        <mini-statistics-card
          class="p-0 bg-white shadow-sm"
          :title="{ text: type, color: 'text-dark opacity-7' }"
          :value="{ text: null, color: 'text-dark' }"
          :icon="{ component: 'text-white ni ni-world', background: 'bg-gradient-success' }"
          @click="selectType(type)"
        />
      </div>
    </div>

    <div class="row">
      <div class="col-12">
        <PushTable :notifications="filteredNotifications" />
      </div>
    </div>
  </div>
</template>
