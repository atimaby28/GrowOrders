<script setup>
import GradientLineChart from "@/examples/Charts/GradientLineChart.vue";

const props = defineProps({
  chartsData: {
    type: Array,
    default: () => []
  }
});
</script>

<template>
  <div class="card card-carousel overflow-hidden h-100 p-0">
    <div
      id="carouselExampleCaptions"
      class="carousel slide h-100"
      data-bs-ride="carousel"
    >
      <div class="carousel-inner border-radius-lg h-100">
        <!-- 동적 차트 -->
        <div
          v-for="(chart, index) in props.chartsData"
          :key="chart.id"
          class="carousel-item h-100"
          :class="{ active: index === 0 }"
        >
          <div class="card z-index-2 h-100">
            <gradient-line-chart
              :id="`chart-${chart.id}`"
              :title="chart.title"
              :description="`<i class='fa fa-arrow-up text-success'></i> 
                            <span class='font-weight-bold'>${index + 4}% more</span> in 2021`"
              :chart="chart.chart"
            />
          </div>
        </div>
      </div>

      <!-- 캐러셀 컨트롤 버튼 -->
      <button
        class="carousel-control-prev w-5 me-3"
        type="button"
        data-bs-target="#carouselExampleCaptions"
        data-bs-slide="prev"
      >
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
      </button>
      <button
        class="carousel-control-next w-5 me-3"
        type="button"
        data-bs-target="#carouselExampleCaptions"
        data-bs-slide="next"
      >
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
      </button>
    </div>
  </div>
</template>
