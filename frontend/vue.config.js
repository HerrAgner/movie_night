const backend = 'http://localhost:8080';

module.exports = {
  publicPath: '',
  outputDir: './../src/main/resources/static/',
  assetsDir: './assets/',
  devServer: {
    port: 8081,
    proxy: {
      '/api': {
        target: backend,
        ws: true,
        changeOrigin: true
      },
      '/assets': {
        target: backend,
        ws: true,
        changeOrigin: true
      }
    }
  }
};