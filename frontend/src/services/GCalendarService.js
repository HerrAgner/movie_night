import AuthService from '@/services/AuthService';


export default () => ({
  async fetchSuggestedEventPeriods(data, duration = 0) {
    let response = await fetch('/api/gcal?duration=' + duration, {
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
  }
});
