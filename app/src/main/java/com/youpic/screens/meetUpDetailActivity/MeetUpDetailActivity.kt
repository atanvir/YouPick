package com.youpic.screens.meetUpDetailActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.youpic.R
import com.youpic.adapter.MeetUpDetailAdapter
import com.youpic.apiConnection.Body.WinnerAndSelectionBody
import com.youpic.apiConnection.DataModel.GetRestaurantForFinalCallDataModel
import com.youpic.baseClass.BaseActivity
import com.youpic.databinding.ActivityMeetUpDetailBinding
import com.youpic.screens.winnerActivity.WinnerActivity
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_meet_up_detail.*

class MeetUpDetailActivity : BaseActivity(), MeetUpDetailAdapter.OnRestroClickListener {

    lateinit var viewModel: MeetUpDetailViewModel
     var selectedRestro: GetRestaurantForFinalCallDataModel.Data.FinalRestaurant?=null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_meet_up_detail)

        init()
        initControl()
        myObserver()
    }


    override fun init()
    {
        findViewById<View>(R.id.backTextView).setOnClickListener { finish() }

        (findViewById<View>(R.id.titleTextView) as TextView).text = "Final Selection!"


        viewModel=ViewModelProvider(this).get(MeetUpDetailViewModel::class.java)

        viewModel.getRestaurantForFinalCallApi(this,intent.getStringExtra("meetingId")!!)

    }

    override fun initControl() {
        doneButton.setOnClickListener{

            if(selectedRestro==null){
                Utils.showMessage(this,"Please select final restaurant!", MSG_TOAST)
            }else{

                val body=WinnerAndSelectionBody(intent.getStringExtra("meetingId")!!
                        ,selectedRestro!!.restaurant.address
                , selectedRestro!!.restaurant.id
                ,selectedRestro!!.restaurant.latitude
                ,selectedRestro!!.restaurant.longitude
                ,selectedRestro!!.restaurant.name)
                viewModel.winnerAndSelectionApi(this,body)


            }
            }
    }

    override fun myObserver() {
        viewModel.getRestaurantForFinalCallResponse.observe(this,{
            if(it.status==SUCCESS){

                detailRecyclerView.layoutManager = LinearLayoutManager(this)
                detailRecyclerView.adapter = MeetUpDetailAdapter(this,it.data.finalRestaurant,this)

            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }
        })

        viewModel.winnerAndSelectionResponse.observe(this,{
            if(it.status== SUCCESS){
                startActivity(Intent(this@MeetUpDetailActivity, WinnerActivity::class.java).putExtra("isWinner", true).putExtras(intent.extras!!))
            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }

        })
    }

    override fun onClick(selectedRestro: GetRestaurantForFinalCallDataModel.Data.FinalRestaurant, pos: Int) {
        this.selectedRestro=selectedRestro
    }


}