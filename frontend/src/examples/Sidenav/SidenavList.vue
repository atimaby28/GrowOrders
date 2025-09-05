<script setup>
import { computed, onMounted } from "vue";
import { useRoute } from "vue-router";
import { useUserStore } from "../../store/users/useUserStore.js";

import SidenavItem from "./SidenavItem.vue";

const userStore = useUserStore();
const route = useRoute();

onMounted(() => {
  userStore.checkLogin();
});

// 현재 라우트의 첫 번째 path segment
const currentRoute = computed(() => route.path.split("/")[1]);

// 네비게이션 항목 정의
const navItems = [
  {
    to: "/farmer/dashboard",
    label: "농부 메인 화면",
    icon: "ni ni-tv-2 text-primary text-sm opacity-10",
    role: ["FARMER"],
    active: "FarmerDashboard",
  },
  {
    to: "/buyer/dashboard",
    label: "주문자 메인 화면",
    icon: "ni ni-tv-2 text-primary text-sm opacity-10",
    role: ["BUYER"],
    active: "BuyerDashboard",
  },
  {
    to: "/buyerlist",
    label: "주문관리",
    icon: "ni ni-calendar-grid-58 text-warning text-sm opacity-10",
    role: ["FARMER"],
    active: "FarmerList",
  },
  {
    to: "/farms/list",
    label: "상품 검색",
    icon: "ni ni-basket text-success text-sm opacity-10",
    role: ["BUYER"],
    active: "FarmList",
  },
  {
    to: `/inventory?farmId=${userStore.user.ownedFarm?.[0]?.id}`,
    label: "재고관리",
    icon: "ni ni-calendar-grid-58 text-warning text-sm opacity-10",
    role: ["FARMER"], // 문자열 타입 role
    active: "inventory",
  },
  {
    to: "/farms/register",
    label: "농장등록",
    icon: "ni ni-credit-card text-success text-sm opacity-10",
    role: ["FARMER"],
    active: "Farm Register",
  },
  {
    to: "/crops/register",
    label: "생물등록",
    icon: "ni ni-app text-info text-sm opacity-10",
    role: ["FARMER"],
    active: "CropRegister",
  },
  {
    to: "/error",
    label: "배송관리",
    icon: "ni ni-world-2 text-danger text-sm opacity-10",
    active: "rtl-page",
  },
  {
    to: "/sales",
    label: "판매량 내역",
    icon: "ni ni-calendar-grid-58 text-warning text-sm opacity-10",
    role: ["FARMER"],
    active: "Sales",
  },
  {
    to: "/profile",
    label: "Profile",
    icon: "ni ni-single-02 text-dark text-sm opacity-10",
    active: "profile",
  },
];

// 현재 role에 맞는 항목 필터링 (getUserRole 사용)
const filteredNavItems = computed(() => {
  const role = userStore.getUserRole()?.toUpperCase() || null;
  return navItems.filter((item) => {
    if (!item.role) return true; // role 제한 없는 경우
    return item.role.includes(role);
  });
});
</script>

<template>
  <div
    class="collapse navbar-collapse w-auto h-auto h-100"
    id="sidenav-collapse-main"
  >
    <ul class="navbar-nav">
      <template v-for="(item, index) in filteredNavItems" :key="index">
        <!-- Profile 위에 ACCOUNT PAGES 추가 -->
        <li v-if="item.label === 'Profile'" class="mt-3 nav-item">
          <h6
            class="text-xs ps-4 text-uppercase font-weight-bolder opacity-6 ms-2"
          >
            ACCOUNT PAGES
          </h6>
        </li>

        <li class="nav-item">
          <sidenav-item
            :to="item.to"
            :class="currentRoute === item.active ? 'active' : ''"
            :navText="item.label"
          >
            <template v-slot:icon>
              <i :class="item.icon"></i>
            </template>
          </sidenav-item>
        </li>
      </template>
    </ul>
  </div>
</template>
