import axios_api from '@/plugins/axiosinterceptor'

//재고 데이터 가져오기
// const getInventoryAPI = async (request) => {
const getInventory = async (id) => {
  let data = {}
  let url = '/inventories/list'

  await axios_api
    .get(url, {
      params: {
        farmId: id,
      },
    })
    .then((response) => {
      data = response.data
    })
    .catch((error) => {
      data = error.data
    })

  return data
}

export default { getInventory }
