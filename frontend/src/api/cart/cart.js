import api from "@/plugins/axiosinterceptor";

// 장바구니 담기
const addToCart = async ({ cropMgtId, quantity }) => {
  let data = {};
  const url = `http://localhost:8080/cart/add/${cropMgtId}`;

  await api.post(url, { quantity })
    .then((res) => {
      data = res;
      console.log("장바구니 추가 성공:", res.data);
    })
    .catch((error) => {
      data = error.response || error;
      console.error("장바구니 추가 실패:", error);
    });

  return data;
};

// 내 장바구니 목록 조회
const getMyCarts = async (userId) => {
  let data = {};
  const url = `http://localhost:8080/cart/${userId}`;

  await api.get(url)
    .then((res) => {
      data = res.data;
      console.log("장바구니 조회 성공:", data);
    })
    .catch((error) => {
      data = error.response || error;
      console.error("장바구니 조회 실패:", error);
    });

  return data;
};

// 👉 default 말고 named export
export { addToCart, getMyCarts };