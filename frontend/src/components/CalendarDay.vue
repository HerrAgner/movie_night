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
          :events="getEvents"
          :event-overlap-threshold="0"
          interval-height="24"
          :interval-format="format"
          :max-days="10"
        >
          <template v-slot:day-header="{ day }"> </template>
        </v-calendar>
      </v-sheet>
    </v-col>
    <div class="calendar_time_pickers">
      START
      <v-time-picker
        v-model="startTime"
        format="24hr"
        @change="handleChangeStartTime"
      ></v-time-picker>
      END
      <v-time-picker
        v-model="endTime"
        format="24hr"
        @change="handleChangeEndTime"
      ></v-time-picker>
      <v-btn class="create_event" @click="createEvent">CREATE</v-btn>
    </div>
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
    start: TimeService().getCurrentDate(),
    startTime: null,
    endTime: null,
    events: [],
    newEvent: [],
    loading: true
  }),
  computed: {
    getStart: {
      get: function() {
        return this.start;
      },
      set: function(newValue) {
        this.start = newValue;
      }
    },
    getEvents: {
      get: function() {
        return this.events.concat(this.newEvent);
      },
      set: function(value) {
        this.events = value;
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
    },
    format(date) {
      return date.time;
    },
    handleChangeStartTime() {
      console.log('start', this.startTime);
    },
    handleChangeEndTime() {
      console.log('end', this.endTime);
    },
    createEvent() {
      console.log(this.getStart);
      if (this.startTime && this.endTime && this.startTime < this.endTime) {
        let startTime = `${this.getStart} ${this.startTime}`;
        let endTime = `${this.getStart} ${this.endTime}`;
        this.newEvent = [
          {
            start: startTime,
            end: endTime,
            name: '',
            color: '',
            description: ''
          }
        ];
      }
    }
  },

  mounted() {
    this.getBusyEvents();
  }
};
</script>

<style>
.calendar_time_pickers {
  display: flex;
  flex-direction: column;
}
</style>
