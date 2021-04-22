package com.youpic.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.youpic.R
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import kotlinx.android.synthetic.main.home_pager_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class HomePagerAdapter     //this.sliderImage=sliderImage;
(private val context: Context,var imageList:ArrayList<HomeDataDataModel.Data.Restaurant.ImageData>?,var name:String,var isDetail2: Boolean) : PagerAdapter() {
    private val sliderImage: ArrayList<String>? = null
    override fun getCount(): Int {
        if(imageList==null||imageList!!.isEmpty())
            return 1
        else
            return imageList!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }



    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        /*LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        ImageView imageView=view.findViewById(R.id.imageView);
        TextView imageNameTextView=view.findViewById(R.id.imageNameTextView);


        imageView.setImageResource(data[position].getImage());
        imageNameTextView.setText(data[position].getName());

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view);*/
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.home_pager_layout, null)

        // View view = LayoutInflater.from(context).inflate(R.layout.item_slider, container, false);
        setView(view, position)
        val viewPager = container as ViewPager
        viewPager.addView(view, 0)


        // container.addView(view);
        return view
    }

    private fun setView(view: View, position: Int) {

        if(imageList==null||imageList!!.isEmpty()){
            Glide.with(context).load(R.drawable.no_images_yet).into(view.imageView)
        }else{

            Glide.with(context).load(imageList!!.get(position).image)
//                    .format(DecodeFormat.PREFER_ARGB_8888)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view.imageView)
        }

        if(isDetail2){
            view.typeTextView.visibility=View.INVISIBLE
        }else{
            view.typeTextView.visibility=View.VISIBLE
            if(imageList.isNullOrEmpty()){
                Log.w("testName","frist"+name)
                view.typeTextView.text=name
            }else{
                Log.w("testName","sec"+imageList!!.get(position).name)

                if(imageList!!.get(position).name.isNullOrEmpty()){
                    view.typeTextView.text=name
                }else{
                    view.typeTextView.text=imageList!!.get(position).name
                }

            }
        }


        /*if(imageList.get(position)!=null && imageList.get(position).image!=null)
            Glide.with(context).load(imageList.get(position).image).into(view.imageView)*/

      //  view.imageView
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        var view: View? = `object` as View
        viewPager.removeView(view)
        view = null
    }
}