import api from "@/plugins/axiosinterceptor";

const farmList = async () => {
  let data = [];
  let url = "/farms/list";

  await api
    .get(url)
    .then((res) => {
      data = res.data?.data ?? res.data;
    })
    .catch((error) => {
      data = error.data;
    });

  return data;
};

const orderList = async () => {
  let data = {};
  let url = "http://localhost:8080/order/listBuyer?page=1&size=1";

  await api
    .get(url)
    .then((res) => {
      data = res.data;
    })
    .catch((error) => {
      data = error.data;
    });

  return data;
};

const orderListOne = async (orderId) => {
  let data = {};
  let url = "http://localhost:8080/order/readCreate/" + orderId;

  await api
    .get(url)
    .then((res) => {
      data = res.data;
    })
    .catch((error) => {
      data = error.data;
    });

  return data;
};

export default { orderList, farmList, orderListOne };
