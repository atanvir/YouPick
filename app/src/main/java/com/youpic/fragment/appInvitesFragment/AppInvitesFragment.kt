package com.youpic.fragment.appInvitesFragment

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.youpic.R
import com.youpic.adapter.PhoneContactAdapter2
import com.youpic.apiConnection.Body.SendFrndRequestBody
import com.youpic.apiConnection.Body.UpdatePhoneConatctBody
import com.youpic.apiConnection.DataModel.GetContactListDataModel
import com.youpic.baseClass.BaseFragment
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.PREF_USER_ID
import com.youpic.utils.Constants.Companion.PREF_USER_NAME
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.HomeData
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.fragment_app_invites.*


class AppInvitesFragment : BaseFragment(), PhoneContactAdapter2.ContactClick,View.OnClickListener {

    lateinit var viewModel:AppInvitesFragViewModel
    val PERMISSION_CODE=121
    var broadcastReceiver: BroadcastReceiver?=null
    var selectedContact: MutableMap<Int, SendFrndRequestBody.RequestData?>?= HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_app_invites, container, false)

     //   requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
      //  requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initCtrl()
        observer()

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {


                /*if(intent!!.getStringExtra("typeNoti")==NOTI_TYPE_MEETING_REQ_ACCEPT){
                    viewModel.homeDataApi(requireActivity())
                }*/
                selectedContact!!.clear()
                viewModel.getContactListApi(requireActivity())


            }
        }
    }

    override fun onResume() {
        super.onResume()
        searchEditText.text.clear()
        if(broadcastReceiver!=null)
        {

            requireActivity().registerReceiver(broadcastReceiver, IntentFilter("com.youpick"));
            Log.w("testBroadcast", "Broadcast register")
        }
    }

    override fun init() {

        selectedContact!!.clear()


        viewModel=ViewModelProvider(this).get(AppInvitesFragViewModel::class.java)
       // viewModel.getPhone(requireActivity())
        checkPermission()
    }

    override fun initCtrl() {
        sendButton.setOnClickListener(this)

        swipeRefreshLayout.setOnRefreshListener {
            searchEditText.text.clear()
            refreshData()
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (appInviteRecycler.adapter != null) {
                    (appInviteRecycler.adapter as PhoneContactAdapter2).getFilter()!!.filter(s)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

    }

    override fun observer() {

        viewModel.getContactListDataModelResponse.observe(requireActivity(), {

            if (it.status.equals(SUCCESS, false)) {

                if (it.data == null || it.data!!.isEmpty()) {
                    viewModel.getPhone(requireActivity())
                } else {

                    // var contactList=ArrayList<GetContactListDataModel.Data>()
                    var contactList = it.data

                    HomeData.getContactListDataModelResponse = it

                    /*for (data in it.data!!){
                        if(data.isExist){
                            contactList.add(data)
                        }
                    }






                    for (data in it.data!!){
                        if(!data.isExist){
                            contactList.add(data)
                        }
                    }*/

                    Log.w("testContact", contactList.toString())


                    appInviteRecycler.layoutManager = LinearLayoutManager(activity)
                    appInviteRecycler.adapter = PhoneContactAdapter2(requireActivity(), contactList, this)
                }

            } else {
                Utils.showMessage(requireActivity(),
                        it.message,
                        MSG_TOAST)
            }
        })

        viewModel.updatePhoneConatctResponse.observe(requireActivity(), {
            if (it.status.equals(SUCCESS, false)) {

                viewModel.getContactListApi(requireActivity())

            } else {
                Utils.showMessage(requireActivity(), it.message, MSG_TOAST)
            }
        })

        viewModel.sendFrndRequestResponse.observe(requireActivity(), {
            if (it.status.equals(SUCCESS, false)) {
                searchEditText.text.clear()
                selectedContact!!.clear()
                viewModel.getContactListApi(requireActivity())

                Utils.showMessage(requireActivity(), it.message, MSG_TOAST)

            } else {
                Utils.showMessage(requireActivity(), it.message, MSG_TOAST)
            }
        })


        viewModel.contactList.observe(requireActivity(), {
            viewModel.updatePhoneConatctApi(requireActivity(), UpdatePhoneConatctBody(it))
        })


    }


    private fun checkPermission() {


        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_CONTACTS)==PackageManager.PERMISSION_GRANTED) {
            setContacts()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS), PERMISSION_CODE)
            }
        }
    }

    private fun refreshData(){
        swipeRefreshLayout.isRefreshing=false
        HomeData.getContactListDataModelResponse=null
        selectedContact!!.clear()
        checkPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    setContacts()
                }
            }

        }

    }



    private fun setContacts(){

        //viewModel.getContactListApi(requireActivity())
       // viewModel.updatePhoneConatctApi(requireActivity(), UpdatePhoneConatctBody(viewModel.getPhone(requireActivity())))

        if(HomeData.getContactListDataModelResponse==null)
            viewModel.getPhone(requireActivity())
        else{
            appInviteRecycler.layoutManager = LinearLayoutManager(activity)
            appInviteRecycler.adapter = PhoneContactAdapter2(requireActivity(), HomeData.getContactListDataModelResponse!!.data, this)
        }


    }

    override fun onClick(data: GetContactListDataModel.Data?, isSelected: Boolean, position: Int) {

        if(isSelected){
            val tempData= SendFrndRequestBody.RequestData(data!!.countryCode, data.isExist, data.mobileNumber)

            selectedContact?.put(position, tempData)
        }else{
            selectedContact?.remove(position)
        }

    }

    override fun onClick(v: View) {
        if(v.id==R.id.sendButton){



            val selectedFav:List<SendFrndRequestBody.RequestData?> = ArrayList(selectedContact?.values)

            if(selectedFav==null || selectedFav.isEmpty()){

                Utils.showMessage(requireActivity(), "Please select some contacts!", MSG_TOAST)

            }else{
               // setInvites(selectedFav)
                getDeepLink(selectedFav)

            }


        }
    }

    private fun getDeepLink(selectedFav : List<SendFrndRequestBody.RequestData?>) {
        try {
            val shareLing = "https://youpick.page.link/?" +
                    "link=https://youpick.com/?Fid="+ requireActivity().getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).getString(PREF_USER_ID,"") +
                    "&st=You Pick!"+
                    "&apn=com.youpic"+
                    "&sd=Hi "+requireActivity().getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).getString(PREF_USER_NAME,"")+" has sent you a friend request!" +
                    "&si=" + "https://res.cloudinary.com/a2karya80559188/image/upload/v1617170097/appstore_ebngne.jpg" +
                    "&ibi=org.app.YouPick2.mobulous" +
                    "&afl=https://play.google.com/store/apps/details?id=com.youpic"+
                    "&isi=1537616399"
            FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(Uri.parse(shareLing))
                    .buildShortDynamicLink()
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            viewModel.sendFrndRequestApi(requireActivity(), SendFrndRequestBody(selectedFav,it.result.shortLink.toString()))
                        }
                    }
        } catch (e: Exception) {
            Log.e("testError",e.message?:"")
        }




    }

    override fun onPause() {
        super.onPause()

        if(broadcastReceiver!=null)
        {
            requireActivity().unregisterReceiver(broadcastReceiver);
            Log.w("testBroadcast", "Broadcast Remove")
        }
    }


    fun setInvites(selectedFav: List<SendFrndRequestBody.RequestData?>){
        var number="";

        for(result in selectedFav){
            number+=result!!.countryCode+result!!.mobileNumber+";"
        }

        val smsIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number))
        smsIntent.putExtra("sms_body", "sms message goes here")
        startActivity(smsIntent)

        /* String separator = "; ";
              if(android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")){
                separator = ", ";
              }
            try {

                 Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                 sendIntent.putExtra("address", "55555"+seperator+"66666");
                 sendIntent.putExtra("sms_body", "Here is My text");
                 sendIntent.setType("vnd.android-dir/mms-sms");
                 startActivity(sendIntent);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }*/

    }



}