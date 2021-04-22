package com.youpic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.youpic.R
import com.youpic.apiConnection.DataModel.GetContactListDataModel
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.phone_contact_layout.view.*

public class PhoneContactAdapter2(var context: Context, var contactList: List<GetContactListDataModel.Data>?, var contactClick: ContactClick?): RecyclerView.Adapter<PhoneContactAdapter2.ViewHolder>()
{
    private var count=0
    private var temp:List<GetContactListDataModel.Data>?=null

    init {
        temp=contactList
    }

    public interface ContactClick{
        fun onClick(data: GetContactListDataModel.Data?, isSelected: Boolean, position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.phone_contact_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.nameTextView.text= contactList?.get(position)?.name
        holder.itemView.initTextView.text= contactList?.get(position)?.name?.get(0).toString()
        /*holder.itemView.numberTextView.text= contactList?.get(position)?.mobileNumber.toString()
        holder.itemView.numberTextView.visibility=View.VISIBLE*/

        /*if(contactList?.get(position)?.isExist == true){
            holder.itemView.invitedImageView.visibility=View.VISIBLE
        }else{
            holder.itemView.invitedImageView.visibility=View.GONE
        }*/

        if(contactList?.get(position)?.isExist==true){
            holder.itemView.flagImageView.visibility=View.VISIBLE
        }else{
            holder.itemView.flagImageView.visibility=View.INVISIBLE
        }

        if(contactList?.get(position)?.isSelected==true){
            holder.itemView.invitedImageView.visibility=View.VISIBLE
        }else{
            holder.itemView.invitedImageView.visibility=View.INVISIBLE
        }

        if(contactList?.get(position)?.isRequest==true){
            holder.itemView.tickImageView.visibility=View.VISIBLE
        }else{
            holder.itemView.tickImageView.visibility=View.GONE
        }

        /*if(contactList?.get(position)?.isFriend==true){
            holder.itemView.flagImageView.visibility=View.VISIBLE
        }else{
            holder.itemView.flagImageView.visibility=View.GONE
        }*/

    }

    override fun getItemCount(): Int {

        if(contactList==null)
            return 0
        else
            return contactList!!.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {

                if(!contactList!!.get(adapterPosition).isRequest){
                    if(!contactList!!.get(adapterPosition).isFriend){
                        var selected=false

                        if(itemView.invitedImageView.visibility==View.INVISIBLE){

                            if((count+1)<=10)
                            {
                                itemView.invitedImageView.visibility=View.VISIBLE
                                selected=true
                                count++

                            }else{
                                Utils.showMessage(context, "Max contact selected", MSG_TOAST)
                            }

                        }else{
                            itemView.invitedImageView.visibility=View.INVISIBLE
                            selected=false
                            count--
                        }

                        contactList?.get(adapterPosition)!!.isSelected=selected

                        if(contactClick!=null){
                            contactClick!!.onClick(contactList?.get(adapterPosition), selected, adapterPosition)
                        }
                    }else{
                        Utils.showMessage(context, "Already in your friendlist!", MSG_TOAST)
                    }

                }else{
                    Utils.showMessage(context, "Already Invited!", MSG_TOAST)
                }





            }
        }

    }


    @NonNull
    fun getFilter(): Filter? {
        return object : Filter() {
            protected override fun performFiltering(constraint: CharSequence?): FilterResults? {
                var alreadyInserted = false
                val results = FilterResults()
                val tempFilterData: ArrayList<GetContactListDataModel.Data> = ArrayList()
                if (constraint == null || constraint.toString().trim { it <= ' ' }.length == 0) {
                    results.values = temp
                } else {
                    val constrainString = constraint.toString().toLowerCase()
                    for (data in temp!!) {
                        alreadyInserted = false
                        if (data.name.toLowerCase().contains(constrainString)||data.mobileNumber.toLowerCase().contains(constrainString)) {
                            tempFilterData.add(data)
                        }

                        /*if(!alreadyInserted)
                          {
                              for(DishListDataModel.Dishlist dishName:data.getChefName())
                              {
                                  if(dishName.getDishName().toLowerCase().contains(constrainString))
                                  {
                                      tempFilterData.add(data);
                                      break;
                                  }
                              }
                          }*/
                    }
                    results.count = tempFilterData.size
                    results.values = tempFilterData
                }
                return results
            }

            protected override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                if (results.values != null) {
                    contactList = results.values as ArrayList<GetContactListDataModel.Data>
                    notifyDataSetChanged()
                }
            }
        }
    }


}