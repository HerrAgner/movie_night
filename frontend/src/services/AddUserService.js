export default () => ({
    async addUserToDB(info) {
      const data = { username: info.username, password: info.password };
    
      await fetch('/api/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      }).then(async response => {
        console.log(response.status);
        return response.status === 200 ? true : false;
      })
    }
});