<script setup>
import { onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useFarm } from "@/views/components/farm/useFarm.js";
import setNavPills from "@/assets/js/nav-pills.js";
import setTooltip from "@/assets/js/tooltip.js";
import DEFAULT_FARM_IMG from "@/assets/img/default-farm-image.png";

const route = useRoute();
const router = useRouter();
const { farm, loading, error, loadFarm, canEdit } = useFarm();

onMounted(() => loadFarm(route.params.id));
onMounted(() => {
  setNavPills();
  setTooltip();
});

function goEdit() {
  if (!farm?.value?.id) return;
  router.push({ name: "FarmEdit", params: { id: farm.value.id } });
}

const imageSrc = computed(() => {
  const f = farm.value ?? {};
  const url = f.farmImage ?? f.imageUrl ?? f.image ?? null;
  return url || DEFAULT_FARM_IMG;
});

const cropRows = computed(() => {
  const f = farm.value && farm.value.data ? farm.value.data : farm.value || {};
  const list = Array.isArray(f.cropList) ? f.cropList : [];

  return list.map((c) => ({
    id: c?.id ?? null,
    type: c?.type || "-",
    cultivateType: c?.cultivateType || "방식 미정",
    expectedQuantity:
      (c?.expectedQuantity ?? "") === "" || c?.expectedQuantity == null
        ? "-"
        : String(c.expectedQuantity),
  }));
});

const cropCount = computed(() => cropRows.value.length);

const stats = computed(() => [
  { key: "region", label: "지역", value: farm.value?.region || "-" },
  {
    key: "area",
    label: "면적",
    value: farm.value?.size != null ? `${farm.value.size} ㎡` : "-",
  },
  { key: "crops", label: "작물 수", value: cropCount.value },
]);
</script>

<template>
  <main class="page">
    <!-- HERO -->
    <section class="hero" aria-label="농장 대표 섹션">
      <img
        :src="imageSrc"
        class="hero__bg"
        @error="
          (e) => {
            e.target.src = DEFAULT_FARM_IMG;
          }
        "
        alt="farm cover"
      />
      <span class="mask bg-gradient-success opacity-5"></span>
      <div class="container-fluid hero__container">
        <div class="hero__content">
          <div class="hero__row">
            <h1 class="hero__title px-2 pb-1">{{ farm?.name || "농장" }}</h1>
          </div>
          <div class="hero__meta px-2">
            <span class="chip chip--glass" v-tooltip="'지역'">{{
              farm?.region
            }}</span>
            <span class="chip chip--glass" v-tooltip="'면적'"
              >{{ farm?.size }} ㎡</span
            >
            <span class="chip chip--glass" v-tooltip="'작물 수'"
              >작물 {{ cropCount }}</span
            >
          </div>
        </div>
      </div>
    </section>

    <div class="container-fluid section">
      <!-- Loading -->
      <div
        v-if="loading"
        class="grid grid--loading"
        aria-busy="true"
        aria-live="polite"
      >
        <div class="card card--skeleton"></div>
        <div class="stack">
          <div class="card card--skeleton" style="height: 160px"></div>
          <div class="card card--skeleton" style="height: 220px"></div>
        </div>
      </div>

      <!-- Error -->
      <div v-else-if="error" class="alert">
        <div class="alert__title">불러오기 실패</div>
        <div class="alert__desc">
          일시적인 문제일 수 있어요. 새로고침하거나 잠시 후 다시 시도해 주세요.
        </div>
        <div class="alert__actions">
          <button class="btn btn--ghost" @click="loadFarm(route.params.id)">
            다시 시도
          </button>
        </div>
      </div>

      <!-- Content -->
      <div v-else-if="farm" class="grid">
        <!-- Left: Poster -->
        <div class="card card--media">
          <div class="media">
            <img
              :src="imageSrc"
              class="media__img"
              @error="
                (e) => {
                  e.target.src = DEFAULT_FARM_IMG;
                }
              "
              :alt="farm.name"
            />
          </div>
          <div class="card__foot note">
            * 이미지는 제한 시간이 지나면 보이지 않을 수 있어요. 목록을
            새로고침하면 갱신됩니다.
          </div>
        </div>

        <!-- Right: Details & Crops -->
        <div class="stack">
          <!-- Details -->
          <div class="card">
            <div class="card__body">
              <div class="kv">
                <div class="kv__row">
                  <div class="kv__k">지역</div>
                  <div class="kv__v">{{ farm.region || "-" }}</div>
                </div>
                <div class="kv__row">
                  <div class="kv__k">면적</div>
                  <div class="kv__v">
                    {{ farm.size != null ? farm.size + " ㎡" : "-" }}
                  </div>
                </div>
                <div class="kv__row kv__row--full">
                  <div class="kv__k">주소</div>
                  <div class="kv__v">{{ farm.address || "-" }}</div>
                </div>
                <div class="kv__row kv__row--full">
                  <div class="kv__k">설명</div>
                  <div class="kv__v preline">{{ farm.contents || "-" }}</div>
                </div>
              </div>

              <div class="stats" role="list">
                <div
                  v-for="s in stats"
                  :key="s.key"
                  class="stat"
                  role="listitem"
                >
                  <div class="stat__icon" aria-hidden="true">
                    <svg
                      v-if="s.key === 'area'"
                      width="20"
                      height="20"
                      viewBox="0 0 24 24"
                      fill="none"
                    >
                      <path
                        d="M4 7h16M4 12h16M4 17h16"
                        stroke="currentColor"
                        stroke-width="1.5"
                        stroke-linecap="round"
                      />
                    </svg>
                    <svg
                      v-else-if="s.key === 'crops'"
                      width="20"
                      height="20"
                      viewBox="0 0 24 24"
                      fill="none"
                    >
                      <path
                        d="M7 12c0-2.8 2.2-5 5-5s5 2.2 5 5-2.2 5-5 5H7v-5z"
                        stroke="currentColor"
                        stroke-width="1.5"
                        stroke-linejoin="round"
                      />
                    </svg>
                    <svg
                      v-else
                      width="20"
                      height="20"
                      viewBox="0 0 24 24"
                      fill="none"
                    >
                      <path
                        d="M12 2l3 7h7l-5.5 4 2.1 7L12 16l-6.6 4 2.1-7L2 9h7l3-7z"
                        stroke="currentColor"
                        stroke-width="1.5"
                        stroke-linejoin="round"
                      />
                    </svg>
                  </div>
                  <div class="stat__meta">
                    <div class="stat__label">{{ s.label }}</div>
                    <div class="stat__value">{{ s.value }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Crops -->
          <div class="card">
            <div class="card__head">
              <h2 class="card__title">작물 정보</h2>
              <span class="badge">총 {{ cropCount }}개</span>
            </div>
            <div class="card__body">
              <div v-if="cropRows.length" class="cropgrid">
                <div
                  v-for="(c, idx) in cropRows"
                  :key="c.id ?? idx"
                  class="crop"
                >
                  <div class="crop__head">
                    <div class="crop__title">{{ c.type }}</div>
                    <span class="chip">{{
                      c.cultivateType || "방식 미정"
                    }}</span>
                  </div>
                  <div class="crop__meta">예측 생산량</div>
                  <div class="crop__value">
                    {{ c.expectedQuantity ?? "-" }}
                    <span class="unit">㎏/10a</span>
                  </div>
                </div>
              </div>
              <div v-else class="empty">등록된 작물 정보가 없습니다.</div>
            </div>
            <div class="card__foot card__foot--actions">
              <router-link class="btn btn--ghost" :to="{ name: 'FarmList' }"
                >목록으로</router-link
              >
              <button v-if="canEdit" class="btn btn--primary" @click="goEdit">
                수정하기
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Fallback: No farm -->
      <div v-else class="empty">데이터를 찾을 수 없습니다.</div>
    </div>
  </main>
</template>

<style scoped>
/* =============================
   LIGHT THEME TOKENS
   ============================= */
:root {
  --bg: #f7faf9;
  --bg-soft: #ffffff;
  --card: #ffffff;
  --card-border: rgba(15, 23, 42, 0.08);
  --muted: #6b7280;
  --text: #0f172a;
  --accent: #22c55e;
  --accent-ink: #064e3b;
  --wash: #f1f5f9;
  --ring: rgba(34, 197, 94, 0.25);
}

.page {
  color: var(--text);
  background: linear-gradient(180deg, #f8fbf9 0%, #f7faf9 60%, #f3f7f5 100%);
  min-height: 100vh;
}
.section {
  padding: 20px 12px 48px;
}

/* =============================
   HERO — cleaner header
   ============================= */
.hero {
  position: relative;
  height: 240px;
  overflow: hidden;
  border-bottom: 1px solid rgba(15, 23, 42, 0.07);
}
.hero__bg {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: brightness(0.88) saturate(1.05);
  transform: scale(1.03);
}
.hero__container {
  position: relative;
  height: 100%;
}
.hero__content {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  justify-content: flex-end;
  padding: 20px 16px;
}

/* title + actions on a single clean row */
.hero__row {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
}
.hero__title {
  margin: 0;
  font-weight: 200;
  letter-spacing: -0.01em;
  color: #fff;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.25);
  font-size: clamp(22px, 2.6vw, 32px);
}
.hero__actions {
  display: flex;
  gap: 8px;
}

/* subtle meta chips row */
.hero__meta {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}
.hero .chip--glass {
  background: rgba(255, 255, 255, 0.22);
  border: 1px solid rgba(255, 255, 255, 0.38);
  color: #fff;
}
.hero .btn--ghost {
  --btn-ink: #fff;
  --btn-bg: rgba(255, 255, 255, 0.12);
  --btn-br: rgba(255, 255, 255, 0.32);
}

/* =============================
   GRID LAYOUT
   ============================= */
.grid {
  display: grid;
  gap: 20px;
  grid-template-columns: 1fr;
}
.grid--loading {
  grid-template-columns: minmax(0, 480px) 1fr;
  align-items: start;
}
@media (min-width: 992px) {
  .grid {
    grid-template-columns: minmax(0, 480px) 1fr;
  }
}
.stack {
  display: grid;
  gap: 20px;
  align-content: start;
}

/* =============================
   CARDS
   ============================= */
.card {
  background: linear-gradient(
      180deg,
      rgba(0, 0, 0, 0.01) 0%,
      rgba(0, 0, 0, 0) 100%
    ),
    var(--card);
  border: 1px solid var(--card-border);
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(2, 6, 12, 0.06);
  overflow: clip;
}
.card--media {
  display: flex;
  flex-direction: column;
  align-self: start;
}
.card__body {
  padding: 18px;
}
.card__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 14px 18px;
  border-bottom: 1px dashed rgba(15, 23, 42, 0.08);
}
.card__foot {
  padding: 10px 14px;
  border-top: 1px dashed rgba(15, 23, 42, 0.08);
  color: var(--muted);
  font-size: 12px;
}
.card__foot--actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
  align-items: center;
}
.card__title {
  margin: 0;
  font-size: 14px;
  letter-spacing: 0.02em;
  text-transform: uppercase;
  color: #0f5132;
}

.card--skeleton {
  height: 360px;
  position: relative;
  overflow: hidden;
  background: #eef2f6;
  border-radius: 16px;
}
.card--skeleton::after {
  content: "";
  position: absolute;
  inset: 0;
  background: linear-gradient(
    90deg,
    rgba(255, 255, 255, 0) 0%,
    rgba(255, 255, 255, 0.6) 50%,
    rgba(255, 255, 255, 0) 100%
  );
  animation: shimmer 1.25s infinite;
}
@keyframes shimmer {
  100% {
    transform: translateX(100%);
  }
}

/* =============================
   MEDIA
   ============================= */
.media {
  position: relative;
  aspect-ratio: 4/3;
  background: #f3f7f5;
}
.media__img {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* =============================
   CHIPS / BADGES / BUTTONS
   ============================= */
.chip {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 0.28rem 0.6rem;
  border-radius: 999px;
  font-size: 12px;
  color: #065f46;
  background: rgba(34, 197, 94, 0.12);
  border: 1px solid rgba(34, 197, 94, 0.24);
}
.chip--glass {
  color: #0f172a;
  background: rgba(15, 23, 42, 0.04);
  border: 1px solid rgba(15, 23, 42, 0.08);
}
.badge {
  font-size: 12px;
  padding: 0.25rem 0.5rem;
  border-radius: 8px;
  background: rgba(34, 197, 94, 0.12);
  border: 1px solid rgba(34, 197, 94, 0.24);
  color: #065f46;
}

.btn {
  --btn-bg: #fff;
  --btn-br: rgba(15, 23, 42, 0.12);
  --btn-ink: var(--text);
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 0.55rem 0.85rem;
  border-radius: 12px;
  border: 1px solid var(--btn-br);
  background: var(--btn-bg);
  color: var(--btn-ink);
  font-weight: 600;
  font-size: 13px;
  letter-spacing: 0.01em;
  cursor: pointer;
  transition:
    transform 0.08s ease,
    box-shadow 0.18s ease,
    background 0.2s ease;
}
.btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 18px rgba(2, 6, 12, 0.08);
}
.btn:active {
  transform: translateY(0);
}
.btn--primary {
  --btn-bg: linear-gradient(
    180deg,
    rgba(34, 197, 94, 1) 0%,
    rgba(22, 163, 74, 1) 100%
  );
  --btn-br: transparent;
  --btn-ink: #fff;
  box-shadow: 0 10px 20px var(--ring);
}
.btn--ghost {
  --btn-bg: transparent;
  --btn-br: rgba(15, 23, 42, 0.14);
}

/* =============================
   KEY/VALUE LINES
   ============================= */
.kv {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 8px 16px;
}
.kv__row {
  display: flex;
  gap: 12px;
  padding: 8px 0;
  border-bottom: 1px dashed rgba(15, 23, 42, 0.06);
}
.kv__row:last-child {
  border-bottom: none;
}
.kv__row--full {
  grid-column: 1 / -1;
}
.kv__k {
  width: 80px;
  min-width: 80px;
  font-size: 12px;
  color: var(--muted);
}
.kv__v {
  flex: 1;
  font-size: 13px;
  font-weight: 500;
}
.preline {
  white-space: pre-line;
}

/* =============================
   STATS
   ============================= */
.stats {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
  margin-top: 16px;
}
.stat {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  border: 1px solid rgba(15, 23, 42, 0.08);
  border-radius: 14px;
  background: linear-gradient(180deg, rgba(0, 0, 0, 0.01), rgba(0, 0, 0, 0));
}
.stat__icon {
  display: grid;
  place-items: center;
  width: 36px;
  height: 36px;
  border-radius: 10px;
  color: #166534;
  background: rgba(34, 197, 94, 0.12);
  border: 1px solid rgba(34, 197, 94, 0.24);
}
.stat__label {
  font-size: 12px;
  color: var(--muted);
}
.stat__value {
  font-weight: 700;
}
.stat__meta {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

/* =============================
   CROPS
   ============================= */
.cropgrid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 12px;
}
@media (min-width: 768px) {
  .cropgrid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

.crop {
  border: 1px solid rgba(15, 23, 42, 0.08);
  border-radius: 14px;
  padding: 14px;
  background: linear-gradient(180deg, rgba(0, 0, 0, 0.01), rgba(0, 0, 0, 0));
  transition:
    transform 0.12s ease,
    box-shadow 0.18s ease,
    border-color 0.2s ease;
}
.crop__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  margin-bottom: 6px;
}
.crop__title {
  font-weight: 700;
  letter-spacing: 0.01em;
}
.crop__meta {
  font-size: 12px;
  color: var(--muted);
}
.crop__value {
  margin-top: 4px;
  font-weight: 700;
}
.unit {
  color: var(--muted);
  font-weight: 600;
  font-size: 12px;
}

/* =============================
   EMPTY / ALERT
   ============================= */
.empty {
  padding: 18px;
  border-radius: 12px;
  border: 1px dashed rgba(15, 23, 42, 0.18);
  color: var(--muted);
  text-align: center;
  background: #fff;
}
.alert {
  border: 1px solid rgba(220, 38, 38, 0.25);
  background: rgba(248, 113, 113, 0.08);
  border-radius: 14px;
  padding: 14px;
  color: #7f1d1d;
}
.alert__title {
  font-weight: 800;
  margin-bottom: 4px;
}
.alert__desc {
  font-size: 13px;
  color: #9f1239;
}
.alert__actions {
  margin-top: 10px;
  display: flex;
  gap: 8px;
}

/* =============================
   A11Y
   ============================= */
[aria-busy="true"] {
  cursor: progress;
}
</style>
