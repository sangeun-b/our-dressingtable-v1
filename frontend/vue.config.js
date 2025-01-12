const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

module.exports = {
  outputDir: "../src/main/resources/static", // build target directory
  devServer: {
    proxy: {
      '/api': {
        // '/api'로 들어오면 port 8080(스프링 서버)로 보냄
        target: 'http://localhost:8080',
        changeOrigin: true // cross-origin 허용
      }
    }
  }
}
