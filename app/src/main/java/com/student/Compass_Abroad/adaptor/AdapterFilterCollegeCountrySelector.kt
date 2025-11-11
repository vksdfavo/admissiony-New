import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemCustomPopupCountrySelectorBinding
import com.student.Compass_Abroad.modal.countryModel.DataX

class AdapterFilterCollegeCountrySelector(
    private val requireActivity: FragmentActivity,
    private val arraylist: ArrayList<DataX>,
    private val popupBinding: View,
    private val listener: OnCountrySelectionListener
) : RecyclerView.Adapter<AdapterFilterCollegeCountrySelector.MyViewHolder>() {

    private var filteredList: MutableList<DataX> = arraylist.toMutableList() // Maintain filtered list
    private var selectedItem: DataX? = null // Store only one selected item

    var onItemClickListener: ((DataX?) -> Unit)? = null


    interface OnCountrySelectionListener {

        fun onCountryChecked(country: DataX)

        fun onCountryUnchecked(country: DataX)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCustomPopupCountrySelectorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = filteredList[position]
        holder.bind(item)
    }

    fun getFilter(): Filter {
        return exampleFilter
    }

    fun getSelectedItem(): DataX? {
        return selectedItem
    }

    fun setInitialSelection(selectedItem: DataX?) {
        this.selectedItem = selectedItem
        notifyDataSetChanged()
    }

    fun clearSelection() {
        selectedItem = null
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    @Suppress("DEPRECATION")
    inner class MyViewHolder(private val binding: ItemCustomPopupCountrySelectorBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cbProgramSelect.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = filteredList[position]
                    if (isChecked) {
                        if (selectedItem != item) {
                            selectedItem = item
                            notifyDataSetChanged()
                            listener.onCountryChecked(item)
                            onItemClickListener?.invoke(selectedItem)
                        }
                    } else {
                        if (selectedItem == item) {
                            selectedItem = null
                            listener.onCountryUnchecked(item)
                            notifyDataSetChanged()
                            onItemClickListener?.invoke(selectedItem)
                        }
                    }
                }
            }

            binding.tvItemCountrySelector.setOnClickListener {
                toggleSelection()
            }

            binding.llCountrySelector.setOnClickListener {
                toggleSelection()
            }
        }

        private fun toggleSelection() {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = filteredList[position]
                if (binding.cbProgramSelect.isChecked) {
                    binding.cbProgramSelect.isChecked = false
                    if (selectedItem == item) {
                        selectedItem = null
                        notifyDataSetChanged()
                        onItemClickListener?.invoke(selectedItem)
                    }
                } else {
                    binding.cbProgramSelect.isChecked = true
                    if (selectedItem != item) {
                        selectedItem = item
                        notifyDataSetChanged()
                        onItemClickListener?.invoke(selectedItem)
                    }
                }
            }
        }

        fun bind(item: DataX) {
            binding.tvItemCountrySelector.text = item.label
            binding.cbProgramSelect.isChecked = (selectedItem == item)
        }
    }

    private val exampleFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val filterList = if (constraint.isNullOrEmpty()) {
                arraylist
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                arraylist.filter { it.label.toLowerCase().contains(filterPattern) }
            }
            results.values = filterList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredList = results?.values as MutableList<DataX>
            notifyDataSetChanged()

            if (filteredList.isEmpty()) {
                popupBinding.findViewById<TextView>(R.id.tvSelect_noData).visibility = View.VISIBLE
                popupBinding.findViewById<TextView>(R.id.tvSelect_noData).text = "No Country Found"
            } else {
                popupBinding.findViewById<TextView>(R.id.tvSelect_noData).visibility = View.GONE
            }
        }
    }
}
