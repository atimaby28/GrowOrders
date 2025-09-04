import { ref, computed } from 'vue'
import axios from '@/plugins/axiosinterceptor.js'
import { useUserStore } from '@/store/users/useUserStore.js'

export function useFarm() {
  const farm = ref(null)
  const loading = ref(false)
  const error = ref(null)
  const userStore = useUserStore()

  const loadFarm = async (id) => {
    loading.value = true
    error.value = null
    try {
      const res = await axios.get(`/farms/${id}`)
      const data = res.data?.data ?? res.data
      farm.value = {
        id: data.id,
        name: data.name,
        region: data.region,
        address: data.address,
        size: data.size,
        contents: data.contents,
        userId: data.userId ?? data.user?.id ?? data.user_id,
        farmImage: data.farmImageUrl ?? data.farmImage,
        cropList: data.cropList,
      }
    } catch (e) {
      error.value = e
      farm.value = null
    } finally {
      loading.value = false
    }
  }

  const canEdit = computed(() => {
    const ownerId = farm.value?.userId
    const loginId = userStore.user?.id
    return !!(ownerId && loginId && ownerId === loginId)
  })

  return { farm, loading, error, loadFarm, canEdit }
}