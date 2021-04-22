package com.youpic.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.youpic.R
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import kotlinx.android.synthetic.main.card_view_pager_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class CardPagerAdapter     //this.sliderImage=sliderImage;
(private val context: Context,var imageList:ArrayList<HomeDataDataModel.Data.Restaurant.ImageData>?,var restroName:String) : PagerAdapter() {
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
        val view = inflater.inflate(R.layout.card_view_pager_layout, null)

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
            Glide.with(context).load(imageList!!.get(position).image).into(view.imageView)
        }

       // Glide.with(context).load(imageList.get(position).image).into(view.imageView)


        view.restroNameTextView.text=restroName

      //  view.imageView
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        var view: View? = `object` as View
        viewPager.removeView(view)
        view = null
    }
}