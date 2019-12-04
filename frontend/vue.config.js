const backend = 'http://localhost:8080';

module.exports = {
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
}