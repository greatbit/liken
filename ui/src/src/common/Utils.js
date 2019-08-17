import Moment from 'moment/min/moment.min.js';
import $ from 'jquery';
import qs from 'qs';


export function timeToDate(time) {
    if (!time || time == 0){
        return 'No data'
    }
    var date = new Date(parseInt(time));
    var currMinutes = date.getMinutes();
    var monthNames = new Array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    if (currMinutes < 10) {
        currMinutes = "0" + currMinutes;
    }
    return date.getDate() + " " + monthNames[date.getMonth()] + " " + date.getFullYear() +
        " " + date.getHours() + ":" + currMinutes;
}

export function longToDateTimeFormatted(uts, format) {
    return Moment(uts).format(format);
}

export function onErrorMessage(message, error){
    if (error && error.response && error.response.message){
        message = message + error.response.data.message;
    }
     $("#error-message-text").html(message);
     $("#error-alert").show();
     $("#error-alert").fadeTo(5000, 500).slideUp(500, function(){
         $("#error-alert").slideUp(500);
         $("#error-message-text").html("");
      });
}

export function onSuccessMessage(message){
    $("#success-message-text").html(message);
    $("#success-alert").fadeTo(5000, 500).slideUp(500, function(){
        $("#success-alert").slideUp(500);
        $("#success-message-text").html("");
     });
}

export function getProgressBarStyle(value, total){
    return {width:  (value * 100) / total + '%'};
}

export function getProgressBarNumber(value, total){
    if (!value || value == 0 || (value * 100) / total < 2) {
        return  "";
    }
    return value;
}

export function timePassed(passedTime){
   var passedTimeDisplayValue = "0";

   if (passedTime == null || passedTime == undefined || passedTime == 'undefined' || passedTime == "" || passedTime == 0){
       return passedTimeDisplayValue;
   }

   if (passedTime > 0 && passedTime < 1000 ){
       return "Less than 1 second";
   }

   if (passedTime >= 1000 && passedTime < 60000 ){
       return Math.floor(passedTime / 1000) + " sec"
   }

   if (passedTime >= 60000 && passedTime < 3600000 ){
       return Math.floor(passedTime / 60000) + " min"
   }

   if (passedTime >= 3600000 ){
       passedTimeDisplayValue = Math.floor(passedTime / 3600000) + " hour "
       passedTimeDisplayValue = passedTimeDisplayValue +
           Math.floor(((passedTime/3600000 - Math.floor(passedTime / 3600000)) * 60)) +  " min"
   }
   return passedTimeDisplayValue;
}

export function intDiv(val, by){
    return (val - val % by) / by;
}

export function filterToQuery(filter){
    return Object.keys(filter).map((key) => {return key + "=" + filter[key]}).join("&");
}

export function queryToFilter(locationSearch){
    var params = qs.parse(locationSearch);
    var filter = {};
    filter.skip = params.page || 0;
    filter.limit = params.size || 20;
    return filter;
}