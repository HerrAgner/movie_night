export default () => ({
    async addUserToDB(info) {
      const data = { username: info.username, password: info.password };
    
      let res = await fetch('/api/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });
      return res.status === 200 ? true : false;
    }
});