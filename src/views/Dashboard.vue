<script setup>
/* ----------------- 📦 Imports ----------------- */
import { onMounted, computed, reactive } from "vue";
import MiniStatisticsCard from "@/examples/Cards/MiniStatisticsCard.vue";
import Carousel from "./components/Carousel.vue";
import CategoriesList from "./components/CategoriesList.vue";
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
  chartData: [],
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
      progress: Math.min(parseInt(wd.ws || 0), 100),
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
      progress: 0, // 음수(-9) 들어오는 경우는 데이터 없음으로 처리
    },
    {
      logo: defaultLogos[4],
      tool: "일사량 (MJ/m²)",
      teamMembers: defaultTeams[4],
      price: wd.si ?? "-",
      progress: Math.min(parseInt(wd.si * 10 || 0), 100),
    },
  ];
});

const cardDescription = computed(() => {
  return `<i class="fa fa-check text-info"></i>
          <span class="font-weight-bold ms-1">농장 지표</span>`;
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
    const [summary, chart, orders, weather] = await Promise.all([
      api.dashboardNav(),
      api.chartData(),
      api.orderList(),
      api.weatherData()
    ]);

    state.summaryData = summary?.data?.summary ?? [];
    state.chartData = chart?.data?.charts ?? [];
    state.orderList = orders.data.orders ?? [];
    state.weatherData = weather ?? {};

  } catch (error) {
    console.error("데이터 로딩 실패:", error);
  }
};



/* ----------------- ⏳ Lifecycle ----------------- */
onMounted(fetchData);

</script>

<template>
  <div class="py-4 container-fluid">
    <!-- 요약 카드 -->
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
      </div>
    </div>

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
                    <h6 class="mb-0 text-sm">{{ order.item }}</h6>
                  </td>
                  <td class="text-center">
                    <p class="mb-0 text-xs font-weight-bold">총 주문금액:</p>
                    <h6 class="mb-0 text-sm">{{ order.orderAmount }}</h6>
                  </td>
                  <td class="text-center">
                    <p class="mb-0 text-xs font-weight-bold">주문량:</p>
                    <h6 class="mb-0 text-sm">{{ order.itemsSold }}</h6>
                  </td>
                  <td class="text-end">
                    <router-link
                      :to="{ name: 'OrderDetail' }"
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
