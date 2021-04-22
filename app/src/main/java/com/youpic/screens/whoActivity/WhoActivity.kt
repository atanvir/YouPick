package com.youpic.screens.whoActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.youpic.R
import com.youpic.adapter.FriendsAdapter
import com.youpic.adapter.GroupAdapter
import com.youpic.apiConnection.Body.MeetingRequestBody
import com.youpic.apiConnection.DataModel.GetFriendListDataModel
import com.youpic.apiConnection.DataModel.GetFrndGroupListDataModel
import com.youpic.baseClass.BaseActivity
import com.youpic.fragment.friendsFragment.FriendsViewModel
import com.youpic.screens.almostDoneActivity.AlmostDoneActivity
import com.youpic.screens.nearActivity.NearActivity
import com.youpic.utils.Constants
import com.youpic.utils.FriendList
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_who.*
import kotlinx.android.synthetic.main.activity_who.doneButton
import kotlinx.android.synthetic.main.activity_who.favImageView
import kotlinx.android.synthetic.main.activity_who.friendsImageView
import kotlinx.android.synthetic.main.activity_who.friendsRecyclerView
import kotlinx.android.synthetic.main.activity_who.noDateFoundTextView
import kotlinx.android.synthetic.main.activity_who.smileyImageView
import kotlinx.android.synthetic.main.common_titlebar.*
import kotlinx.android.synthetic.main.fragment_friends.*

class WhoActivity : BaseActivity(), View.OnClickListener,FriendsAdapter.OnFriendClick,GroupAdapter.OnGroupClick
{
    lateinit var friendViewModel:FriendsViewModel
    lateinit var dataList: ArrayList<GetFriendListDataModel.Data>
    val selectedFriendList= HashMap<String,MeetingRequestBody.UsersData>()
    lateinit var groupList:ArrayList<GetFrndGroupListDataModel.Data>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_who)

        Utils.printAllExtras(intent.extras!!)

        FriendList.selectedFriendList.clear()

        init()
        initControl()
        myObserver()




    }

    override fun onClick(v: View) {
        if (v.id == R.id.friendsImageView) {
            FriendList.selectedFriendList.clear()
            setLayout(v.id)
        } else if (v.id == R.id.favImageView) {
            FriendList.selectedFriendList.clear()
            setLayout(v.id)
        } else if (v.id == R.id.doneButton)
        {
            //val selectedFav:List<MeetingRequestBody.UsersData?> = ArrayList(selectedFriendList?.values)

                FriendList.selectedFriendList=selectedFriendList
            if(FriendList.getFriendList(this).isEmpty()){

                Utils.showMessage(this, "Please select some contacts!", Constants.MSG_TOAST)

            }else{

                val mIntent:Intent
                if(intent.getBooleanExtra("locationDetail", false))
                {
                    mIntent=Intent(this@WhoActivity, AlmostDoneActivity::class.java)
                            .putExtra("locationDetail",intent.getBooleanExtra("locationDetail", false))

                }else{
                    mIntent=Intent(this@WhoActivity, NearActivity::class.java)
                }

                mIntent.putExtras(intent.extras!!)
                startActivity(mIntent)


            }

        }
    }

    private fun setLayout(id: Int)
    {
        if (id == R.id.friendsImageView)
        {
            selectedFriendList.clear()
            noDateFoundTextView.visibility=View.GONE
            smileyImageView.visibility=View.GONE
            if(intent.getBooleanExtra("locationDetail", false)){
                titleTextView.text="Friends"
            }else{
                (findViewById<View>(R.id.titleTextView) as TextView).text = "Who?"
            }

            friendsImageView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.friend_select, 0, 0)
            friendsImageView.setTextColor(ContextCompat.getColor(this@WhoActivity, R.color.app_green))
            favImageView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.group_unselect, 0, 0)
            favImageView.setTextColor(ContextCompat.getColor(this@WhoActivity, R.color.grey))

            friendViewModel.getFriendListApi(this)

        } else if (id == R.id.favImageView) {
            selectedFriendList.clear()
            noDateFoundTextView.visibility=View.GONE
            smileyImageView.visibility=View.GONE

            if(intent.getBooleanExtra("locationDetail", false)){
                titleTextView.text="Groups"
            }else{
                (findViewById<View>(R.id.titleTextView) as TextView).text = "Who's Invited?"
            }

            friendsImageView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.friend_unselect, 0, 0)
            friendsImageView.setTextColor(ContextCompat.getColor(this@WhoActivity, R.color.grey))
            favImageView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.group_select, 0, 0)
            favImageView.setTextColor(ContextCompat.getColor(this@WhoActivity, R.color.app_green))

            friendViewModel.getFrndGroupListApi(this)
        }
    }

    override fun init() {

        if(intent.getBooleanExtra("locationDetail", false)){
            titleTextView.text="Friends"
        }

        friendViewModel=ViewModelProvider(this).get(FriendsViewModel::class.java)

    }

    override fun initControl() {
        setLayout(R.id.friendsImageView)
        favImageView.setOnClickListener(this)
        friendsImageView.setOnClickListener(this)
        doneButton.setOnClickListener(this)
        findViewById<View>(R.id.backTextView).setOnClickListener { v: View? -> finish() }
    }

    override fun myObserver()
    {
        friendViewModel.getFriendListResponse.observe(this,
                {
                    if(it.status== Constants.SUCCESS){
                        friendsRecyclerView.layoutManager = LinearLayoutManager(this)
                         dataList= it.data
                        if(dataList==null||dataList.isEmpty()){
                            noDateFoundTextView.visibility=View.VISIBLE
                            smileyImageView.visibility=View.VISIBLE
                            friendsRecyclerView.visibility=View.GONE
                        }else{
                            noDateFoundTextView.visibility=View.GONE
                            smileyImageView.visibility=View.GONE
                            friendsRecyclerView.visibility=View.VISIBLE
                        }
                        friendsRecyclerView.adapter = FriendsAdapter(this,dataList,this,true)
                    }else{
                        Utils.showMessage(this,it.message, Constants.MSG_TOAST)
                        noDateFoundTextView.visibility=View.VISIBLE
                    }

                })

        friendViewModel.getFrndGroupListResponse.observe(this,{

            if(it.status== Constants.SUCCESS)
            {
                groupList=it.data
                if(it.data.isEmpty()){
                    noDateFoundTextView.visibility=View.VISIBLE
                    smileyImageView.visibility=View.VISIBLE
                    friendsRecyclerView.visibility=View.GONE
                }else{
                    noDateFoundTextView.visibility=View.GONE
                    smileyImageView.visibility=View.GONE
                    friendsRecyclerView.visibility=View.VISIBLE
                }
                friendsRecyclerView.layoutManager = LinearLayoutManager(this)
                friendsRecyclerView.adapter = GroupAdapter(this!!,it.data,this@WhoActivity,true)
                //friendsRecyclerView.adapter = FriendsAdapter(requireActivity(),dataList,this,true)
            }else{
                Utils.showMessage(this,it.message, Constants.MSG_TOAST)
                noDateFoundTextView.visibility=View.VISIBLE
            }

        })

        friendViewModel.removeGroupResponse.observe(this,{
            Utils.showMessage(this,it.message, Constants.MSG_TOAST)
            friendViewModel.getFrndGroupListApi(this)
            selectedFriendList.clear()
            FriendList.selectedFriendList.clear()
        })

        friendViewModel.removeFriendResponse.observe(this,{
            Utils.showMessage(this,it.message, Constants.MSG_TOAST)
            friendViewModel.getFriendListApi(this)
            selectedFriendList.clear()
            FriendList.selectedFriendList.clear()
        })
    }



    override fun onItemClick(friendId: String, name: String, lat: String, _long: String, isRemove: Boolean, position: Int)
    {
        if(isRemove){
            selectedFriendList.remove(friendId)
        }else{
            selectedFriendList.put(friendId,MeetingRequestBody.UsersData(friendId,name,lat,_long))
        }
    }

    override fun onGroupClick(data: GetFrndGroupListDataModel.Data, isAdd: Boolean, position: Int) {
        for (item in data.memberData){

            if(isAdd){
                if(!getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)?.getString(Constants.PREF_USER_ID,"").equals(item.memberData.id)){
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

    override fun onGroupSettingClick(data: GetFrndGroupListDataModel.Data, name: String, position: Int) {

        removeFriendBottomLayout(null,data,name,position)

    }

    override fun onSettingClick(friendId: String, position: Int) {
        removeFriendBottomLayout(friendId,null,dataList.get(position).acceptToData.name,position)
    }

    private fun removeFriendBottomLayout(friendId: String?,data: GetFrndGroupListDataModel.Data?,name:String,position: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
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


                /*for (item in data.memberData){

                    selectedFriendList.remove(item.memberData.id)

                }*/

                friendViewModel.removeGroupApi(this,data.id)
                /*groupList.removeAt(position)
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


               /* selectedFriendList.remove(friendId)*/

                friendViewModel.removeFriendApi(this,friendId!!)

                /*dataList.removeAt(position)
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


}