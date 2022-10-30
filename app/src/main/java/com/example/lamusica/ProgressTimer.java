package com.example.lamusica;

import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Date;

public class ProgressTimer {
    private static int timerRunning = 0 ;
    private CountDownTimer countDowntimer ;
    private long timeLeftInMilliSeconds ;
    private TextView CountDown ;
    private boolean state = true;
    private long fixedtime ;
    private ProgressBar pg ;
    private int progress ;
    private long rate ;
    private boolean Return ;
    private SeekBar seekBar ;
    ProgressTimer(long timeLeftInMilliSeconds , TextView CountDown , ProgressBar pg , SeekBar seekBar){
        timerRunning = 0 ;
        this.rate = 0 ;
        this.pg = pg ;
        Return = true ;
        fixedtime = timeLeftInMilliSeconds ;
        this.CountDown = CountDown ;
        this.timeLeftInMilliSeconds = timeLeftInMilliSeconds ;
        countDowntimer = null ;
        this.seekBar = seekBar ;
    }

    ProgressTimer() {
        timerRunning = 0;
        //to use timer runing in the first time
        //but we need to define the first constructor
        state = false;
        // state = false it mean that we use the second constructor not the first
    }

    public void startTime(){
        //state = first constructor
        if(state) {
            //start time
            start() ;
            timerRunning = 1;
        }
    }

    public void stopTime(){
        //state = first constructor
        if(state) {
            // stop time
            stop () ;
            timerRunning = 0;
        }
    }

    public void pauseTime() {
        //state = first constructor
        if(state) {
            // function pause save millisecond before stop the timer
            pause() ;
            timerRunning = 2;
        }
    }

    public void updateTimer(){
        // Update will return in every time the new time as string
        // we will set the string in the text view every time
        increase();
        CountDown.setText(update());
    }

    public void resetTime(){
        //state = first constructor
        if(state) {
            CountDown.setText("00:00:00");
            countDowntimer.cancel();
            timerRunning = 0;
            pg.setProgress(0);
        }
    }

    public void start(){
        countDowntimer = new CountDownTimer(timeLeftInMilliSeconds, 1000) {
            // 1000 is second ==> count down
            // timeLeftInMilliSeconds is the time that the user is enterd
            @Override
            public void onTick(long millisUntilFinished) {
                //millisUntilFinished is the changed time
                timeLeftInMilliSeconds = millisUntilFinished ;

                updateTimer();
            }

            @Override
            public void onFinish() {
                //passing function
            }
        }.start();
    }

    public void stop (){
        countDowntimer.cancel();
    }

    public void pause(){
        // save date in millisecond
        // when timer start , it will start at the time that was stoped form
        saveDate();
        countDowntimer.cancel();
    }

    public String update(){
        //format the timer to print it
        long fixedT2 = fixedtime - timeLeftInMilliSeconds ;
        return ConvertMilli(fixedT2) ;
    }

    public void saveDate(){
        // convert the time before stoped it to millisecond
        // to start with it in the next time
        timeLeftInMilliSeconds = (new Date(timeLeftInMilliSeconds).getHours() - 2) * 60 * 60 * 1000 +
                (new Date(timeLeftInMilliSeconds)).getMinutes() * 60 * 1000 +
                (new Date(timeLeftInMilliSeconds)).getSeconds() * 1000 ;
    }

    private void updateProgress(){
        pg.setProgress(progress);
        seekBar.setProgress(progress);
    }

    public void increase (){
        if(progress < 110) {
            rate = Math.round((((float) (1000 + (fixedtime - timeLeftInMilliSeconds))) / fixedtime) * 100); //150
            System.out.println(progress + "============================= progress");
            progress = (int) rate;
            countDowntimer.onFinish();
            updateProgress() ;
        }
    }


    public int getTimerRunning() {
        return timerRunning;
    }

    public void setTimeLeftInMilliSeconds(long timeLeftInMilliSeconds) {
        this.timeLeftInMilliSeconds = timeLeftInMilliSeconds;
    }

    public void setFixedtime(long fixedtime) {
        this.fixedtime = fixedtime;
    }

    public long getFixedtime() {
        return fixedtime;
    }

    public CountDownTimer getCountDown() {
        return countDowntimer;
    }

    public long getTimeLeftInMilliSeconds() {
        return timeLeftInMilliSeconds;
    }

    public String ConvertMilli(long time){
        String timeleft ;
        timeleft = "" + (new Date(time).getHours()-2) ;
        timeleft += " : " ;

        if((new Date(time)).getMinutes() < 10){
            timeleft += "0" ;
        }
        timeleft += "" + (new Date(time)).getMinutes() ;
        timeleft += " : " ;

        if((new Date(time)).getSeconds() < 10){
            timeleft += "0" ;
        }
        timeleft += (new Date(time)).getSeconds() ;
        return timeleft ;
    }

}
