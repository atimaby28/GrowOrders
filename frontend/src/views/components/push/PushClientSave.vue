<script setup>
// [푸시 알림] 클라이언트 endpoints, keys 디비에 저장 
import axios from 'axios'

const VAPID_PUBLIC_KEY =
  'BLaZmJ4WyIM5yOg3-xMWtvsLEvKQGFM5OYog7hwniwpLKQifl8hVof-knJsFSTwGcpCFrDmeqNMlE3yNgO122gc'

onMounted(async () => {
  const permission = await Notification.requestPermission()
  if (permission !== 'granted') {
    console.log('권한 없음')
    return
  }

  const registration = await navigator.serviceWorker.register('/service-worker.js')

  let subscription = await registration.pushManager.getSubscription()
  if (!subscription) {
    subscription = await registration.pushManager.subscribe({
      userVisibleOnly: true,
      applicationServerKey: VAPID_PUBLIC_KEY,
    })
  }  
  console.log(JSON.stringify(subscription))
  await axios.post('http://localhost:8080/push/sub', subscription)  // WAS IP주소
})
</script>