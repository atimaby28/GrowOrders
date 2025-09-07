self.addEventListener('push', (event) => {
  let data = {};
  if (event.data) {
    try { data = event.data.json(); }
    catch { data = { body: event.data.text() }; }
  }

  const title = data.title || 'GrowOrders';
  const body  = (data.body ?? data.message) || '새 알림이 도착했습니다.';
  event.waitUntil(self.registration.showNotification(title, { body }));
});
