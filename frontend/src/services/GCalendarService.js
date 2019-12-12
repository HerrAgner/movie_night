import AuthService from '@/services/AuthService';


export default () => ({
  async getFreeBusyCalendarFromList(data) {
    let response = await fetch('/api/gcal', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: AuthService().getBearerTokenAsString()
      },
      body: JSON.stringify(data)
    });
    response = response.status === 200 ? await response.json() : null;
    return response;
  },
  async getEventsFromCalendar(data) {
    let response = await fetch('/api/gcal/events', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: AuthService().getBearerTokenAsString()
      },
      body: JSON.stringify(data)
    });
    response = response.status === 200 ? await response.json() : null;
    return response;
  },
  async createGoogleCalendarEvent(data) {
       let res = await fetch('/api/gcal/events/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: AuthService().getBearerTokenAsString()
      },
      body: JSON.stringify(data)
    });
    return res.status === 200 ? true : false;
  }  ,

  async updateGoogleCalendarEvent(data) {
       let res = await fetch('/api/gcal/event', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: AuthService().getBearerTokenAsString()
      },
      body: JSON.stringify(data)
    });
    return res.status === 200;
  },


    async deleteEvent(eventId){
        console.log(eventId)
        let response = await fetch('api/gcal/event/'+eventId, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                Authorization: AuthService().getBearerTokenAsString()
            },
        });
        return response.status === 200 ? true : null;
    },
});
