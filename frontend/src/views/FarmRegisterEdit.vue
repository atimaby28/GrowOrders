<script setup>
import { ref, watchEffect, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useFarm } from "@/views/components/farm/useFarm.js"
import axios from '@/plugins/axiosinterceptor.js'
import FarmHeader from '@/views/components/farm/FarmHeader.vue'
import FarmForm from '@/views/components/farm/FarmForm.vue'
import FarmImageUploader from '@/views/components/farm/FarmImageUploader.vue'

const { farm, loading, error, loadFarm, canEdit } = useFarm()
const route = useRoute()
const router = useRouter()

const farmName = ref('')
const selectedLocation = ref('')
const addressDetail = ref('')
const area = ref('')
const description = ref('')
const farmImage = ref('')

onMounted(async () => {
  await loadFarm(route.params.id)
  if (!canEdit.value) return router.replace({ name: 'FarmDetail', params: { id: route.params.id } })
})

watchEffect(() => {
  if (farm.value) {
    farmName.value = farm.value.name ?? ''
    selectedLocation.value = farm.value.region ?? ''
    addressDetail.value = farm.value.address ?? ''
    area.value = farm.value.size ?? ''
    description.value = farm.value.contents ?? ''
    farmImage.value = farm.value.farmImage ?? ''
  }
})

async function handleUpdate() {
  const dto = {
    name: farmName.value,
    region: selectedLocation.value,
    address: addressDetail.value,
    size: area.value ? Number(area.value) : null,
    contents: description.value,
    farmImage: farmImage.value,
  }
  const fd = new FormData()
  fd.append('dto', new Blob([JSON.stringify(dto)], { type: 'application/json' }))

  await axios.post(`/farms/${farm.value.id}`, fd)
  alert('농장 정보가 저장되었습니다.')
  router.push({ name: 'FarmDetail', params: { id: farm.value.id } })
}
</script>

<template>
  <main>
    <div class="py-4 container-fluid">
      <div v-if="loading">로딩...</div>
      <div v-else-if="error">불러오기 실패</div>
      <div v-else-if="farm" class="row">
        <div class="col-md-8">
          <div class="card">
            <FarmHeader mode="edit" :canEdit="true" @submit="handleUpdate" />
            <div class="card-body">
              <FarmForm
                :readOnly="false"
                v-model:farmName="farmName"
                v-model:selectedLocation="selectedLocation"
                v-model:addressDetail="addressDetail"
                v-model:area="area"
                v-model:description="description"
                v-model:farmImage="farmImage"
                :locationOptions="[farm.region].filter(Boolean)"/>
              <div class="row"><div class="col-md-12">
                <FarmImageUploader v-model:file="profileFile" :disabled="false" :initialUrl="farm.imageUrl" />
              </div></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>