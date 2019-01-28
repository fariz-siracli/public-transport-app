package com.fs.mobile.tansportcatalog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.fs.mobile.tansportcatalog.db.AppDatabase
import com.fs.mobile.tansportcatalog.utils.Constants
import com.fs.mobile.tansportcatalog.utils.Constants.Companion.DB_NAME
import com.fs.mobile.tansportcatalog.utils.Constants.Companion.DB_NAME_FULL_NAME
import com.fs.mobile.tansportcatalog.utils.MyContextWrapper
import com.fs.mobile.tansportcatalog.utils.Utils
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.language_view.view.*
import java.util.*


var database: AppDatabase? = null
var currentPage = 0

class MainActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    var context: Context? = null
    private var doubleBackToExitPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fabric.with(
            Fabric.Builder(this)
                .kits(Crashlytics())
                .appIdentifier(BuildConfig.APPLICATION_ID)
                .build()
        )
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Constants.DB_NAME_FULL_NAME = getDatabasePath(DB_NAME).getParent() + "/" + DB_NAME_FULL_NAME
        Utils.log(Constants.DB_NAME_FULL_NAME)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        context = this

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                currentPage = position
            }

            override fun onPageSelected(position: Int) {
            }
        })

        // if(!Utils.checkDatabaseExistence(this, Constants.DB_NAME))

        database = AppDatabase.getAppDataBase(this)
        Utils.copyDataBaseOfApp(this, Constants.DB_NAME)
    }


    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.press_back), Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun attachBaseContext(newBase: Context) {
        val context = MyContextWrapper.wrap(newBase)
        super.attachBaseContext(context)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            changeLanguageDialog()
            return true
        } else if (id == R.id.send_email) {
            val s = arrayOf("fariz.siracli@gmail.com")
            composeEmail(s, getString(R.string.app_name) + " : Feedback")
        }

        return super.onOptionsItemSelected(item)
    }

    fun composeEmail(addresses: Array<String>, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }


    @SuppressLint("InflateParams")
    fun changeLanguageDialog() {
        val inflater = layoutInflater
        val alertLayout = inflater.inflate(R.layout.language_view, null)
        val alert: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert)
        } else {
            AlertDialog.Builder(context)
        }
        alert.setTitle(R.string.language)

        alert.setView(alertLayout)
        val rbAz = alertLayout.rb_az
        rbAz.setText(R.string.az)
        val rbEn = alertLayout.rb_en
        rbEn.setText(R.string.eng)
        val rbRu = alertLayout.rb_ru
        rbRu.setText(R.string.rus)
        val defLang = Constants.language
        when (defLang) {
            "az" -> rbAz.isChecked = true
            "ru" -> rbRu.isChecked = true
            else -> rbEn.isChecked = true
        }
        alert.setCancelable(false)
        alert.setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
        alert.setPositiveButton(R.string.ok) { _, _ ->
            val selectedLang = when {
                rbAz.isChecked -> "az"
                rbRu.isChecked -> "ru"
                else -> "en"
            }
            if (selectedLang != Constants.language) {
                Utils.setPreference(this, Constants.SAVED_USER_LANGUAGE, selectedLang)
                Utils.log("start intent 11")
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(intent)
            }
        }
        val dialog = alert.create()
        dialog.show()
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            val placeholderFragment = PlaceholderFragment.newInstance(position + 1)
            placeholderFragment.mainActivity = this@MainActivity
            return placeholderFragment
        }

        override fun getCount(): Int {
            return 5
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    @SuppressLint("ValidFragment")
    class PlaceholderFragment : Fragment() {

        var mainActivity: MainActivity? = null

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            val rootView = inflater.inflate(R.layout.fragment_main, container, false)
            val recView: RecyclerView = rootView.findViewById<RecyclerView>(R.id.rv_items)

            recView.layoutManager = LinearLayoutManager(context!!)
            val companiesAdapter = CompaniesAdapter(listOf(), mainActivity!!)
            recView.adapter = companiesAdapter
            val page = arguments!!.getInt(ARG_SECTION_NUMBER)

            AsyncTask.execute {

                val companies = database!!.companyDao().getCompanyByType(page)
                if (companies != null) {
                    Collections.shuffle(companies)
                    companiesAdapter.items = companies
                    getActivity()!!.runOnUiThread { companiesAdapter.notifyDataSetChanged() }
                }

            }

            return rootView
        }

        companion object {
            var pageNumber = 0
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                pageNumber = sectionNumber
                return fragment
            }
        }
    }

    override fun onDestroy() {
        if (database != null)
            database!!.close()
        super.onDestroy()
    }
}
