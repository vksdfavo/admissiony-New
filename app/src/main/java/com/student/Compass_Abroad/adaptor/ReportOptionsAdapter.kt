import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R

class ReportOptionsAdapter(private val reasonsList: List<com.student.Compass_Abroad.modal.ReportReasons.Record>) :
    RecyclerView.Adapter<ReportOptionsAdapter.ViewHolder>() {

    private var selectedItem = -1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reasonText: TextView = itemView.findViewById(R.id.tvReason)
        val radioButton: RadioButton = itemView.findViewById(R.id.radioButton)

        init {
            // Set click listener to handle radio button selection
            radioButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Uncheck previously selected radio button
                    notifyItemChanged(selectedItem)
                    // Update selected item and check the current radio button
                    selectedItem = position
                    notifyItemChanged(selectedItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dialog, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = reasonsList[position]
        holder.reasonText.text = currentItem.name
        holder.radioButton.isChecked = position == selectedItem
    }

    override fun getItemCount(): Int = reasonsList.size

    // Function to get the selected reason
    fun getSelectedReasonIdentifier(): String? {
        return if (selectedItem != -1) {
            reasonsList[selectedItem].identifier
        } else {
            null
        }
    }
}
