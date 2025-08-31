import api from "@/plugins/axiosinterceptor";

// 백엔드 호출 후 받은 데이터를 반환
// const farmerList = async (req) => {
//     let data = {};
//     let url = '/api/FarmerList.json';

//     await api.get(url, req)
//         .then((res) => {
//             data = res.data;
//         })
//         .catch((error) => {
//             data = error.data;
//         });

//     return data;
// }

// const buyerList = async (req) => {
//     let data = {};
//     let url = '/api/BuyerList.json';

//     await api.get(url, req)
//         .then((res) => {
//             data = res.data;
//         })
//         .catch((error) => {
//             data = error.data;
//         });

//     return data;
// }

// const orderList = async () => {
//     let data = {};
//     let url = '/api/orderList.json';

//     await api.get(url)
//         .then((res) => {
//             data = res.data;
//         })
//         .catch((error) => {
//             data = error.data;
//         });

//     return data;
// }

const farmList = async () => {
    let data = {};
    let url = '/api/farmList.json';

    await api.get(url)
        .then((res) => {
            data = res.data;
        })
        .catch((error) => {
            data = error.data;
        });

    return data;
}

const orderList = async () => {
    let data = {};
    let url = 'http://localhost:8080/order/listBuyer?page=1&size=1';

    await api.get(url)
        .then((res) => {
            data = res.data;
                    console.log(data);
        })
        .catch((error) => {
            data = error.data;
        });

    return data;
}

const orderListOne = async (orderId) => {
    let data = {};
    let url = 'http://localhost:8080/order/readCreate/'+orderId;

    await api.get(url)
        .then((res) => {
            data = res.data;
                    console.log(data);
        })
        .catch((error) => {
            data = error.data;
        });

    return data;
}


export default { orderList, farmList, orderListOne };