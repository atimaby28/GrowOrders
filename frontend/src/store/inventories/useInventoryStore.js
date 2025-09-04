import { defineStore } from "pinia";
import { reactive } from "vue";

export const useInventoryStore = defineStore("inventories", () => {
  let farmInfo = reactive({
    user_id: null,
    id: null,
    name: "",
    region: "",
    address: "",
    size: null,
    contents: "",
    farmImage: null,
    cropList: [],
  });

  return { farmInfo };
});
