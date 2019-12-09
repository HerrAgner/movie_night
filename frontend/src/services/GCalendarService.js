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
  }
});