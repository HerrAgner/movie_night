<template>
  <Loading v-if="isLoading" />
  <v-row v-else>
    <v-col cols="12">
      <div class="calendar_time_pickers">
        <v-dialog
          ref="dialogStart"
          v-model="startTimeModal"
          :return-value.sync="startTime"
          width="290px"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="startTime"
              label="Start time"
              prepend-icon="access_time"
              readonly
              v-on="on"
            ></v-text-field>
          </template>
          <v-time-picker
            v-if="startTimeModal"
            v-model="startTime"
            full-width
            format="24hr"
            @change="handleChangeStartTime"
          >
          </v-time-picker>
        </v-dialog>

        <v-dialog
          ref="dialogEnd"
          v-model="endTimeModal"
          :return-value.sync="endTime"
          width="290px"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="endTime"
              label="End time"
              prepend-icon="access_time"
              readonly
              v-on="on"
            ></v-text-field>
          </template>
          <v-time-picker
            v-if="endTimeModal"
            v-model="endTime"
            full-width
            format="24hr"
            @change="handleChangeStartTime"
          >
          </v-time-picker>
        </v-dialog>
      </div>
      <div style="display: flex;">
        <div>
          <v-btn
            small
            style="height: 100%;"
            color="primary"
            @click="$refs.calendar.prev()"
          >
            <v-icon>navigate_before</v-icon>
          </v-btn>
        </div>
        <v-sheet style="flex-grow: 1;">
          <v-calendar
            ref="calendar"
            color="primary"
            type="day"
            v-model="start"
            :now="start"
            :events="getEvents"
            interval-height="24"
            :interval-format="format"
            :max-days="10"
            :event-color="getEventColor"
          >
          </v-calendar>
        </v-sheet>
        <div>
          <v-btn
            small
            style="height: 100%;"
            color="primary"
            @click="$refs.calendar.next()"
          >
            <v-icon>navigate_next</v-icon>
          </v-btn>
        </div>
      </div>
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
  props: {
    duration: Number
  },
  data: () => ({
    startTimeModal: false,
    endTimeModal: false,
    start: TimeService().getCurrentDate(),
    startTime: null,
    endTime: null,
    events: [],
    calendarEvents: null,
    newEvent: [],
    loading: true
  }),
  computed: {
    getStart() {
      return this.start;
    },
    getEvents() {
      return this.events.concat(this.newEvent);
    },
    isLoading() {
      return this.loading;
    }
  },
  methods: {
    async getBusyEvents() {
      this.loading = true;
      this.calendarEvents = await GCalendarService().getBusyAndFreePeriodsFromCalendars(
        ['martin', 'user'],
        this.duration
      );
      console.log(this.calendarEvents);
      if (this.calendarEvents.free[0]) {
        this.startTime = TimeService()
          .parseFromMSISO(this.calendarEvents.free[0].start.value)
          .split(' ')[1];
        this.endTime = TimeService()
          .parseFromMSISO(this.calendarEvents.free[0].end.value)
          .split(' ')[1];

        this.createEvent();
      }

      this.calendarEvents.busy = this.calendarEvents.busy.sort(
        (a, b) => a.start.value - b.start.value
      );

      this.calendarEvents.free = this.calendarEvents.free.sort(
        (a, b) => a.start.value - b.start.value
      );
      for (let event of this.calendarEvents.busy) {
        if (event.start.value && event.end.value) {
          this.events.push({
            start: TimeService().parseFromMSISO(event.start.value),
            end: TimeService().parseFromMSISO(event.end.value),
            name: '',
            color: 'grey',
            description: ''
          });
        }
      }

      this.loading = false;
    },
    getEventColor(event) {
      return event.color;
    },
    format(date) {
      return date.time;
    },
    handleChangeStartTime() {
      // this.startTime + ':00';
      this.createEvent();
    },
    handleChangeEndTime() {
      // this.endTime + ':00';
      this.createEvent();
    },
    isBetweenConvertedToSeconds(x, min, max) {
      x = Math.floor(x / 1000);
      min = Math.floor(min / 1000);
      max = Math.floor(max / 1000);
      return x > min && x < max;
    },
    createEvent() {
      let newEventStart = TimeService().parseDateTimeStringToMilliseconds(
        `${this.start} ${this.startTime}`
      );

      let newEventEnd = TimeService().parseDateTimeStringToMilliseconds(
        `${this.start} ${this.endTime}`
      );
      if (newEventStart > newEventEnd) {
        newEventEnd += 86400000;
      }
      let validated = this.validateTimeAvailable(newEventStart, newEventEnd);
      if (validated) {
        this.newEvent = [
          {
            start: TimeService().parseFromMSISO(newEventStart),
            end: TimeService().parseFromMSISO(newEventEnd),
            name: '',
            color: 'green',
            description: ''
          }
        ];
      }
    },
    validateTimeAvailable(newEventStart, newEventEnd) {
      const current = TimeService().parseDateTimeStringToMilliseconds();
      if (
        !newEventStart ||
        !newEventEnd ||
        newEventStart < current ||
        newEventEnd < current
      ) {
        return false;
      }
      for (const busyPeriod of this.calendarEvents.busy) {
        const busyPeriodStart =
          busyPeriod.start && busyPeriod.start.value
            ? busyPeriod.start.value
            : null;
        const busyPeriodEnd =
          busyPeriod.end && busyPeriod.end.value ? busyPeriod.end.value : null;

        if (!busyPeriodStart || !busyPeriodEnd) return false;

        if (
          this.isBetweenConvertedToSeconds(
            newEventStart,
            busyPeriodStart,
            busyPeriodEnd
          )
        ) {
          return false;
        }

        if (
          this.isBetweenConvertedToSeconds(
            newEventEnd,
            busyPeriodStart,
            busyPeriodEnd
          )
        ) {
          return false;
        }

        if (
          this.isBetweenConvertedToSeconds(
            busyPeriodStart,
            newEventStart,
            newEventEnd
          )
        ) {
          return false;
        }
      }
      return true;
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
}
</style>
