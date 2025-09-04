import { reactive, computed } from "vue";
import api from "@/api/order";
import defaultFarmImage from "@/assets/img/default-farm-image.png";

const toArr = (v) => (Array.isArray(v) ? v : v != null ? [v] : []);

const buildCropsFromFarmFields = (farm) => {
  const ids = toArr(farm?.cropId);
  const types = toArr(farm?.cropType);
  const cults = toArr(farm?.cropCultivateType);
  const yields = toArr(farm?.cropExpectedQuantity);

  const len = Math.max(ids.length, types.length, cults.length, yields.length);
  const res = [];
  for (let i = 0; i < len; i++) {
    res.push({
      id: ids[i] ?? null,
      type: types[i] ?? types[0] ?? null,
      cultivateType: cults[i] ?? cults[0] ?? null,
      expectedQuantity: yields[i] ?? yields[0] ?? null,
    });
  }
  return res;
};

export function farmCrops() {
  const farmData = reactive({ list: [] });

  const farmImageUrl = (farm) => farm?.farmImage || defaultFarmImage;

  const flatCrops = computed(() => {
    const out = [];
    for (const farm of farmData.list ?? []) {
       if (Array.isArray(farm?.cropList) && farm.cropList.length) {
        for (const crop of farm.cropList) out.push({ farm, crop });
        continue;
      }
      if (Array.isArray(farm?.crops) && farm.crops.length) {
        for (const crop of farm.crops) out.push({ farm, crop });
        continue;
      }
      const rebuilt = buildCropsFromFarmFields(farm);
      if (rebuilt.length) for (const crop of rebuilt) out.push({ farm, crop });
      else out.push({ farm, crop: null });
    }
    return out;
  });

  const load = async () => {
    try {
      const list = await api.farmList();
      farmData.list = Array.isArray(list) ? list : [];
    } catch (e) {
      console.error("API 호출 오류:", e);
      farmData.list = [];
    }
  };

  return { farmData, flatCrops, farmImageUrl, load, DEFAULT_FARM_IMG: defaultFarmImage };
}