export default () => ({
  async getMovieDetails(id) {
    let response = await fetch('api/movies?i=' + id);
    response = response.status === 200 ? response.json() : null;
    return response;
  }
});
