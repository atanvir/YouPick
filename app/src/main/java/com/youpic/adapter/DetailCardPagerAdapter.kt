package com.youpic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.youpic.R
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import java.util.*

class DetailCardPagerAdapter     //this.sliderImage=sliderImage;
(private val context: Context, private val name: String, var imageData: ArrayList<HomeDataDataModel.Data.Restaurant.ImageData>?) : PagerAdapter() {
    private val sliderImage: ArrayList<String>? = null
    override fun getCount(): Int {
        if(imageData==null||imageData!!.isEmpty())
            return 1
        else
            return imageData!!.size
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
        val view = inflater.inflate(R.layout.detail_card_view_pager_layout, null)

        // View view = LayoutInflater.from(context).inflate(R.layout.item_slider, container, false);

        setView(view, position)
        val viewPager = container as ViewPager
        viewPager.addView(view, 0)


        // container.addView(view);
        return view
    }

    private fun setView(view: View, position: Int) {
        (view.findViewById<View>(R.id.nameTextView) as TextView).text = name

        if(imageData==null||imageData!!.isEmpty()){
            Glide.with(context).load(R.drawable.no_images_yet).into(view.findViewById(R.id.restroImageView))
        }else{
            Glide.with(context).load(imageData!!.get(position).image).into(view.findViewById(R.id.restroImageView))
        }

    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        var view: View? = `object` as View
        viewPager.removeView(view)
        view = null
    }
}