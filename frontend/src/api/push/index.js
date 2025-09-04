import api from "@/plugins/axiosinterceptor";

const notification = async () => {
  const res = await api.get('/push/history');   
  return res.data.data;                      
};


export default { notification }