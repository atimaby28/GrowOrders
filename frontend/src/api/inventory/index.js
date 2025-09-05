import axios_api from "@/plugins/axiosinterceptor";

//재고 목록 가져오기
// const getInventoryAPI = async (request) => {
const getInventory = async (id) => {
  let data = {};
  let url = "/inventories/list";

  await axios_api
    .get(url, {
      params: {
        farmId: id,
      },
    })
    .then((response) => {
      data = response.data;
    })
    .catch((error) => {
      data = error.data;
    });

  return data;
};

//재고 상세 가져오기
const getInventoryDetail = async (inventoryId) => {
  let data = {};
  let url = "/inventories/details";

  await axios_api
    .get(url, {
      params: {
        cropId: inventoryId,
      },
    })
    .then((response) => {
      data = response.data;
    })
    .catch((error) => {
      data = error.data;
    });
  return data;
};

//재고 수정
const updateInventory = async (inventoryEditForm) => {
  let data = {};
  let url = "/inventories/update";

  await axios_api
    .post(url, inventoryEditForm)
    .then((response) => {
      data = response.data;
    })
    .catch((error) => {
      data = error.data;
    });

  return data;
};

//재고 검색
const searchInventory = async (farmId, searchFrom) => {
  let data = {};
  let url = "/inventories/search";

  await axios_api
    .get(url, {
      params: {
        farmId: farmId,
        type: searchFrom.type,
        status: searchFrom.status,
        saleStatus: searchFrom.saleStatus,
        order: searchFrom.order,
      },
    })
    .then((response) => {
      data = response.data;
    })
    .catch((error) => {
      data = error.data;
    });
  return data;
};

export default {
  getInventory,
  getInventoryDetail,
  updateInventory,
  searchInventory,
};
