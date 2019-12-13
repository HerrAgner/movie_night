<template>
  <v-container pa-0>
    <!-- <Loading v-if="isLoading" /> -->
    <v-select :items="getSuggestedTimePeriods" @change="updateTime" label="Pick time" :disabled="getSuggestedTimePeriods.length === 0" />
  </v-container>
</template>

<script>
import GCalendarService from '@/services/GCalendarService';
import TimeService from '@/services/TimeService';
import Loading from '@/components/Loading';

export default {
  name: 'SuggestedEventTimes',
  props: {
    runtime: Number,
    attendees: Array
  },
  components: {
    Loading
  },
  data: () => ({
    suggestedTimes: [],
    loading: true
  }),
  computed: {
    getAttendees() {
      return this.attendees;
    },
    isLoading() {
      return this.loading;
    },
    getSuggestedTimePeriods() {
      return this.suggestedTimes;
    }
  },
  methods: {
    updateTime(data){
      this.$emit('handleTimeUpdate', data);
    },
    async getSuggestedTimes() {
      this.loading = true;

      let response = await GCalendarService().fetchSuggestedEventPeriods(
        this.attendees,
        this.runtime
      );
      if (response && response.length > 0) {
        for (const time of response) {
          this.suggestedTimes.push(
            TimeService().parseFromMSISO(time.start.value) +
              ' - ' +
              TimeService().parseFromMSISO(time.end.value)
          );
        }
      } else {
        this.suggestedTimes = [];
      }
      this.loading = false;
    }
  },
  watch: {
    getAttendees: function(value) {
      if(value.length > 0) {
        this.getSuggestedTimes();
      } else {
        this.suggestedTimes = [];
      }
    }
  }
};
</script>

<style></style>
