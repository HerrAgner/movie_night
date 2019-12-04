const backend = 'http://localhost';

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