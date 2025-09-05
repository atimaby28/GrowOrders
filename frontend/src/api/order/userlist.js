import api from "@/plugins/axiosinterceptor";

// 백엔드 호출 후 받은 데이터를 반환



const farmerList = async (req = {}) => {
    const params = {
    page: Number(req.page ?? 1), 
    size: Number(req.size ?? 10),
    };

    const { data } = await api.get("http://localhost:8080/order/listFarmer", {
    params,
    withCredentials: true,
    });
    return data; 
};


// const buyerList = async (req = {}) => {
//     const params = {
//     page: Number(req.page ?? 1), 
//     size: Number(req.size ?? 10),
//     };

//     const { data } = await api.get('http://localhost:8080/order/listBuyer', {
//     params,
//     withCredentials: true
//     });
//     return data; 
// };

const buyerList = async (req = {}) => {
    const params = {
    page: Number(req.page ?? 1),
    size: Number(req.size ?? 10),
    };

  // baseURL이 interceptor에 있으면 그냥 '/order/listBuyer'
    const { data } = await api.get("/order/listBuyer", { params });

  // BaseResponse { success, message, data } 구조 → 안쪽 data만 꺼내서 반환
    return data?.data ?? data;
};

const farmList = async () => {
    let data = {};
    let url = '/api/FarmList.json';

    await api.get(url)
        .then((res) => {
            data = res.data;
        })
        .catch((error) => {
            data = error.data;
        });

    return data;
}

export default { farmerList, buyerList, farmList };