import Cookie from "js-cookie";
import store from "../store/index"

export default () => ({
    async getUser() {
        if (Cookie.get("token") !== undefined) {
            let token = Cookie.get("token");
            let res = await fetch('/api/login', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                },
            });
            if (res.status === 200) {
                let username = await res.text();
                store.state.isLoggedin = true;
                store.state.cookie = Cookie.get("token");
                store.state.loggedInUser = username
            } else if (res.status === 500){
                store.state.isLoggedin = false;
                Cookie.remove('token');
            }
        }
    }

});
