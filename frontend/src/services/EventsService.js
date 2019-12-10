import AuthService from './AuthService';

export default () => ({
    async getAllEvents(username) {
        let res = await fetch('/api/events', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: AuthService().getBearerTokenAsString()
            },
        });
        return res.status === 200 ? true : false;
    },

});
