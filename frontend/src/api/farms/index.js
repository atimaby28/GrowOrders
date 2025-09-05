import axios_api from '@/plugins/axiosinterceptor'

// 농장 등록
const farmRegister = async (farmRegisterForm) => {
  let url = '/farms/register'

 const { dto } = farmRegisterForm

  // FormData 구성
  const fd = new FormData()
  fd.append('dto', new Blob([JSON.stringify(dto)], { type: 'application/json' })) // 키 이름 반드시 'dto'
  
  try {
    // Content-Type 설정하지 말고 그대로 보내기 (axios가 boundary 포함해서 자동 설정)
    const res = await axios_api.post(url, fd)
    return res.data
  } catch (err) {
    // error.response가 없는 경우 대비
    if (err?.response?.data) return err.response.data
    throw err
  }
}

export default { farmRegister }
