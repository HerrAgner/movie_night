import AuthService from '@/services/AuthService';


export default () => ({
  async getMovieDetails(id) {
    let response = await fetch('api/movies?i=' + id);
    response = response.status === 200 ? response.json() : null;
    return response;
  },

  async getAllMoviesDetails(ids) {
    //let allId = JSON.stringify(ids);
    //console.log(allId);
    //let url = 'api/movies/GetJson';

    //for (let i=0; i<allId.length; ++i) {
      //if (url.indexOf('?') === -1) {
        //url = url + '?id=' + allId[i];
      //}else {
        //url = url + ',' + allId[i];
      //}
    //}
    console.log(JSON.stringify(ids));
    let response = await fetch('api/movies', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: AuthService().getBearerTokenAsString()
      },
      body: JSON.stringify(ids)
    });

  }
});
