import api from "@/plugins/axiosinterceptor";

// 백엔드 호출 후 받은 데이터를 반환
const orderList = async () => {
    let data = {};
    let url = '/dashboard/recent';

    await api.get(url)
        .then((res) => {
            data = res.data;
        })
        .catch((error) => {
            data = error.data;
        });

    return data;
}

const dashboardNav = async () => {
    let data = {};
    let url = '/dashboard/summary';

    await api.get(url)
        .then((res) => {
            data = res.data;
        })
        .catch((error) => {
            data = error.data;
        });

    return data;
}

const chartData = async () => {
    let data = {};
    let url = '/dashboard/charts';

    await api.get(url)
        .then((res) => {
            data = res.data;
        })
        .catch((error) => {
            data = error.data;
        });

    return data;
}

const weatherData = async () => {
    let data ={};
    let url = '/weather'

        await api.get(url)
        .then((res) => {
            data = res.data;
        })
        .catch((error) => {
            data = error.data;
        });

    return data;
}

export default { orderList, dashboardNav, chartData, weatherData }