import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "../store/users/useUserStore.js";

import FarmerDashboard from "../views/Dashboard.vue";
import BuyerDashboard from "../views/BuyerDashboard.vue";
import Tables from "../views/Tables.vue";
import VirtualReality from "../views/VirtualReality.vue";
import Profile from "../views/Profile.vue";
import Signup from "../views/Signup.vue";
import Signin from "../views/Signin.vue";

import BuyerList from "../views/order/BuyerList.vue";
import FarmList from "../views/order/FarmList.vue";
import FarmerList from "../views/order/FarmerList.vue";

import OrderDetail from "../views/order/OderDetail.vue";
import OrderCreate from "../views/order/OrderCreate.vue";
import OrderModify from "../views/order/OrderModify.vue";
import OrderModifyComplete from "../views/order/OrderModifyComplete.vue";
import OrderCreateComplete from "../views/order/OrderCreateComplete.vue";

import Error from "../views/Error.vue";

import CartView from "../views/test/CartView.vue";
import OrderView from "../views/test/OrderView.vue";
import PaymentView from "../views/test/PaymentView.vue";
import CropPage from "../views/test/CropPage.vue";

import OrderCreate2 from "../views/test/OrderCreate2.vue";
import OrderCart2 from "../views/test/OrderCart2.vue";
import CartView2 from "../views/test/CartView2.vue";

import Chat from "../views/Chat.vue";
import Cart from "../views/CartView.vue";
import OrderCartComplete from "../views/order/OrderCartComplete.vue";
import FarmDetail from "../views/FarmRegisterDetail.vue";
import FarmEdit from "../views/FarmRegisterEdit.vue";

// 생물 등록
import CropRegister from "../views/CropRegister.vue";
// 농장 등록
import FarmRegister from "../views/FarmRegister.vue";
// 판매량 조회
import Sales from "@/views/Sales.vue";
// 재고 관리
import Inventory from "@/views/Inventory.vue";
// 알림 히스토리
import Push from "@/views/Push.vue";

const routes = [
  {
    path: "/",
    name: "/",
    component: Signin,
  },
  {
    path: "/farmer/dashboard",
    name: "FarmerDashboard",
    component: FarmerDashboard,
  },
  {
    path: "/buyer/dashboard",
    name: "BuyerDashboard",
    component: BuyerDashboard,
  },
  {
    path: "/tables",
    name: "Tables",
    component: Tables,
  },
  {
    path: "/virtual-reality",
    name: "Virtual Reality",
    component: VirtualReality,
  },
  {
    path: "/error",
    name: "Error",
    component: Error,
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile,
  },
  {
    path: "/signin",
    name: "Signin",
    component: Signin,
  },
  {
    path: "/signup",
    name: "Signup",
    component: Signup,
  },
  {
    path: "/buyerlist",
    name: "BuyerList",
    component: BuyerList,
  },
  {
    path: "/ordersdetial",
    name: "OrderDetail",
    component: OrderDetail,
  },
  {
    path: "/Farms/:FarmId",
    name: "OrderCreate",
    component: OrderCreate,
    props: true,
  },

  {
    path: "/farms/list",
    name: "FarmList",
    component: FarmList,
  },
  {
    path: "/farmerlist",
    name: "FarmerList",
    component: FarmerList,
  },
  // 알림 추가
  {
    path: "/order-modify/:orderId",
    name: "OrderModify",
    component: OrderModify,
    props: true,
  },
  {
    path: "/order-modify-complete/:orderId/:status",
    name: "OrderModifyComplete",
    component: OrderModifyComplete,
    props: true,
  },
  {
    path: "/order-create-complete",
    name: "OrderCreateComplete",
    component: OrderCreateComplete,
  },
  {
    path: "/order-cart-complete",
    name: "OrderCartComplete",
    component: OrderCartComplete,
  },
  {
    path: "/cart",
    name: "Cart",
    component: Cart,
  },
  // 생물 추가
  {
    path: "/crops/register",
    name: "CropRegister",
    component: CropRegister,
  },
  // 농장 추가
  {
    path: "/farms/register",
    name: "FarmRegister",
    component: FarmRegister,
  },
  // 농장 디테일
  {
    path: "/farms/:id",
    name: "FarmDetail",
    component: FarmDetail,
    props: true,
  },
  // 농장 편집
  {
    path: "/farms/:id",
    name: "FarmEdit",
    component: FarmEdit,
    props: true,
  },
  // 주문 추가
  {
    path: "/orders/register",
    name: "OrderCreate",
    component: OrderCreate,
  },
  // 판매량 조회
  {
    path: "/sales",
    name: "Sales",
    component: Sales,
  },
  {
    path: "/chat",
    name: "Chat",
    component: Chat,
  },
  // 재고 관리
  {
    path: "/inventory",
    name: "Inventory",
    component: Inventory,
    children: [
      {
        path: ":farm_index",
        name: "InventorysTable",
        component: () => import("@/views/components/InventorysTable.vue"),
        props: true,
      },
    ],
  },
  // 알림 히스토리
  {
    path: "/push/history",
    name: "Push",
    component: Push,
  },
  {
    path: "/cart",
    name: "Cart",
    component: CartView,
  },
  {
    path: "/order",
    name: "Order",
    component: OrderView,
  },
  {
    path: "/payment",
    name: "Payment",
    component: PaymentView,
  },
  {
    path: "/crop",
    name: "Crop",
    component: CropPage,
  },
  {
    path: "/ordercreate2",
    name: "OrderCreate2",
    component: OrderCreate2,
  },
  {
    path: "/orderCart2",
    name: "OrderCart2",
    component: OrderCart2,
  },
  {
    path: "/cartview2",
    name: "CartView2",
    component: CartView2,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  linkActiveClass: "active",
});

router.beforeEach((to) => {
  const userStore = useUserStore();
  const loggedIn = userStore.checkLogin();

  if (to.meta.requiresAuth && !loggedIn) {
    return "/signin"; // 로그인 안 됐으면 /signin으로 이동
  }

  if (to.path === "/signin" && loggedIn) {
    // 이미 로그인 되어있으면 대시보드로 리다이렉트
    return userStore.user.role === "FARMER"
      ? "/farmer/dashboard"
      : "/buyer/dashboard";
  }
});

export default router;
