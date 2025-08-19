import axios_api from '@/plugins/axiosinterceptor'

//작물 등록
const cropRegister = async (cropRegisterForm) => {
  let data = {}
  let url = '/crops/register'

  await axios_api
    .post(url, cropRegisterForm)
    .then((response) => {
      data = response.data
    })
    .catch((error) => {
      data = error.response.data
    })

  return data
}

export default { cropRegister }
