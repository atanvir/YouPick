package com.youpic.screens

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.youpic.R
import android.widget.TextView
import android.content.Intent
import com.youpic.screens.whoActivity.WhoActivity
import android.app.DatePickerDialog
import android.widget.DatePicker
import android.app.TimePickerDialog
import android.util.Log
import android.view.View
import android.widget.TimePicker
import com.youpic.screens.almostDoneActivity.AlmostDoneActivity
import com.youpic.screens.nearActivity.NearActivity
import com.youpic.utils.Constants.Companion.DATE_FORMAT
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_when.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class WhenActivity : AppCompatActivity(), View.OnClickListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener
{
    var whenType:String?=null
    var time:String?=null
    var date:String?=null

    private fun init() {
        laterTextView.setOnClickListener(this)
        anotherDayTextView.setOnClickListener(this)
        doneButton.setOnClickListener(this)
        dateTextView.setOnClickListener(this)
        timeTextView.setOnClickListener(this)
        todayTimeTextView.setOnClickListener(this)
        nowTextView.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_when)

        Utils.printAllExtras(intent.extras!!)
      //  Log.w("testIntent",intent.extras.toString())

        init()
        findViewById<View>(R.id.backTextView).setOnClickListener { finish() }
        (findViewById<View>(R.id.titleTextView) as TextView).text = "When?"
        doneButton.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

        if( whenType=="Now"){
            date=null
            time=null
            doneButton.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {

        if(v.id==R.id.nowTextView){
            whenType="Now"

            laterLayout.visibility = View.GONE
            anotherDayLayout.visibility = View.GONE

            todayTimeTextView.text="What time today?"
            timeTextView.text="Time"
            dateTextView.text="Date"



            val outputTimePattern = "HH:mm"
            try {
                date = SimpleDateFormat(DATE_FORMAT).format(Calendar.getInstance().time)
                time = SimpleDateFormat(outputTimePattern).format(Calendar.getInstance().time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }


            onDoneClick()

        }else if (v.id == R.id.laterTextView) {
            doneButton.visibility = View.VISIBLE
            anotherDayLayout.visibility = View.GONE
            laterLayout.visibility = View.VISIBLE

            whenType="Later Today"
        } else if (v.id == R.id.anotherDayTextView) {
            doneButton.visibility = View.VISIBLE
            laterLayout.visibility = View.GONE
            anotherDayLayout.visibility = View.VISIBLE

            whenType="Another Day"
        } else if (v.id == R.id.doneButton)
        {
            onDoneClick()
        } else if (v.id == R.id.dateTextView)
        {

            if(todayTimeTextView.text!="What time today?"){

                todayTimeTextView.text="What time today?"
                time=null
                date=null
            }

            var calendar=Calendar.getInstance()
            calendar.add(Calendar.HOUR_OF_DAY,24)
            var year:Int
            var month:Int
            var mDate:Int

            if(date==null){
                year=SimpleDateFormat("yyyy").format(calendar.time).toInt()
                month=SimpleDateFormat("MM").format(calendar.time).toInt()-1
                mDate= SimpleDateFormat("dd").format(calendar.time).toInt()
            }else{
                year=SimpleDateFormat("yyyy").format(SimpleDateFormat(DATE_FORMAT).parse(date)).toInt()
                month=SimpleDateFormat("MM").format(SimpleDateFormat(DATE_FORMAT).parse(date)).toInt()-1
                mDate=SimpleDateFormat("dd").format(SimpleDateFormat(DATE_FORMAT).parse(date)).toInt()
            }

            val datepickerdialog = DatePickerDialog(this,
                    AlertDialog.THEME_HOLO_DARK, this@WhenActivity,
                    year,
                    month,
                    mDate)
            datepickerdialog.datePicker.minDate=calendar.timeInMillis
            datepickerdialog.show()
        } else if (v.id == R.id.timeTextView || v.id == R.id.todayTimeTextView) {
            var hour=""
            var min:Int
            if(v.id== R.id.todayTimeTextView){



                if(time==null){
                    var calendar=Calendar.getInstance()
                    calendar.add(Calendar.HOUR,1)
                    hour= SimpleDateFormat("HH").format(calendar.time)
                    min=SimpleDateFormat("mm").format(Calendar.getInstance().time).toInt()
                    dateTextView.text="Date"
                    date=null
                }else{
                    if(timeTextView.text=="Time"){
                        min=SimpleDateFormat("mm").format(SimpleDateFormat("HH:mm").parse(time)).toInt()
                        hour=SimpleDateFormat("HH").format(SimpleDateFormat("HH:mm").parse(time))
                    }else{

                        timeTextView.text="Time"
                        dateTextView.text="Date"

                        todayTimeTextView.text="What time today?"

                        time=null
                        date=null

                        var calendar=Calendar.getInstance()
                        calendar.add(Calendar.HOUR,1)
                        hour= SimpleDateFormat("HH").format(calendar.time)
                        min=SimpleDateFormat("mm").format(Calendar.getInstance().time).toInt()
                    }
                }







            }else{
                if(todayTimeTextView.text!="What time today?"){

                    todayTimeTextView.text="What time today?"

                    time=null
                    date=null

                }

                hour=SimpleDateFormat("HH").format(Calendar.getInstance().time)

                if(time==null){
                    min=SimpleDateFormat("mm").format(Calendar.getInstance().time).toInt()
                }else{
                    min=SimpleDateFormat("mm").format(SimpleDateFormat("HH:mm").parse(time)).toInt()
                    hour=SimpleDateFormat("HH").format(SimpleDateFormat("HH:mm").parse(time))
                }
            }



            val datepickerdialog = TimePickerDialog(
                    this,
                    AlertDialog.THEME_HOLO_DARK,
                    this,
                    hour.toInt(),
                    min,
                    true)
            datepickerdialog.show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int)
    {
       // date= "$dayOfMonth/$month/$year"
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")


        val outputFormat = SimpleDateFormat(DATE_FORMAT)
        var str: String? = null
        try {
            str = outputFormat.format(simpleDateFormat.parse(year.toString()+"-"+(month+1)+"-"+dayOfMonth.toString()))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        date= str
        dateTextView.text=str
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int)
    {



        if(whenType.equals("Later Today")){

            if(SimpleDateFormat("HH:mm").parse(hourOfDay.toString()+":"+minute)
                            .compareTo(SimpleDateFormat("HH:mm").parse(SimpleDateFormat("HH:mm").format(Calendar.getInstance().time)))<=0)
                            {

                Utils.showMessage(this,"Invalid time selected!", MSG_TOAST)

            }else{
                time=hourOfDay.toString()+":"+minute
                todayTimeTextView.text=Utils.getTimeString(time!!)

                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                try {
                    date = SimpleDateFormat(DATE_FORMAT).format(Calendar.getInstance().time)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }


        }else{
            time=hourOfDay.toString()+":"+minute
            timeTextView.text=Utils.getTimeString(time!!)
        }
    }

    private fun onDoneClick(){
        var valid=false

        if(whenType=="Now"){
             valid=true

        }else if(whenType=="Later Today"){
            if(time.isNullOrEmpty()){
                Utils.showMessage(this,"Please select time first!", MSG_TOAST)
                valid=false
            }else{
                valid=true
            }
        }else{

            if(time.isNullOrEmpty()){
                Utils.showMessage(this,"Please select time first!", MSG_TOAST)
                valid=false
            }else if(date.isNullOrEmpty()){
                Utils.showMessage(this,"Please select date first!", MSG_TOAST)
                valid=false
            }else
                valid=true

        }

        if(valid){

            val fIntent:Intent
            if (intent.getBooleanExtra("locationDetail", false))
            {
                fIntent=Intent(this@WhenActivity, WhoActivity::class.java)
                        .putExtra("locationDetail", intent.getBooleanExtra("locationDetail", false))
            } else if(intent.getBooleanExtra("fromHome", false)){

                fIntent=Intent(this, NearActivity::class.java)



            }else {
                fIntent=Intent(this@WhenActivity, WhoActivity::class.java)
            }

            fIntent.putExtra("when",whenType)
            fIntent.putExtra("whenDate",date)
            fIntent.putExtra("whenTime",time)
            fIntent.putExtras(intent.extras!!)

            startActivity(fIntent)

        }

    }

}