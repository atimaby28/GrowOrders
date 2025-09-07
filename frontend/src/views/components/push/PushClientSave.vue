<script setup>
// [푸시 알림] 클라이언트 endpoints, keys 디비에 저장 
import { onMounted } from "vue";
import axios from 'axios'

const VAPID_PUBLIC_KEY =
  'BNRLXh3-LRDmepqNE_cSsZv5FT41b-izqF-qJRo1ACs8qYrFs8_xshBchv06u3FDT3ingwAcn46EZf7kzw31Rts'

function urlBase64ToUint8Array(base64) {
  const padding = '='.repeat((4 - base64.length % 4) % 4);
  const b64 = (base64 + padding).replace(/-/g, '+').replace(/_/g, '/');
  const raw = atob(b64);
  const out = new Uint8Array(raw.length);
  for (let i = 0; i < raw.length; ++i) out[i] = raw.charCodeAt(i);
  return out;
}

onMounted(async () => {
  const permission = await Notification.requestPermission();
  if (permission !== 'granted') return;

  const registration = await navigator.serviceWorker.register('/service-worker.js');

  let subscription = await registration.pushManager.getSubscription();
  if (!subscription) {
    subscription = await registration.pushManager.subscribe({
      userVisibleOnly: true,
      applicationServerKey: urlBase64ToUint8Array(VAPID_PUBLIC_KEY),
    });
  }

  await axios.post('http://localhost:8080/push/sub', subscription, {
    withCredentials: true,
  });
})
</script>