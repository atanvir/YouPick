package com.youpic.baseClass

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {
    abstract fun init()
    abstract fun initControl()
    abstract fun myObserver()
}