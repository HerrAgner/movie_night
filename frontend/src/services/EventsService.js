import AuthService from './AuthService';

export default () => ({
    async getAllEvents() {
        let res = await fetch('/api/event/all', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: AuthService().getBearerTokenAsString()
            },
        });
        return res.status === 200 ? res.json() : false;
    },

});
