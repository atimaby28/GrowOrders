import api from "@/plugins/axiosinterceptor";

const signUp = async (req) => {
    let data = {};
    let url = '/users/signup';

    await api.post(url, req)
        .then((res) => {
            data = res.data;
        })
        .catch((error) => {
            data = error.response?.data || error.message;
        });
    return data;
};

const signIn = async (req) => {
    let data = {};
    let url = '/login';
    
    await api.post(url, req)
        .then((res) => {
            data = res.data;
        })
        .catch((error) => {
            data = error.response?.data || error.message;
        });
    return data;
};



export default { signUp, signIn };