package planet.info.skyline.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import planet.info.skyline.R
import planet.info.skyline.tech.billable_timesheet.LaborCodesActivity

class AdapterLaborCodes(val context: Context, private val laborCodes: ArrayList<LaborCodes>) : RecyclerView.Adapter<AdapterLaborCodes.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_vendor, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return laborCodes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, laborCodes[position])
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, labor_code: LaborCodes) {
            val textViewLaborCodes = itemView.findViewById(R.id.txtvw_vendor) as TextView
            val linearLayout_Parent = itemView.findViewById(R.id.ll_parent) as LinearLayout

            textViewLaborCodes.text = labor_code.laborName
            linearLayout_Parent.setOnClickListener {
                val returnIntent = Intent()
                returnIntent.putExtra("Labor_id", labor_code.lalorIDPK)
                Toast.makeText(context, labor_code.laborName, Toast.LENGTH_SHORT).show()
                if (context is LaborCodesActivity) {
                    (context as Activity).setResult(Activity.RESULT_OK, returnIntent)
                    (context as Activity).finish()
                }
            }
        }


    }
}