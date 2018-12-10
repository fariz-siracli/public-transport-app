package com.fs.mobile.tansportcatalog

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.crashlytics.android.Crashlytics
import com.fs.mobile.tansportcatalog.utils.Constants
import com.fs.mobile.tansportcatalog.utils.Utils
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
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
    private var context: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Fabric.with(this, Crashlytics())
        Fabric.with(
            Fabric.Builder(this)
                .kits(Crashlytics())
                .appIdentifier(BuildConfig.APPLICATION_ID)
                .build()
        )
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
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
                Utils.log("current = " + position)

            }

            override fun onPageSelected(position: Int) {

            }

        })


        Utils.copyDataBaseToApp(this, Constants.DB_NAME)
        database = AppDatabase.getAppDataBase(this)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
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
            var recView: RecyclerView = rootView.findViewById<RecyclerView>(R.id.rv_items)
            recView.layoutManager = LinearLayoutManager(context!!)
            var companiesAdapter = CompaniesAdapter(listOf(), mainActivity!!)
            recView.adapter = companiesAdapter
            var page = arguments!!.getInt(ARG_SECTION_NUMBER)

            AsyncTask.execute {
                Utils.log("page = " + page)
                var companies = database!!.companyDao().getCompanyByType(page)
                Collections.shuffle(companies)
                companiesAdapter.items = companies
                getActivity()!!.runOnUiThread { companiesAdapter.notifyDataSetChanged() }

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
}
