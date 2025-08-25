<script setup>
<<<<<<< HEAD
// import { useRouter } from 'vue-router'
=======
/* ----------------- 📦 Imports ----------------- */
import { onMounted, computed, reactive } from "vue";
>>>>>>> refs/remotes/origin/main
import MiniStatisticsCard from "@/examples/Cards/MiniStatisticsCard.vue";
import Carousel from "./components/Carousel.vue";
import CategoriesList from "./components/CategoriesList.vue";
<<<<<<< HEAD
// import US from "@/assets/img/orderlabs/girl.png";
// import DE from "@/assets/img/orderlabs/boy.png";
// import GB from "@/assets/img/orderlabs/man.png";
// import BR from "@/assets/img/orderlabs/woman.png";

=======
>>>>>>> refs/remotes/origin/main
import ProjectCard from "./components/ProjectCard.vue";
import api from "@/api/dashboard";

/* ----------------- 🖼 Assets ----------------- */
import logoXD from "@/assets/img/orderlabs/sun.png";
import logoAtlassian from "@/assets/img/orderlabs/icon-sun-cloud.png";
import logoSlack from "@/assets/img/orderlabs/temperature.png";
import logoSpotify from "@/assets/img/orderlabs/product.png";
import logoJira from "@/assets/img/orderlabs/humidity.png";
import logoInvision from "@/assets/img/orderlabs/co2.png";

import team1 from "@/assets/img/team-1.jpg";
import team2 from "@/assets/img/team-2.jpg";
import team3 from "@/assets/img/team-3.jpg";
import team4 from "@/assets/img/team-4.jpg";

/* ----------------- 📊 State ----------------- */
const state = reactive({
  summaryData: [],
  farmMonitoringData: { farm_monitoring: [] },
  chartData: [],
  farmStatus: {},
  orderList: [],
  weatherData: {}
});

/* ----------------- 📈 Constants ----------------- */
const defaultLogos = [
  logoXD, logoAtlassian, logoSlack, logoSpotify, logoJira, logoInvision,
];

const defaultTeams = [
  [team1, team2, team3, team4],
  [team1, team2],
  [team2, team4],
  [team1, team2, team3, team4],
  [team1],
  [team1, team4],
];

/* ----------------- 🧮 Computed ----------------- */
const rows = computed(() => {
  const wd = state.weatherData || {};

  return [
    {
      logo: defaultLogos[0],
      tool: "풍속 (m/s)",
      teamMembers: defaultTeams[0],
      price: wd.ws ?? "-",
      progress: Math.min(parseInt(wd.ws || 0), 100), // progress는 %라 적당히 변환
    },
    {
      logo: defaultLogos[1],
      tool: "기온 (℃)",
      teamMembers: defaultTeams[1],
      price: wd.ta ?? "-",
      progress: Math.min(parseInt(wd.ta || 0), 100),
    },
    {
      logo: defaultLogos[2],
      tool: "습도 (%)",
      teamMembers: defaultTeams[2],
      price: wd.hm ?? "-",
      progress: Math.min(parseInt(wd.hm || 0), 100),
    },
    {
      logo: defaultLogos[3],
      tool: "강수량 (mm)",
      teamMembers: defaultTeams[3],
      price: wd.rn ?? "-",
      progress: 0, // 음수(-9) 들어오는 경우는 "데이터 없음"으로 보고 0 처리
    },
    {
      logo: defaultLogos[4],
      tool: "일사량 (MJ/m²)",
      teamMembers: defaultTeams[4],
      price: wd.si ?? "-",
      progress: Math.min(parseInt(wd.si * 10 || 0), 100), // 일사량을 %로 스케일링
    },
  ];
});

const cardDescription = computed(() => {
  const date = state.farmMonitoringData.date || "";
  return `<i class="fa fa-check text-info"></i>
          <span class="font-weight-bold ms-1">농장 지표</span> ${date}`;
});

const top4Orders = computed(() => {
  return [...(state.orderList || []).slice(-4)].reverse();
});

/* ----------------- 🔧 Utils ----------------- */
const getImageUrl = (imgName) => {
  try {
    return require(`@/assets/img/orderlabs/${imgName}`);
  } catch (e) {
    console.error("이미지 로드 실패:", imgName);
    return require("@/assets/img/orderlabs/boy.png"); // fallback
  }
};

/* ----------------- 🚀 Methods ----------------- */
const fetchData = async () => {
  try {
    const [summary, farmStatus, chart, status, orders, weather] = await Promise.all([
      api.dashboardNav(),
      api.farmStatus(),
      api.chartData(),
      api.farmStatus(),
      api.orderList(),
      await api.weatherData()
    ]);

<<<<<<< HEAD
onMounted(async () => {
  await getOrderList();
  await getData();

});
=======
    state.summaryData = summary?.summary ?? [];
    state.farmMonitoringData = farmStatus ?? {};
    state.chartData = chart ?? [];
    state.farmStatus = status ?? {};
    state.orderList = orders ?? [];
    state.weatherData = weather ?? {};
  } catch (error) {
    console.error("데이터 로딩 실패:", error);
  }
};
>>>>>>> refs/remotes/origin/main

/* ----------------- ⏳ Lifecycle ----------------- */
onMounted(fetchData);
</script>

<template>
  <div class="py-4 container-fluid">
    <!-- 요약 카드 -->
    <div class="row">
<<<<<<< HEAD
      <div class="col-lg-12">
        <div class="row">
          <div
            v-for="(data, index) in state.summaryData"
            :key="index"
            class="col-lg-3 col-md-6 col-12"
          >
            <mini-statistics-card
              :title="data.title"
              :value="data.value"
              :description="`
                <span
                  class='text-sm font-weight-bolder ${
                    data.change.trend === 'up' ? 'text-success' : 'text-danger'
                  }'>
                  ${data.change.trend === 'up' ? '+' : '-'}${data.change.percentage}%
                </span> ${data.change.text}
              `"
              :icon="data.icon"
            />
            <PushClientSave />
          </div>
          <!-- <div class="col-lg-3 col-md-6 col-12">
            <mini-statistics-card
              title="예상 수익금"
              value="$53,000"
              description="<span
                class='text-sm font-weight-bolder text-success'
                >+55%</span> since yesterday"
              :icon="{
                component: 'ni ni-money-coins',
                background: 'bg-gradient-primary',
                shape: 'rounded-circle',
              }"
            />
          </div>
          <div class="col-lg-3 col-md-6 col-12">
            <mini-statistics-card
              title="예상 생산량"
              value="2,300"
              description="<span
                class='text-sm font-weight-bolder text-success'
                >+3%</span> since last week"
              :icon="{
                component: 'ni ni-world',
                background: 'bg-gradient-danger',
                shape: 'rounded-circle',
              }"
            />
          </div>
          <div class="col-lg-3 col-md-6 col-12">
            <mini-statistics-card
              title="요청 생산량"
              value="+3,462"
              description="<span
                class='text-sm font-weight-bolder text-danger'
                >-2%</span> since last quarter"
              :icon="{
                component: 'ni ni-paper-diploma',
                background: 'bg-gradient-success',
                shape: 'rounded-circle',
              }"
            />
          </div>
          <div class="col-lg-3 col-md-6 col-12">
            <mini-statistics-card
              title="판매량"
              value="$103,430"
              description="<span
                class='text-sm font-weight-bolder text-success'
                >+5%</span> than last month"
              :icon="{
                component: 'ni ni-cart',
                background: 'bg-gradient-warning',
                shape: 'rounded-circle',
              }"
            />
          </div> -->
        </div>
        <div class="row">
          <div class="col-lg-7 mb-lg">
            <!-- line chart -->
            <div class="card">
              <div class="p-3 pb-0 card-header">
                <div class="d-flex justify-content-between">
                  <h6 class="mb-2">최근 주문자 정보</h6>
                </div>
              </div>
              <div class="p-3 pb-0 table-responsive">
                <table class="table align-items-center">
                  <tbody>
                    <tr v-for="(order, index) in top4Orders" :key="index">
                      <td class="w-30">
                        <div class="px-2 py-1 d-flex align-items-center">
                          <div>
                            <img
                              :src="getImageUrl(order.img)"
                              alt="Profile Img"
                              style="width: 40px; height: 40px; object-fit: cover;"
                            />
                          </div>
                          <div class="ms-4">
                            <p class="mb-0 text-xs font-weight-bold">구매자:</p>
                            <h6 class="mb-0 text-sm">{{ order.name }}</h6>
                          </div>
                        </div>
                      </td>
                      <td>
                        <div class="text-center">
                          <p class="mb-0 text-xs font-weight-bold">판매품목:</p>
                          <h6 class="mb-0 text-sm">{{ order.crop }}</h6>
                        </div>
                      </td>
                      <td>
                        <div class="text-center">
                          <p class="mb-0 text-xs font-weight-bold">총 주문금액:</p>
                          <h6 class="mb-0 text-sm">{{ order.total }}</h6>
                        </div>
                      </td>
                      <td class="text-sm align-middle">
                        <div class="text-center col">
                          <p class="mb-0 text-xs font-weight-bold">주문량:</p>
                          <h6 class="mb-0 text-sm">{{ order.quantity }}</h6>
                        </div>
                      </td>
                      <!-- 버튼용 td 추가 -->
                      <td class="text-end">
                        <router-link
                          :to="{ name: 'OrderDetail', params: { orderId: order.orderId } }"
                          class="btn btn-link btn-icon-only btn-rounded btn-sm text-dark icon-move-right my-auto"
                          style="text-decoration: none;"
                        >
                          <i
                            :class="`ni ${isRTL ? 'ni-bold-left' : 'ni-bold-right'}`"
                            aria-hidden="true"
                          ></i>
                        </router-link>
                      </td>
                    </tr>
                  </tbody>
=======
      <div
        v-for="(data, index) in state.summaryData"
        :key="index"
        class="col-lg-3 col-md-6 col-12"
      >
        <mini-statistics-card
          :title="data.title"
          :value="data.value"
          :description="`
            <span
              class='text-sm font-weight-bolder ${
                data.change.trend === 'up' ? 'text-success' : 'text-danger'
              }'>
              ${data.change.trend === 'up' ? '+' : '-'}${data.change.percentage}%
            </span> ${data.change.text}
          `"
          :icon="data.icon"
        />
      </div>
    </div>
>>>>>>> refs/remotes/origin/main

    <!-- 주문자 정보 & 카테고리 -->
    <div class="row">
      <!-- 최근 주문자 -->
      <div class="col-lg-7 mb-lg">
        <div class="card">
          <div class="p-3 pb-0 card-header">
            <h6 class="mb-2">최근 주문자 정보</h6>
          </div>
          <div class="p-3 pb-0 table-responsive">
            <table class="table align-items-center">
              <tbody>
                <tr v-for="(order, index) in top4Orders" :key="index">
                  <td class="w-30">
                    <div class="px-2 py-1 d-flex align-items-center">
                      <img
                        :src="getImageUrl(order.img)"
                        alt="Profile Img"
                        style="width: 40px; height: 40px; object-fit: cover;"
                      />
                      <div class="ms-4">
                        <p class="mb-0 text-xs font-weight-bold">구매자:</p>
                        <h6 class="mb-0 text-sm">{{ order.name }}</h6>
                      </div>
                    </div>
                  </td>
                  <td class="text-center">
                    <p class="mb-0 text-xs font-weight-bold">판매품목:</p>
                    <h6 class="mb-0 text-sm">{{ order.crop }}</h6>
                  </td>
                  <td class="text-center">
                    <p class="mb-0 text-xs font-weight-bold">총 주문금액:</p>
                    <h6 class="mb-0 text-sm">{{ order.total }}</h6>
                  </td>
                  <td class="text-center">
                    <p class="mb-0 text-xs font-weight-bold">주문량:</p>
                    <h6 class="mb-0 text-sm">{{ order.quantity }}</h6>
                  </td>
                  <td class="text-end">
                    <router-link
                      :to="{ name: 'OrderDetail', params: { orderId: order.orderId } }"
                      class="btn btn-link btn-icon-only btn-rounded btn-sm text-dark"
                    >
                      <i class="ni ni-bold-right" aria-hidden="true"></i>
                    </router-link>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 카테고리 리스트 -->
      <div class="col-lg-5">
        <categories-list
          :categories="[
            {
              icon: { component: 'ni ni-mobile-button', background: 'dark' },
              label: '판매량 내역',
              description: '250 in stock <strong>346+ sold</strong>',
            },
            {
              icon: { component: 'ni ni-tag', background: 'dark' },
              label: '이벤트',
              description: '123 closed <strong>15 open</strong>',
            },
            {
              icon: { component: 'ni ni-box-2', background: 'dark' },
              label: '배송 현황',
              description: '1 is active <strong>40 closed</strong>',
            },
            {
              icon: { component: 'ni ni-satisfied', background: 'dark' },
              label: '새로운 알림',
              description: '+ 430',
            },
          ]"
        />
      </div>
    </div>

    <!-- 농장 모니터링 & 차트 -->
    <div class="row mt-4">
      <div class="col-lg-7 mb-lg-0 mb-4">
        <project-card
          title="농장 모니터링"
          :description="cardDescription"
          :headings="['상태', '관리자', '지표', '상태']"
          :rows="rows"
        />
      </div>
      <div class="col-lg-5">
        <carousel :chartsData="state.chartData" />
      </div>
    </div>
  </div>
</template>
