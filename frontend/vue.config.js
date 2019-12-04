const backend = 'http://localhost:8080';

module.exports = {
  publicPath: '',
  outputDir: './../src/main/resources/static/',
  devServer: {
    port: 8081,
    proxy: {
      '/api': {
        target: backend,
        ws: true,
        changeOrigin: true
      }
    }
  }
};