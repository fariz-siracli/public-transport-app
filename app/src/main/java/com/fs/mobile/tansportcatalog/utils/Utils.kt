package com.fs.mobile.tansportcatalog.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.fs.mobile.tansportcatalog.R
import java.io.*

class Utils {

    companion object {

        fun copyDataBaseToApp(activity: Activity, dbName: String) {
            val outFileName = activity.getDatabasePath(dbName).getPath()
            val dir = File(activity.getDatabasePath(dbName).getParent())
            dir.mkdir()
            // Open the empty mainDb as the output stream
            val outputFile = File(outFileName)
            try {
                outputFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            var myInput: InputStream? = null
            var myOutput: OutputStream? = null
            try {
                myInput = activity.getAssets().open(dbName)
                myOutput = FileOutputStream(outputFile)
                // transfer bytes from the inputfile to the outputfile
                var buffer = ByteArray(1024)
                var length = 0
                while (myInput.read(buffer).let { length = it; it > 0 }) {
                    myOutput.write(buffer, 0, length)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    myInput!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    myOutput!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun setPreference(context: Context, key: String, value: String) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putString(key, value)
            editor.commit()
        }

        fun getResource(context: Context, resource:Int): Drawable {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.getDrawable(resource)
            } else {
                context.resources.getDrawable(resource)
            }
        }

        fun checkDatabaseExistence(context: Context, fileName: String): Boolean {
            var db = context.getDatabasePath(fileName);
            return db.exists()
        }

        fun log(text: String) {
            try {
                Crashlytics.log(text)
                Log.i("TRANS_CATALOG", text)
            } catch (x: Exception) {
                x.stackTrace
            }
        }
    }

}