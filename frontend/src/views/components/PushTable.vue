<script setup>
defineProps({
  notifications: { type: Array, default: () => [] }
})

// 알림 타입
const CODE_LABEL = {
  ABNORMAL_WEATHER_NOTIFICATION: '이상 기후 알림',
  ORDER_REGISTER_NOTIFICATION: '주문 등록 알림',
  ORDER_STATUS_CHANGED_NOTIFICATION: '주문 상태 변경 알림',
}
const typeLabel = (code) => CODE_LABEL[code] || ''

// 수신 시간 
const formatDate = (ts) => {
  if (!ts) return ''
  const d = new Date(ts)
  return d.toLocaleString('ko-KR', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit'
  })
}
</script>

<template>
  <div class="card">
    <div class="card-header pb-0"><h6>알림 목록</h6></div>
    <div class="card-body px-0 pt-0 pb-2">
      <div class="table-responsive p-0">
        <table class="table align-items-center mb-0" style="table-layout: fixed;">
          <thead>
            <tr>
              <th style="width: 20%;" class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">유형</th>
              <th style="width: 40%;" class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">제목 / 내용</th>
              <th style="width: 30%;" class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 text-center">수신 시간</th>
              <th style="width: 10%;" class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 text-center">상태</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(noti, idx) in notifications" :key="noti.id ?? idx">
              <td class="text-xs font-weight-bold mb-0 text-trunate ps-4">{{ typeLabel(noti.pushType) }}</td>
              <td class="text-sm">
                <p class="text-xs font-weight-bold mb-0 text-truncate ps-3">{{ noti.title }}</p>
                <p class="text-xs text-secondary mb-0 text-truncate ps-3">{{ noti.message }}</p>
              </td>
              <td class="align-middle text-center">
                <span class="text-secondary text-xs font-weight-bold">{{ formatDate(noti.createdAt) }}</span>
              </td>
              <td class="align-middle text-center text-sm">
                <span :class="['badge badge-sm', noti.isRead ? 'bg-gradient-secondary' : 'bg-gradient-success']">
                  {{ noti.isRead ? '읽음' : '안읽음' }}
                </span>
              </td>
            </tr>
            <tr v-if="!notifications || notifications.length === 0">
              <td colspan="4" class="text-center text-secondary text-xs py-4">표시할 알림이 없습니다.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<style scoped>
.text-truncate { white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
</style>
