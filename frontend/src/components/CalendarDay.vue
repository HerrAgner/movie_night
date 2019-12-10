<template>
  <Loading v-if="isLoading" />
  <v-row v-else>
    <v-col>
      <v-btn outlined small color="primary" @click="$refs.calendar.prev()">
        <v-icon>navigate_before</v-icon>
      </v-btn>

      <v-btn outlined small color="primary" @click="$refs.calendar.next()">
        <v-icon>navigate_next</v-icon>
      </v-btn>
      <v-sheet height="800">
        <v-calendar
          ref="calendar"
          color="primary"
          type="day"
          v-model="getStart"
          :events="compEvents"
          :event-overlap-threshold="0"
          interval-height="24"
          :interval-format="format"
          :max-days="10"
          @mousedown:time="date => createEventStart(date)"
          @mouseup:time="date => createEventEnd(date)"
          @change="data => handleChange(data)"
        >
          <!-- @click:time="console.log('hej')" -->
          <template v-slot:day-header="{ day }"> </template>
          <!-- <template v-slot:day-body="{ time }"> {{time}} </template> -->
          <!-- <template v-slot:interval="{ time }"> {{time}}</template> -->
        </v-calendar>
      </v-sheet>
    </v-col>
  </v-row>
</template>

<script>
import GCalendarService from '@/services/GCalendarService';
import TimeService from '@/services/TimeService';
import Loading from '@/components/Loading';

export default {
  name: 'CalendarDay',
  components: {
    Loading
  },
  data: () => ({
    start: new Date(),
    newEvent: {eventSet: false},
    events: [
      {
        name: '',
        details:
          'This starts in the middle of an event and spans over multiple events',
        start: '2019-12-08 12:00',
        end: '2019-12-08 14:00',
        color: 'deep-purple'
      }
    ],
    loading: true
  }),
  computed: {
    getStart: {
      get: function() {
        return this.start.toLocaleString();
      },
      set: function(newValue) {
        this.start = newValue;
      }
    },
    compEvents: {
      get: function() {
        return this.events;
      },
      set: function(newValue) {
        this.events = newValue;
      }
    },
    isLoading() {
      return this.loading;
    },
    isEventSet() {
      return !!this.newEvent.eventSet;
    }
  },
  methods: {
    async getBusyEvents() {
      this.loading = true;
      let busyEvents = await GCalendarService().getFreeBusyCalendarFromList([
        'martin',
        'user'
      ]);

      for (let user of Object.values(busyEvents)) {
        if (
          user &&
          user.calendars &&
          user.calendars.primary &&
          user.calendars.primary.busy
        ) {
          for (let event of user.calendars.primary.busy) {
            let startDate = TimeService().parseFromMSISO(event.start.value);
            let endDate = TimeService().parseFromMSISO(event.end.value);

            this.events.push({
              start: startDate,
              end: endDate,
              name: '',
              color: 'indigo',
              description: ''
            });
          }
        }
      }
      this.loading = false;
      console.log(this.events);
    },
    format(date) {
      return date.time;
    },
    createEventStart(date) {
      this.newEvent.start = date;
    },
    createEventEnd(date) {
      this.newEvent.end = date;
      const startTime = `${this.newEvent.start.date} ${this.newEvent.start.time}`;
      const endTime = `${this.newEvent.end.date} ${this.newEvent.end.time}`;
      // console.log('timetoy', date.timeToY(true));
      // console.log(this.isEventSet);
      if (!this.isEventSet) {
        this.compEvents = [...this.compEvents, {start: startTime, end: endTime, name: ''}]
        console.log(this.compEvents);
        this.newEvent.eventSet = true;
      } else {
        // console.log('eeevent', this.events[0]);
        // this.compEvents = this.compEvents;
        console.log('eeevent', this.events[0]);
      }
      console.log(this.newEvent);
      
    },
    handleChange(data) {
      console.log(data);
    }
  },
  mounted() {
    this.getBusyEvents();
  }
};
</script>

<style></style>
