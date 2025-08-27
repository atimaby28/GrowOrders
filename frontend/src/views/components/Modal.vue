<script setup>
import api from '@/api/inventory'
import { reactive, watch } from 'vue'

const props = defineProps(['inventoryId'])

//화면에 그릴 재고 상세 데이터
let inventory = reactive({
  id: null, //재고 ID 및 작물 ID
  cropType: '', //작물 종류
  area: null, //재배 면적,
  cultivateType: '', //재배 방식
  expectedHarvestDate: '', //예상 수확일
  expectedQuantity: null, //예상 수확량
  maxExpectedQuantity: null, //최대 예상 수확량
  sowingStartDate: '', //파종 시작일
})

//재고 수정 요청 데이터
const inventoryEditForm = reactive({
  cropId: null,
})

//수정 버튼 누르면, 수정 api 호출
const updateInventory = async () => {
  Object.assign(inventoryEditForm, inventory) //reactive 객체 내부 값 업데이트
  inventoryEditForm.cropId = props.inventoryId

  const data = await api.updateInventory(inventoryEditForm)
  if (data && data.success) {
    //다시 목록으로 돌아가도록 redirect
    console.log('수정 성공')
  } else {
    alert('재고를 수정하지 못하였습니다.')
  }
}

//재고 상세 조회 api 호출
const getInventoryDetail = async () => {
  const data = await api.getInventoryDetail(props.inventoryId)

  if (data && data.success) {
    Object.assign(inventory, data.data)
  } else {
    alert('데이터를 불러오지 못하였습니다.')
  }
}

//선택한 inventoryId 값이 바뀔 때마다 호출
watch(
  () => props.inventoryId,
  (newInventoryId) => {
    getInventoryDetail(newInventoryId)
  },
)
</script>

<template>
  <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">재고 수정</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body container-fluid py-4">
          <div class="row">
            <div class="col-md-12">
              <div class="card">
                <div class="card-body">
                  <p class="text-uppercase text-sm">재고 정보</p>
                  <div class="row">
                    <div class="col-md-6">
                      <div class="form-group">
                        <label for="example-text-input" class="form-control-label">재고 ID</label>
                        <input class="form-control" type="text" :placeholder="inventory.id" disabled />
                      </div>
                    </div>
                    <div class="col-md-6">
                      <div class="form-group">
                        <label for="example-text-input" class="form-control-label">작물 종류</label>
                        <input class="form-control" type="text" :placeholder="inventory.type" disabled />
                      </div>
                    </div>
                    <div class="col-md-6">
                      <div class="form-group">
                        <label for="example-text-input" class="form-control-label">재배 면적</label>
                        <div class="input-group">
                          <input class="form-control" type="text" v-model="inventory.area" />
                          <span class="input-group-text form-control-20">/㎡</span>
                        </div>
                      </div>
                    </div>
                    <div class="col-md-6">
                      <div class="form-group">
                        <label for="example-text-input" class="form-control-label">재배 방식</label>
                        <input class="form-control" type="text" :placeholder="inventory.cultivateType" disabled />
                      </div>
                    </div>
                    <div class="col-md-6">
                      <div class="form-group">
                        <label for="example-text-input" class="form-control-label">파종 시작일</label>
                        <input class="form-control" type="date" v-model="inventory.sowingStartDate" />
                      </div>
                    </div>

                    <div class="col-md-6">
                      <div class="form-group">
                        <label for="example-text-input" class="form-control-label">예측 수확일</label>
                        <input class="form-control" type="date" v-model="inventory.expectedHarvestDate" />
                      </div>
                    </div>
                    <div class="col-md-6">
                      <div class="form-group">
                        <label for="example-text-input" class="form-control-label">예측 산출량</label>
                        <div class="input-group">
                          <input class="form-control" type="text" v-model="inventory.expectedQuantity" />
                          <span class="input-group-text form-control-20">/㎡</span>
                        </div>
                      </div>
                    </div>
                    <div class="col-md-6">
                      <div class="form-group">
                        <label for="example-text-input" class="form-control-label">최대 예측 산출량</label>
                        <div class="input-group">
                          <input class="form-control" type="text" v-model="inventory.maxExpectedQuantity" />
                          <span class="input-group-text form-control-20">/㎡</span>
                        </div>
                      </div>
                    </div>
                  </div>

                  <hr class="horizontal dark" />
                  <p class="text-uppercase text-sm">추가 정보</p>
                  <div class="row">
                    <div class="col-md-12">
                      <div class="form-group">
                        <label for="example-text-input" class="form-control-label">메모</label>
                        <input class="form-control" type="text" placeholder="재고에 관련하여 필요한 메모 작성하세요." />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
          <button type="button" class="btn btn-primary" @click="updateInventory()">저장</button>
        </div>
      </div>
    </div>
  </div>
</template>
