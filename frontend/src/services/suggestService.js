export default () => ({
  jsonp() {
    let that = {};

    that.send = function (src, options) {
      let callback_name = options.callbackName || 'callback',
        on_success = options.onSuccess || function () {
        },
        on_timeout = options.onTimeout || function () {
        },
        timeout = options.timeout || 10; // sec

      let timeout_trigger = window.setTimeout(function () {
        window[callback_name] = function () {
        };
        on_timeout();
      }, timeout * 1000);

      window[callback_name] = function (data) {
        window.clearTimeout(timeout_trigger);
        on_success(data);
      };

      let script = document.createElement('script');
      script.type = 'text/javascript';
      script.async = true;
      script.src = src;

      document.getElementsByTagName('head')[0].appendChild(script);
    };

    return that;
  },
  testo() {
    var script = document.createElement('script');
    script.src = 'https://v2.sg.media-imdb.com/suggests/g/gladi.json';

    document.querySelector('head').appendChild(script);
    return script;
  }
})