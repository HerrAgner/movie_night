import Cookie from 'js-cookie';

export default () => ({
  getBearerToken() {
    const token = Cookie.get('token');
    return token ? token : null;
  },
  getBearerTokenAsString() {
    return `Bearer ${this.getBearerToken()}`;
  }
});
