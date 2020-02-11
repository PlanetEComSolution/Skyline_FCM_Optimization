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
import planet.info.skyline.adapter.AdapterSWOStatus
import planet.info.skyline.adapter.MyDividerItemDecoration
import planet.info.skyline.adapter.SWOStatus
import planet.info.skyline.crash_report.ConnectionDetector
import planet.info.skyline.network.Api
import planet.info.skyline.progress.ProgressHUD
import planet.info.skyline.shared_preference.Shared_Preference
import planet.info.skyline.util.Utility


class SWO_Status_Activity : AppCompatActivity(), ResponseInterface {

    lateinit var recyclerView: RecyclerView
    lateinit var context: Context
    val listSwoStatus: ArrayList<SWOStatus> = ArrayList()

    lateinit var adapter: AdapterSWOStatus
    lateinit var mProgressHUD: ProgressHUD

    lateinit var oldSwoStatus: String
    lateinit var updatedSwoStatus: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swo_status)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        context = this
        if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
            title = Utility.getTitle("Update AWO Status")
        } else {
            title = Utility.getTitle("Update SWO Status")
        }


        recyclerView = findViewById<RecyclerView>(R.id.rv_labor_codes_list)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, resources.getInteger(R.integer.list_divider_margin_left)))

        showprogressdialog()
        fetchSWOStatus()
    }

    private fun fetchSWOStatus() {
        val jsonObject = JSONObject()
        try {
            val dealerId = Shared_Preference.getDEALER_ID(this)
            jsonObject.put("dealerID", dealerId)
            var role = Shared_Preference.getUSER_ROLE(this)
            if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
                role = Utility.USER_ROLE_ARTIST
            }
            jsonObject.put("Role", role)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (ConnectionDetector(context).isConnectingToInternet) {
            MyAsyncTask(this, false, this, Api.API_bind_SWO_AWO_Status, jsonObject).execute()
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchSWOAWODetailsByID() {
        val jsonObject = JSONObject()
        try {
            val swoID = Shared_Preference.getSWO_ID(context)
            jsonObject.put("swoID_Awoid", swoID)
            if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
                jsonObject.put("Type", Utility.TYPE_AWO)
            } else {
                jsonObject.put("Type", Utility.TYPE_SWO)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (ConnectionDetector(context).isConnectingToInternet) {
            MyAsyncTask(this, false, this, Api.API_Get_Swo_AWO_DetailByID_Type, jsonObject).execute()
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show()
        }
    }

    override fun handleResponse(responseStr: String?, api: String?) {
        if (api.equals(Api.API_bind_SWO_AWO_Status)) {
            val jsonArry = JSONArray(responseStr)
            supportActionBar!!.setSubtitle(Utility.getTitle("Total : " + jsonArry.length().toString()))
            for (i in 0..jsonArry.length() - 1 step 1) {
                val jsonObject: JSONObject = jsonArry.getJSONObject(i)
                val swoStatus = SWOStatus(jsonObject.getString("txt_status"), jsonObject.getString("ID_PK"))
                listSwoStatus.add(swoStatus)
            }
            fetchSWOAWODetailsByID()

        } else if (api.equals(Api.API_Get_Swo_AWO_DetailByID_Type)) {
            hideprogressdialog()
            val jsonArry = JSONArray(responseStr)
            if (jsonArry.length() > 0) {
                val json_obj: JSONObject = jsonArry.getJSONObject(0)
                oldSwoStatus = json_obj.getString("SWO_Status_new")
            }

            var selectedPosition = -1  // by default no swo status is selected
            for (i in 0..listSwoStatus.size - 1) {
                if (oldSwoStatus == listSwoStatus.get(i).SWOStatusId) {
                    selectedPosition = i
                }
            }

            adapter = AdapterSWOStatus(this, listSwoStatus, selectedPosition)
            recyclerView.adapter = adapter
            if (selectedPosition != -1) recyclerView.scrollToPosition(selectedPosition)


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
                updatedSwoStatus = adapter.updatedSWOStatus
                if (updatedSwoStatus == "") {
                    Toast.makeText(context, "Please select swo status.", Toast.LENGTH_SHORT).show()
                } else {
                    val returnIntent = Intent()
                    returnIntent.putExtra("oldSwoStatus", oldSwoStatus)
                    returnIntent.putExtra("updatedSwoStatus", updatedSwoStatus)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showprogressdialog() {
        try {
            mProgressHUD = ProgressHUD.show(context, Utility.LOADING_TEXT, false)
        } catch (e: java.lang.Exception) {
            e.message
        }
    }

    fun hideprogressdialog() {
        try {
            if (mProgressHUD.isShowing()) {
                mProgressHUD.dismiss()
            }
        } catch (e: java.lang.Exception) {
            e.message
        }
    }

}
