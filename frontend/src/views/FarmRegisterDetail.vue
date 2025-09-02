<script setup>
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import setNavPills from '@/assets/js/nav-pills.js'
import setTooltip from '@/assets/js/tooltip.js'
import FarmHeader from '@/views/components/farm/FarmHeader.vue'
import FarmForm from '@/views/components/farm/FarmForm.vue'
import FarmImageUploader from '@/views/components/farm/FarmImageUploader.vue'
import { useFarm } from "@/views/components/farm/useFarm.js"

const route = useRoute()
const router = useRouter()
const { farm, loading, error, loadFarm, canEdit } = useFarm()

onMounted(() => loadFarm(route.params.id))

function goEdit() {
  router.push({ name: 'FarmEdit', params: { id: farm.value.id } })
}

onMounted(() => { setNavPills(); setTooltip(); })
</script>

<template>
  <main>
    <div class="container-fluid">
      <div class="page-header min-height-300" :style="{ backgroundImage: `url(${require('@/assets/img/farm_register.jpg')})`, marginRight: '-24px', marginLeft: '-25%' }">
        <span class="mask bg-gradient-success opacity-6"></span>
      </div>
      <div class="card shadow-lg mt-n6"></div>
    </div>

    <div class="py-4 container-fluid">
      <div v-if="loading">로딩...</div>
      <div v-else-if="error">불러오기 실패</div>
      <div v-else-if="farm" class="row">
        <div class="col-md-8">
          <div class="card">
            <FarmHeader mode="detail" :canEdit="canEdit" @edit="goEdit" />
            <div class="card-body">
              <FarmForm
                :readOnly="true"
                v-model:farmName="farm.name"
                v-model:selectedLocation="farm.region"
                v-model:addressDetail="farm.address"
                v-model:area="farm.size"
                v-model:description="farm.contents"
                :locationOptions="[]"
              />
              <div class="row"><div class="col-md-12">
                <FarmImageUploader :disabled="true" :initialUrl="farm.imageUrl" />
              </div></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>
