package planet.info.skyline.tech.billable_timesheet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import planet.info.skyline.R
import planet.info.skyline.RequestControler.MyAsyncTask
import planet.info.skyline.RequestControler.ResponseInterface
import planet.info.skyline.adapter.*
import planet.info.skyline.crash_report.ConnectionDetector
import planet.info.skyline.network.Api
import planet.info.skyline.shared_preference.Shared_Preference
import planet.info.skyline.util.Utility


class LaborCodesActivity : AppCompatActivity(), ResponseInterface {

    lateinit var recyclerView: RecyclerView
    lateinit var context: Context
    lateinit var adapter : AdapterLaborCodesNew
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_labor_codes)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = Utility.getTitle("Choose Labor Code")



        context = this
        recyclerView = findViewById<RecyclerView>(R.id.rv_labor_codes_list)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, resources.getInteger(R.integer.list_divider_margin_left)))

        fetchLaborCodes()
    }

    private fun fetchLaborCodes() {
        val jsonObject = JSONObject()
        try {
            val dealerId = Shared_Preference.getDEALER_ID(this)
            val role = Shared_Preference.getUSER_ROLE(this)
            jsonObject.put("dealerID", dealerId)
            jsonObject.put("cat", role)
            jsonObject.put("type", "1")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (ConnectionDetector(context).isConnectingToInternet) {
            MyAsyncTask(this, true, this, Api.API_BILLABLE_NONBILLABLE_CODE, jsonObject).execute()
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show()
        }
    }

    override fun handleResponse(responseStr: String?, api: String?) {
        if (api.equals(Api.API_BILLABLE_NONBILLABLE_CODE)) {
            val listLaborCodes: ArrayList<LaborCodes> = ArrayList()
            val jsonArry= JSONArray(responseStr)
            supportActionBar!!.setSubtitle(Utility.getTitle("Total : "+jsonArry.length().toString()))
            for (i in 0..jsonArry.length()-1 step 1) {
                val jsonObject: JSONObject = jsonArry.getJSONObject(i)
                val laborCode = LaborCodes( jsonObject.getString("Labor_name"),jsonObject.getString("Lalor_ID_PK"))
                listLaborCodes.add(laborCode)
            }
            var selectedPos :Int =-1  // by default no labor code is selected
            adapter = AdapterLaborCodesNew(this,listLaborCodes,selectedPos)
            recyclerView.adapter = adapter
            if(selectedPos!=-1) recyclerView.scrollToPosition(selectedPos)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_sharefiles, menu)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.menu_done -> {
                var laborcode= adapter.selectedLaborCode
                if( laborcode == "" ){
                    Toast.makeText(context,"Please select labor code.",Toast.LENGTH_SHORT).show()
                }else {
                    val returnIntent = Intent()
                    returnIntent.putExtra("updatedLaborCode", laborcode)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
