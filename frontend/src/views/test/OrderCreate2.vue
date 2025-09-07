<script setup>
import { useRouter, useRoute } from "vue-router";
import { ref } from "vue";
import { addToCart } from "../../store/test/cart"; // ✅ named import

const router = useRouter();
const route = useRoute();

// 상품 관리 ID (CropOrderManagementId) - URL 파라미터에서 가져옴
const cropMgtId = Number(route.params.cropMgtId) || 1;
const quantity = ref(1);

// 장바구니 담기
const handleAddToCart = async () => {
  try {
    const res = await addToCart({ cropMgtId, quantity: quantity.value });

    let msg = "";
    if (typeof res.data === "string") {
      msg = res.data;
    } else {
      msg = res?.data?.message || "장바구니에 추가되었습니다.";
    }
    alert(msg);
  } catch (e) {
    console.error("장바구니 담기 실패:", e);
    alert("장바구니 담기 실패");
  }
};

// 주문 생성 (더미, 백엔드 호출 ❌)
const createOrder = () => {
  router.push("/order-create-complete");
};
</script>

<template>
  <main>
    <div class="container-fluid">
      <div
        class="page-header min-height-300"
        style="
          background-image: url('https://images.unsplash.com/photo-1531512073830-ba890ca4eba2?auto=format&fit=crop&w=1920&q=80');
          margin-right: -24px;
          margin-left: -34%;
        "
      >
        <span class="mask bg-gradient-success opacity-6"></span>
      </div>
      <div class="card shadow-lg mt-n6">
        <div class="card-body p-3">
          <div class="row gx-4">
            <div class="col-auto">
              <div class="avatar avatar-xl position-relative">
                <img
                  src="@/assets/img/team-1.jpg"
                  alt="profile_image"
                  class="w-100 border-radius-lg shadow-sm"
                />
              </div>
            </div>
            <div class="col-auto my-auto">
              <div class="h-100">
                <h5 class="mb-1">심시경</h5>
                <p class="mb-0 font-weight-bold text-sm">SSG1999@gmail.com</p>
              </div>
            </div>
            <div
              class="col-lg-4 col-md-6 my-sm-auto ms-sm-auto me-sm-0 mx-auto mt-1"
            >
              <div class="nav-wrapper position-relative end-0">
                <ul class="nav nav-pills nav-fill p-1" role="tablist">
                  <li class="nav-item">
                    <a
                      class="nav-link mb-0 px-0 py-1 active d-flex align-items-center justify-content-center"
                      href="javascript:;"
                      role="tab"
                      aria-selected="true"
                    >
                      <span class="ms-2">내정보</span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a
                      class="nav-link mb-0 px-0 py-1 d-flex align-items-center justify-content-center"
                      href="javascript:;"
                      role="tab"
                      aria-selected="false"
                    >
                      <span class="ms-2">주문목록</span>
                    </a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="container-fluid py-4">
      <div class="row">
        <div class="col-md-8">
          <div class="card">
            <div class="card-header pb-0">
              <div class="d-flex align-items-center">
                <p class="mb-0">주문생성</p>
                <div class="ms-auto">
                  <button
                    class="btn btn-info btn-sm me-2"
                    @click="handleAddToCart"
                  >
                    🛒 장바구니 담기
                  </button>
                  <button class="btn btn-primary btn-sm" @click="createOrder">
                    주문생성
                  </button>
                </div>
              </div>
            </div>
            <div class="card-body">
              <p class="text-uppercase text-sm">주문정보</p>
              <div class="row">
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="form-control-label">주문자 성명</label>
                    <input class="form-control" type="text" value="심시경" />
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="form-control-label">품목명</label>
                    <input class="form-control" type="text" value="토마토" />
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="form-control-label">단가</label>
                    <input class="form-control" type="text" value="5000원/kg" />
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="form-control-label">수량</label>
                    <input
                      class="form-control"
                      type="number"
                      v-model="quantity"
                      min="1"
                    />
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label class="form-control-label">총합</label>
                    <input class="form-control" type="text" value="20000원" />
                  </div>
                </div>
                <div class="col-md-12">
                  <div class="form-group">
                    <label class="form-control-label">주소</label>
                    <input
                      class="form-control"
                      type="text"
                      value="경상북도 구미시 도봉로 5길 21-높은유통"
                    />
                  </div>
                </div>
              </div>
              <hr class="horizontal dark" />
              <p class="text-uppercase text-sm">요구사항</p>
              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label class="form-control-label">주문 요청사항</label>
                    <input
                      class="form-control"
                      type="text"
                      value="내용물 안터지게 포장해주세요"
                    />
                  </div>
                </div>
                <div class="col-md-12">
                  <div class="form-group">
                    <label class="form-control-label">배달 요청사항</label>
                    <input
                      class="form-control"
                      type="text"
                      value="아기가 자고 있어요 초인종 누르지 말아주세요"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 판매자 프로필 카드 -->
        <div class="col-md-4">
          <div class="card card-profile">
            <img
              src="@/assets/img/bg-profile.jpg"
              alt="Image placeholder"
              class="card-img-top"
            />
            <div class="row justify-content-center">
              <div class="col-4 col-lg-4 order-lg-2">
                <div class="mt-n4 mt-lg-n6 mb-4 mb-lg-0">
                  <a href="javascript:;">
                    <img
                      src="@/assets/img/team-2.jpg"
                      class="rounded-circle img-fluid border border-2 border-white"
                    />
                  </a>
                </div>
              </div>
            </div>
            <div class="card-header text-center border-0 pt-0 pt-lg-2 pb-4 pb-lg-3">
              <div class="d-flex justify-content-between">
                <a href="javascript:;" class="btn btn-sm btn-info mb-0"
                  >전화번호</a
                >
                <a href="javascript:;" class="btn btn-sm btn-dark mb-0"
                  >메세지</a
                >
              </div>
            </div>
            <div class="card-body pt-0">
              <div class="text-center mt-4">
                <h5>김농사</h5>
                <div class="h6 font-weight-300">우리아기들농장</div>
                <div class="h6 mt-4">충청북도 증평군 증평읍 사곡리 1434</div>
                <div>사랑으로 열정으로 보답하겠습니다</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>