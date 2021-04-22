package com.youpic.baseClass

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    abstract fun init()
    abstract fun initCtrl()
    abstract fun observer()

}