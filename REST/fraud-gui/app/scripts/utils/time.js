function msToTime(duration) {
  var seconds = parseInt((duration/1000)%60)
    , minutes = parseInt((duration/(1000*60))%60)
    , hours = parseInt((duration/(1000*60*60))%24)
    , days = parseInt((duration/(1000*60*60*24)));

  hours = (hours < 10) ? "0" + hours : hours;
  minutes = (minutes < 10) ? "0" + minutes : minutes;
  seconds = (seconds < 10) ? "0" + seconds : seconds;
  days = (days < 10) ? "0" + days : days;

  return (days > 0 ? (days + (days % 10 == 1 ? ' day ' : ' days ')) : '')
    + ((hours > 0 || minutes > 0 || seconds > 0) ? hours + ":" + minutes + ":" + seconds : '');
}
