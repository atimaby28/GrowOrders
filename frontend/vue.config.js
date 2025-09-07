// vue.config.js
module.exports = {
  devServer: {
    proxy: {
      "/api": {
        target: "http://localhost:8080", // 백엔드 주소
        changeOrigin: true,
        // pathRewrite: { "^/api": "" },
        secure: false,
      },
    },
  },
};
