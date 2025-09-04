import { ref, computed } from "vue";

export function farmListFilter(flatCrops) {
  const q = ref("");
  const selectedCrop = ref("");
  const selectedRegion = ref("");
  const selectedCultivate = ref("");

  const cropTypeOptions = computed(() => {
    const s = new Set();
    for (const { crop } of flatCrops.value) {
      const t = (crop?.type ?? "").toString().trim();
      if (t) s.add(t);
    }
    return [...s].sort((a, b) => a.localeCompare(b));
  });

  const regionOptions = computed(() => {
    const s = new Set();
    for (const { farm } of flatCrops.value) {
      const r = (farm?.region ?? "").toString().trim();
      if (r) s.add(r);
    }
    return [...s].sort((a, b) => a.localeCompare(b));
  });

  const cultivateOptions = computed(() => {
    const s = new Set();
    for (const { crop } of flatCrops.value) {
      const c = (crop?.cultivateType ?? "").toString().trim();
      s.add(c); 
    }
    return [...s].sort((a, b) => a.localeCompare(b));
  });

  const filtered = computed(() => {
    const qval = q.value.trim().toLowerCase();

    return flatCrops.value.filter(({ farm, crop }) => {
      const type      = (crop?.type ?? "").toString().trim();
      const region    = (farm?.region ?? "").toString().trim();
      const cultivate = (crop?.cultivateType ?? "").toString().trim();

      if (selectedCrop.value   && type   !== selectedCrop.value) return false;
      if (selectedRegion.value && region !== selectedRegion.value) return false;

      if (selectedCultivate.value) {
        if (selectedCultivate.value === "__none__") {
          if (!(cultivate === "" || cultivate == null)) return false;
        } else if (cultivate !== selectedCultivate.value) {
          return false;
        }
      }

      if (!qval) return true;
      const text = `${farm?.name || ""} ${region} ${type} ${cultivate}`.toLowerCase();
      return text.includes(qval);
    });
  });

  return {
    q, selectedCrop, selectedRegion, selectedCultivate,
    cropTypeOptions, regionOptions, cultivateOptions,
    filtered,
  };
}
