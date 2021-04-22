package com.youpic.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.youpic.R
import com.youpic.screens.almostDoneActivity.AlmostDoneActivity
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_where.*
import java.util.*
import kotlin.collections.ArrayList


class WhereActivity : AppCompatActivity(), View.OnClickListener
{
    val PLACE_REQ_CODE=121;
    var selectedPos:LatLng?=null;
    var distance="5"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_where)
        findViewById<View>(R.id.backTextView).setOnClickListener { finish() }
        (findViewById<View>(R.id.titleTextView) as TextView).text = "Where?"
        doneButton.setOnClickListener {
            if(selectedPos==null){
                Utils.showMessage(this,"Please select some location!!",MSG_TOAST)
            }else{
                startActivity(Intent(this@WhereActivity, AlmostDoneActivity::class.java)
                        .putExtras(intent.extras!!)
                        .putExtra("distance",distance)
                        .putExtra("lat",selectedPos!!.latitude.toString())
                        .putExtra("long",selectedPos!!.longitude.toString()))
            }


        }
        mile5Button.setOnClickListener(this)
        mile10Button.setOnClickListener(this)
        mile20Button.setOnClickListener(this)
        mile40Button.setOnClickListener(this)
        searchTextView.setOnClickListener(this)
    }

    override fun onClick(v: View)
    {
        if(v.id== R.id.searchTextView){

            Places.initialize(this, resources.getString(R.string.map_api_key))


            val fields1: List<Place.Field> = Arrays.asList(Place.Field.LAT_LNG,Place.Field.NAME)


            val filterTypes: List<Int> = ArrayList()


            val intent1 = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields1).setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .build(this)
            startActivityForResult(intent1, PLACE_REQ_CODE)


        }else{

            mile5Button.background = ContextCompat.getDrawable(this@WhereActivity, R.drawable.round_corner_white)
            mile10Button.background = ContextCompat.getDrawable(this@WhereActivity, R.drawable.round_corner_white)
            mile20Button.background = ContextCompat.getDrawable(this@WhereActivity, R.drawable.round_corner_white)
            mile40Button.background = ContextCompat.getDrawable(this@WhereActivity, R.drawable.round_corner_white)
            if (v.id == R.id.mile5Button) {
                mile5Button.background = ContextCompat.getDrawable(this@WhereActivity, R.drawable.round_corner_white_stroke_green)
                distance="5"
            } else if (v.id == R.id.mile10Button) {
                mile10Button.background = ContextCompat.getDrawable(this@WhereActivity, R.drawable.round_corner_white_stroke_green)
                distance="10"
            } else if (v.id == R.id.mile20Button) {
                mile20Button.background = ContextCompat.getDrawable(this@WhereActivity, R.drawable.round_corner_white_stroke_green)
                distance="20"
            } else if (v.id == R.id.mile40Button) {
                mile40Button.background = ContextCompat.getDrawable(this@WhereActivity, R.drawable.round_corner_white_stroke_green)
                distance="40"
            }

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)

                searchTextView.text=place.name

                selectedPos = place.latLng
            }
        }
    }


}