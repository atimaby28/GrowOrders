import { ref, computed, watch, onMounted, onBeforeUnmount } from "vue";

export function farmListScroll(
  sourceRef,
  { batchSize = 30, threshold = 0.2 } = {}
) {
  const visibleCount = ref(batchSize);
  const loadingMore = ref(false);

  const canLoadMore = computed(() => visibleCount.value < sourceRef.value.length);
  const visibleItems = computed(() =>
    sourceRef.value.slice(0, visibleCount.value)
  );

  function loadMore() {
    if (!canLoadMore.value) return;
    visibleCount.value = Math.min(
      visibleCount.value + batchSize,
      sourceRef.value.length
    );
  }

  function reset() {
    visibleCount.value = Math.min(batchSize, sourceRef.value.length);
  }

  watch(sourceRef, () => reset(), { immediate: true });

  const sentinel = ref(null);
  let observer = null;

  function setupObserver() {
    if (observer) observer.disconnect();
    observer = new IntersectionObserver(
      ([entry]) => {
        if (entry?.isIntersecting && canLoadMore.value && !loadingMore.value) {
          loadingMore.value = true;
          requestAnimationFrame(() => {
            loadMore();
            loadingMore.value = false;
          });
        }
      },
      { root: null, threshold }
    );
    if (sentinel.value) observer.observe(sentinel.value);
  }

  onMounted(setupObserver);
  onBeforeUnmount(() => observer?.disconnect());

  return { visibleItems, canLoadMore, loadMore, reset, sentinel, loadingMore };
}
