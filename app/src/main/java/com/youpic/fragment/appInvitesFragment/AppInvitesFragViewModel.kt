package com.youpic.fragment.appInvitesFragment

import android.content.Context
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.hbb20.CountryCodePicker
import com.youpic.apiConnection.Body.SendFrndRequestBody
import com.youpic.apiConnection.Body.UpdatePhoneConatctBody
import com.youpic.apiConnection.DataModel.GetContactListDataModel
import com.youpic.apiConnection.DataModel.SendFrndRequestDataModel
import com.youpic.apiConnection.DataModel.UpdatePhoneConatctDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.Constants
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.Utils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap


class AppInvitesFragViewModel:ApiBaseViewModel() {


    var updatePhoneConatctResponse=MutableLiveData<UpdatePhoneConatctDataModel>()
    var getContactListDataModelResponse=MutableLiveData<GetContactListDataModel>()
    var sendFrndRequestResponse=MutableLiveData<SendFrndRequestDataModel>()
    var contactList=MutableLiveData<List<UpdatePhoneConatctBody.PhoneContact>>()


    fun updatePhoneConatctApi(context: Context, body: UpdatePhoneConatctBody){

        CustomProgressBar.getCustomDialog(context)

        apiInterface.updatePhoneConatctApi(Utils.getUserToken(context), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    CustomProgressBar.dismissDialog()
                    updatePhoneConatctResponse.value = it
                }, {
                    CustomProgressBar.dismissDialog()
                    ErrorHandler.handlerError(context, it)
                })

    }

    fun getContactListApi(context: Context){

        CustomProgressBar.getCustomDialog(context)

        apiInterface.getContactListApi(Utils.getUserToken(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    CustomProgressBar.dismissDialog()
                    getContactListDataModelResponse.value = it
                }, {
                    CustomProgressBar.dismissDialog()
                    ErrorHandler.handlerError(context, it)
                })

    }

    fun sendFrndRequestApi(context: Context,body: SendFrndRequestBody){
        CustomProgressBar.getCustomDialog(context)

        apiInterface.sendFrndRequestApi(Utils.getUserToken(context),body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    CustomProgressBar.dismissDialog()
                    sendFrndRequestResponse.value = it
                },{
                    CustomProgressBar.dismissDialog()
                    ErrorHandler.handlerError(context, it)
                })
    }

    fun getPhone(context: Context)
    {
        CustomProgressBar.getCustomDialog(context)
        getContact(context).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                   // CustomProgressBar.dismissDialog()
                    contactList.value = it
                            }, {
                    CustomProgressBar.dismissDialog()
                    ErrorHandler.handlerError(context,it)
                            })


    }

    fun getContact(context:Context):Observable<List<UpdatePhoneConatctBody.PhoneContact>>{
        return Observable.create{
            emit->

            val manager = context.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
            val ccp= CountryCodePicker(context)
            if(manager.simCountryIso.isNullOrEmpty()){
                ccp.setCountryForNameCode(manager.networkCountryIso)
            }else{
                ccp.setCountryForNameCode(manager.simCountryIso)
            }

            Log.w("testCountryCode",ccp.selectedCountryCode )
            val contactList: MutableMap<String,UpdatePhoneConatctBody.PhoneContact> = LinkedHashMap<String,UpdatePhoneConatctBody.PhoneContact>()


            val cr = context.contentResolver
            val PROJECTION = arrayOf(
                    ContactsContract.RawContacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    Phone.NUMBER)
            val uri = Phone.CONTENT_URI
            val filter = "" + ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0"/* and " + Phone.TYPE + "=" + Phone.TYPE_MOBILE*/
            val order = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE NOCASE ASC" // LIMIT " + limit + " offset " + lastId + "";
            val phoneCur = cr.query(uri, PROJECTION, filter, null, order)



            while (phoneCur!!.moveToNext())
            {
                var numberWithCountrCodePlus:String

                if(phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER))[0].toString().equals("+",true)){


                    numberWithCountrCodePlus=phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)).replace(" ", "")


                }else{

                    if(("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)))[0].toString().equals("0",true)){

                        numberWithCountrCodePlus = ("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER))).replace(" ", "")
                        numberWithCountrCodePlus="+"+ccp.selectedCountryCode+numberWithCountrCodePlus.substring(1)

                    }else{
                        numberWithCountrCodePlus="+"+ccp.selectedCountryCode+phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)).replace(" ", "")
                    }
                }

                Log.w("testContact","Number: "+numberWithCountrCodePlus+" Name: "+phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                if(!contactList.containsKey(numberWithCountrCodePlus)){
                    val number=(phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)))
                            .replace(" ", "")
                    var num:Phonenumber.PhoneNumber
                    var body:UpdatePhoneConatctBody.PhoneContact
                    if(number[0].toString().equals("+",true)){

                        try{
                            num=PhoneNumberUtil.getInstance()
                                    .parse(number, "")

                            body=UpdatePhoneConatctBody.PhoneContact("+"+num.countryCode,
                                    ("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)))
                                            .replace(" ", "")
                                            .replace("-","")
                                            .replace("(","")
                                            .replace(")","")
                                            .replace("+"+num.countryCode,""),
                                    "" + phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                        }catch (e:Exception){
                            body=UpdatePhoneConatctBody.PhoneContact("+"+ccp.selectedCountryCode,
                                    ("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)))
                                            .replace(" ", "")
                                            .replace("-","")
                                            .replace("(","")
                                            .replace(")",""),
                                    "" + phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                        }

                    }else{

                        if(("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)))[0].toString().equals("0",true)){

                            var number:String = ("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)))
                                    .replace(" ", "")
                                    .replace("-","")
                                    .replace("(","")
                                    .replace(")","")
                            body=UpdatePhoneConatctBody.PhoneContact("+"+ccp.selectedCountryCode,
                                    number.substring(1),
                                    "" + phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                        }else{
                            body=UpdatePhoneConatctBody.PhoneContact("+"+ccp.selectedCountryCode,
                                    ("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)))
                                            .replace(" ", "")
                                            .replace("-","")
                                            .replace("(","")
                                            .replace(")",""),
                                    "" + phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                        }

                    }




                    if(body.countryCode.equals(context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE).getString(Constants.PREF_COUNTRY_CODE, "")) &&
                            body.mobileNumber.equals(context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE).getString(Constants.PREF_USER_NUMBER, ""))){

                    }else{
                        contactList.put(body.countryCode+body.mobileNumber,body)
                        Log.w("insertedContacts","inserted Number: "+body.countryCode+body.mobileNumber+" Name: "+body.name)

                    }


                }else{
                    Log.w("rejectedContacts","rejected: "+numberWithCountrCodePlus)

                }


            }
            phoneCur.close()

            Log.w("testContacts","contactList: "+contactList.toString())
                emit.onNext(ArrayList(contactList.values))


        }
    }

    /*fun getContact(context:Context):Observable<List<UpdatePhoneConatctBody.PhoneContact>>{
        return Observable.create{
            emit->

            val manager = context.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
            val ccp= CountryCodePicker(context)
            if(manager.simCountryIso.isNullOrEmpty()){
                ccp.setCountryForNameCode(manager.networkCountryIso)
            }else{
                ccp.setCountryForNameCode(manager.simCountryIso)
            }

            Log.w("testCountryCode",ccp.selectedCountryCode )
            val contactList: MutableMap<String,UpdatePhoneConatctBody.PhoneContact> = LinkedHashMap<String,UpdatePhoneConatctBody.PhoneContact>()
            val cr = context.contentResolver
            val PROJECTION = arrayOf(
                    ContactsContract.RawContacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.RawContacts.VERSION,
                    Phone.PHOTO_URI,
                    Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Photo.CONTACT_ID)
            val uri = Phone.CONTENT_URI
            val filter = "" + ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0"*//* and " + Phone.TYPE + "=" + Phone.TYPE_MOBILE*//*
            val order = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE NOCASE ASC" // LIMIT " + limit + " offset " + lastId + "";
            val phoneCur = cr.query(uri, PROJECTION, filter, null, order)
            while (phoneCur!!.moveToNext())
            {
                try{
                    Log.w("testContactVersion",phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.RawContacts.VERSION)))

                }catch (e:Exception){
                    Log.w("testContactVersion",e.message!!)
                }

                var numberWithCountrCodePlus:String

                if(phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER))[0].toString().equals("+",true)){


                    numberWithCountrCodePlus=phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)).replace(" ", "")


                }else{

                    if(("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)))[0].toString().equals("0",true)){

                        numberWithCountrCodePlus = ("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER))).replace(" ", "")
                        numberWithCountrCodePlus="+"+ccp.selectedCountryCode+numberWithCountrCodePlus.substring(1)

                    }else{
                        numberWithCountrCodePlus="+"+ccp.selectedCountryCode+phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)).replace(" ", "")
                    }
                }

                Log.w("testContact","Number: "+numberWithCountrCodePlus+" Name: "+phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                if(!contactList.containsKey(numberWithCountrCodePlus)){
                    val number=(phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)))
                            .replace(" ", "")
                    var num:Phonenumber.PhoneNumber
                    var body:UpdatePhoneConatctBody.PhoneContact
                    if(number[0].toString().equals("+",true)){

                        try{
                            num=PhoneNumberUtil.getInstance()
                                    .parse(number, "")

                            body=UpdatePhoneConatctBody.PhoneContact("+"+num.countryCode,
                                    ("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER))).replace(" ", "").replace("+"+num.countryCode,""),
                                    "" + phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                        }catch (e:Exception){
                            body=UpdatePhoneConatctBody.PhoneContact("+"+ccp.selectedCountryCode,
                                    ("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER))).replace(" ", ""),
                                    "" + phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                        }

                    }else{

                        if(("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER)))[0].toString().equals("0",true)){

                            var number:String = ("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER))).replace(" ", "")
                            body=UpdatePhoneConatctBody.PhoneContact("+"+ccp.selectedCountryCode,
                                    number.substring(1),
                                    "" + phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))

                        }else{
                            body=UpdatePhoneConatctBody.PhoneContact("+"+ccp.selectedCountryCode,
                                    ("" + phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER))).replace(" ", ""),
                                    "" + phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                        }

                    }




                    if(body.countryCode.equals(context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE).getString(Constants.PREF_COUNTRY_CODE, "")) &&
                            body.mobileNumber.equals(context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE).getString(Constants.PREF_USER_NUMBER, ""))){

                    }else{
                        contactList.put(body.countryCode+body.mobileNumber,body)
                        Log.w("insertedContacts","inserted Number: "+body.countryCode+body.mobileNumber+" Name: "+body.name)

                    }


                }else{
                    Log.w("rejectedContacts","rejected: "+numberWithCountrCodePlus)

                }


            }
            phoneCur.close()

            Log.w("testContacts","contactList: "+contactList.toString())
            emit.onNext(ArrayList(contactList.values))


        }
    }*/

}