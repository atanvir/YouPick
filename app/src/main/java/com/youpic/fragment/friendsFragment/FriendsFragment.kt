package com.youpic.fragment.friendsFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.youpic.R
import com.youpic.adapter.FriendsAdapter
import com.youpic.adapter.GroupAdapter
import com.youpic.apiConnection.Body.MeetingRequestBody
import com.youpic.apiConnection.DataModel.GetFriendListDataModel
import com.youpic.apiConnection.DataModel.GetFrndGroupListDataModel
import com.youpic.baseClass.BaseFragment
import com.youpic.screens.OccasionActivity
import com.youpic.screens.almostDoneActivity.AlmostDoneActivity
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.screens.nearActivity.NearActivity
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.DATE_FORMAT
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.FriendList
import com.youpic.utils.TakeImage
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.fragment_friends.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FriendsFragment : BaseFragment(), View.OnClickListener ,FriendsAdapter.OnFriendClick,GroupAdapter.OnGroupClick{


    lateinit var viewModel:FriendsViewModel
    lateinit var dataList: ArrayList<GetFriendListDataModel.Data>
    lateinit var groupList:ArrayList<GetFrndGroupListDataModel.Data>
    val selectedFriendList= HashMap<String,MeetingRequestBody.UsersData>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_friends,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedFriendList.clear()

        init()
        initCtrl()
        observer()



    }

    override fun init()
    {
        viewModel=ViewModelProvider(this).get(FriendsViewModel::class.java)
        if (arguments!!.getBoolean("openGroups", false)) {
            setLayout(R.id.favImageView)
        } else {
            setLayout(R.id.friendsImageView)
        }

    }

    override fun initCtrl() {
        friendsImageView.setOnClickListener(this)
        favImageView.setOnClickListener(this)
        doneButton.setOnClickListener(this)
    }

    override fun observer() {

        viewModel.getFriendListResponse.observe(requireActivity(),
                {
                    selectedFriendList.clear()
                    FriendList.selectedFriendList.clear()
                    if(it.status== SUCCESS){
                        friendsRecyclerView.layoutManager = LinearLayoutManager(activity)
                        dataList= it.data
                        if(dataList.isEmpty()){
                            noDateFoundTextView.visibility=View.VISIBLE
                            smileyImageView.visibility=View.VISIBLE
                            friendsRecyclerView.visibility=View.GONE
                        }else{
                            noDateFoundTextView.visibility=View.GONE
                            smileyImageView.visibility=View.GONE
                            friendsRecyclerView.visibility=View.VISIBLE
                        }
                        friendsRecyclerView.adapter = FriendsAdapter(requireActivity(),dataList,this,true)
                    }else{
                        Utils.showMessage(requireActivity(),it.message, MSG_TOAST)
                        noDateFoundTextView.visibility=View.VISIBLE
                    }

                })

        viewModel.removeFriendResponse.observeOnce(requireActivity(),{
            Utils.showMessage(requireActivity(),it.message,MSG_TOAST)
            viewModel.getFriendListApi(requireActivity())
        })

        viewModel.getFrndGroupListResponse.observe(requireActivity(),{
            selectedFriendList.clear()
            FriendList.selectedFriendList.clear()
            if(it.status== SUCCESS){
                if(it.data.isEmpty()){
                    noDateFoundTextView.visibility=View.VISIBLE
                    smileyImageView.visibility=View.VISIBLE
                    friendsRecyclerView.visibility=View.GONE
                }else{
                    noDateFoundTextView.visibility=View.GONE
                    smileyImageView.visibility=View.GONE
                    friendsRecyclerView.visibility=View.VISIBLE
                }
                groupList=it.data
                friendsRecyclerView.layoutManager = LinearLayoutManager(activity)
                friendsRecyclerView.adapter = GroupAdapter(context!!,groupList,this,true)
                //friendsRecyclerView.adapter = FriendsAdapter(requireActivity(),dataList,this,true)
            }else{
                Utils.showMessage(requireActivity(),it.message, MSG_TOAST)
                noDateFoundTextView.visibility=View.VISIBLE
            }

        })

        viewModel.removeGroupResponse.observe(requireActivity(),{
            Utils.showMessage(requireActivity(),it.message,MSG_TOAST)
            viewModel.getFrndGroupListApi(requireActivity())
        })
    }

    override fun onClick(v: View) {
        if (v.id == R.id.friendsImageView) {
            setLayout(v.id)
        } else if (v.id == R.id.favImageView) {
            setLayout(v.id)
        }else if(v.id==R.id.doneButton){

            FriendList.selectedFriendList=selectedFriendList
            if(FriendList.getFriendList(requireActivity()).isEmpty()){

                Utils.showMessage(requireActivity(), "Please select some contacts!", Constants.MSG_TOAST)

            }else{

                val fIntent=Intent(requireActivity(), OccasionActivity::class.java)



                /*val outputTimePattern = "HH:mm"
                try {
                    fIntent.putExtra("whenDate",SimpleDateFormat(DATE_FORMAT).format(Calendar.getInstance().time))
                    fIntent.putExtra("whenTime",SimpleDateFormat(outputTimePattern).format(Calendar.getInstance().time))

                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                fIntent.putExtra("when","Now")
                fIntent.putExtra("fromHome",true)*/


                fIntent.putExtra("fromHome",true)
                startActivity(fIntent)


            }



        }
    }

    private fun setLayout(id: Int)
    {
        if (id == R.id.friendsImageView) {
            /*selectedFriendList.clear()*/
            (activity as HomeActivity?)!!.setTitle("Friends")
            friendsImageView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.friend_select, 0, 0)
            friendsImageView.setTextColor(ContextCompat.getColor(activity!!, R.color.app_green))
            favImageView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.group_unselect, 0, 0)
            favImageView.setTextColor(ContextCompat.getColor(activity!!, R.color.grey))
            viewModel.getFriendListApi(requireActivity())
        } else if (id == R.id.favImageView) {
            /*selectedFriendList.clear()*/
            (activity as HomeActivity?)!!.setTitle("Groups")
            friendsImageView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.friend_unselect, 0, 0)
            friendsImageView.setTextColor(ContextCompat.getColor(activity!!, R.color.grey))
            favImageView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.group_select, 0, 0)
            favImageView.setTextColor(ContextCompat.getColor(activity!!, R.color.app_green))

            noDateFoundTextView.visibility=View.GONE
            smileyImageView.visibility=View.GONE
            friendsRecyclerView.visibility=View.VISIBLE
            viewModel.getFrndGroupListApi(requireActivity())

        }
    }

    private fun removeFriendBottomLayout(friendId: String?,data: GetFrndGroupListDataModel.Data?,name:String,position: Int) {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val sheetView: View = this.layoutInflater.inflate(R.layout.delete_bottom_dialog, null)
        bottomSheetDialog.setContentView(sheetView)
        bottomSheetDialog.window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent)
        val initTextView = sheetView.findViewById<TextView>(R.id.initTextView)
        val nameTextView = sheetView.findViewById<TextView>(R.id.nameTextView)
        val removeTextView = sheetView.findViewById<TextView>(R.id.removeTextView)
        val CancelTextView = sheetView.findViewById<TextView>(R.id.cancelTextView)


        nameTextView.text=name
        initTextView.text=name[0].toString()

        removeTextView.setOnClickListener{

            if(data!=null){
                viewModel.removeGroupApi(requireActivity(),data.id)
                /*groupList.removeAt(position)

                for (item in data.memberData){

                    selectedFriendList.remove(item.memberData.id)

                }


                friendsRecyclerView.adapter!!.notifyItemRemoved(position)*/
                bottomSheetDialog.dismiss()
                if(groupList.isEmpty()){
                    noDateFoundTextView.visibility=View.VISIBLE
                    smileyImageView.visibility=View.VISIBLE
                    friendsRecyclerView.visibility=View.GONE
                }else{
                    noDateFoundTextView.visibility=View.GONE
                    smileyImageView.visibility=View.GONE
                    friendsRecyclerView.visibility=View.VISIBLE
                }
            }else{
                viewModel.removeFriendApi(requireActivity(),friendId!!)
                /*selectedFriendList.remove(friendId)
                dataList.removeAt(position)
                friendsRecyclerView.adapter!!.notifyItemRemoved(position)*/
                bottomSheetDialog.dismiss()
                if(dataList.isEmpty()){
                    noDateFoundTextView.visibility=View.VISIBLE
                    smileyImageView.visibility=View.VISIBLE
                    friendsRecyclerView.visibility=View.GONE
                }else{
                    noDateFoundTextView.visibility=View.GONE
                    smileyImageView.visibility=View.GONE
                    friendsRecyclerView.visibility=View.VISIBLE
                }
            }






        }


        CancelTextView.setOnClickListener { bottomSheetDialog.dismiss() }
        bottomSheetDialog.show()
    }



    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observeForever(object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    override fun onSettingClick(friendId: String, position: Int) {
/*
        selectedFriendList.remove(friendId)
*/
        removeFriendBottomLayout(friendId,null,dataList.get(position).acceptToData.name,position)
    }

    override fun onItemClick(friendId: String, name: String, lat: String, _long: String, isRemove: Boolean, position: Int) {
        if(isRemove){
            selectedFriendList.remove(friendId)
        }else{
            selectedFriendList.put(friendId, MeetingRequestBody.UsersData(friendId,name,lat,_long))
        }
    }

    override fun onGroupClick(data: GetFrndGroupListDataModel.Data, isAdd: Boolean, position: Int) {

        for (item in data.memberData){

            if(isAdd){
                if(!requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)?.getString(Constants.PREF_USER_ID,"").equals(item.memberData.id)){

                    selectedFriendList.put(item.memberData.id,MeetingRequestBody.UsersData(item.memberData.id,
                            item.memberData.name,
                            item.memberData.lat,
                            item.memberData._long))
                }

            }else{
                selectedFriendList.remove(item.memberData.id)
            }

        }


    }

    override fun onGroupSettingClick(data: GetFrndGroupListDataModel.Data, name: String, position: Int)
    {
        removeFriendBottomLayout(null,data,name,position)
    }
}