export default () => {
  return {
    getCurrentDate(offset = 0) {
      // returns the current date as string "2019-01-01"
      return this.getCurrentDateTime(offset).split(' ')[0];
    },
    getCurrentTime(offset = 0) {
      // returns the current time as string "12:00:00"
      return this.getCurrentDateTime(offset).split(' ')[1];
    },
    getCurrentTimeHM(offset = 0) {
      // returns the current time without seconds "12:00"
      return this.getCurrentTime(offset).substring(0, 5);
    },
    getCurrentDateTime(offset = 0) {
      // returns the full DateTime string as "2019-01-01 12:00:00"
      return this.getISOWithOffset(
        Date.now() + offset * 1000,
        this.getLocalOffsetMS()
      );
    },
    getISOWithOffset(date = Date.now(), offset = 0) {
      // helper function for getting an ISO string, used internal
      return new Date(date - offset)
        .toISOString()
        .slice(0, -5)
        .split('T')
        .join(' ');
    },
    parseFromS(s) {
      // takes a number of seconds as input
      // returns a Date object
      return new Date(s * 1000);
    },
    parseFromMS(ms) {
      // takes a number of ms as input
      // returns a Date object
      return new Date(ms);
    },
    parseFromSISO(s) {
      // takes a number of seconds as input
      // returns a string ex: "2019-01-01 13:33:33"
      return this.getISOWithOffset(s * 1000, this.getLocalOffsetMS());
    },
    parseFromMSISO(ms) {
      // takes a number of millis as input
      // returns a string ex: "2019-01-01 13:33:33"
      return this.getISOWithOffset(ms, this.getLocalOffsetMS());
    },
    getLocalOffsetMS() {
      // helper function for getting the timezone offset, used internal
      return new Date().getTimezoneOffset() * 60000;
    },
    parseDateTimeStringToSeconds(string) {
      /*
       * Used in order to parse a DateTime from String like "2012-03-04 11:30"
       * returns the epoch second representing that DateTime (UTC)
       */
      string = string.trim().replace(' ', 'T');
      let parsing = string.match(/T.*/)[0].length;
      switch (parsing) {
        case 6:
          string = string.concat(':00.000Z');
          break;
        case 9:
          string = string.concat('.000Z');
          break;
        default:
          string = string.replace(/\..*/, '.000Z');
          break;
      }
      return parseInt(
        (new Date(string).getTime() + this.getLocalOffsetMS()) / 1000
      );
    }
  };
};
