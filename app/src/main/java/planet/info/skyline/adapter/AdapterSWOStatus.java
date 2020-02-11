package planet.info.skyline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.List;

import planet.info.skyline.R;

public class AdapterSWOStatus extends RecyclerView.Adapter<AdapterSWOStatus.LaborCodesVH> {
    Context context;
    List<SWOStatus> list;
    private int selectedPos;

    public AdapterSWOStatus(Context context, List<SWOStatus> list, int selectedPos) {
        this.context = context;
        this.list = list;
        this.selectedPos = selectedPos;
    }


    @Override
    public LaborCodesVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_swo_status, parent, false);
        return new LaborCodesVH(view);
    }

    @Override
    public void onBindViewHolder(LaborCodesVH holder, int position) {
        holder.bind(position);
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public String getUpdatedSWOStatus() {
        if (selectedPos != -1) {
            return  list.get(selectedPos).getSWOStatusId();
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LaborCodesVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckedTextView mCheckedTextView;

        public LaborCodesVH(View itemView) {
            super(itemView);
            mCheckedTextView = (CheckedTextView) itemView.findViewById(R.id.checked_text_view);
            itemView.setOnClickListener(this);
        }

        void bind(final int position) {
            if (selectedPos != -1) {
                if (selectedPos == position) {
                    mCheckedTextView.setChecked(true);
                } else {
                    mCheckedTextView.setChecked(false);
                }
            }
            mCheckedTextView.setText(list.get(position).getSWOStatus());


        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (!mCheckedTextView.isChecked()) {
                mCheckedTextView.setChecked(true);
                selectedPos = adapterPosition;
                notifyDataSetChanged();
            }
        }
    }

}
