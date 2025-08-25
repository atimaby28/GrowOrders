self.addEventListener('push', (event) => {
  event.waitUntil(self.registration.showNotification('알림 제목', { body: '알림 내용' }))
})
