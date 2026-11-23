package com.lakbay.tuklas

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class TuklasTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application =
        super.newApplication(cl, TuklasTestApplication::class.java.name, context)
}
