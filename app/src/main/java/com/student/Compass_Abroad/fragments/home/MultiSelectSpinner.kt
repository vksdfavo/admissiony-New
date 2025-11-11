import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import java.util.*

class MultiSelectSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatSpinner(context, attrs), DialogInterface.OnMultiChoiceClickListener {

    interface OnMultipleItemsSelectedListener {
        fun selectedIndices(indices: List<Int>)
        fun selectedStrings(strings: List<String>)
    }

    private var listener: OnMultipleItemsSelectedListener? = null
    private var _items: Array<String>? = null
    private var mSelection: BooleanArray? = null
    private var mSelectionAtStart: BooleanArray? = null
    private var _itemsAtStart: String? = null
    private var simpleAdapter: ArrayAdapter<String>
    private var hasNone = false

    init {
        simpleAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item)
        super.setAdapter(simpleAdapter)
    }

    override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
        if (mSelection != null && which < mSelection!!.size) {
            if (hasNone) {
                if (which == 0 && isChecked && mSelection!!.size > 1) {
                    for (i in 1 until mSelection!!.size) {
                        mSelection!![i] = false
                        (dialog as AlertDialog).listView.setItemChecked(i, false)
                    }
                } else if (which > 0 && mSelection!![0] && isChecked) {
                    mSelection!![0] = false
                    (dialog as AlertDialog).listView.setItemChecked(0, false)
                }
            }
            mSelection!![which] = isChecked
            simpleAdapter.clear()
            simpleAdapter.add(buildSelectedItemString())
        } else {
            throw IllegalArgumentException("Argument 'which' is out of bounds.")
        }
    }

    override fun performClick(): Boolean {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Please select!")
        builder.setMultiChoiceItems(_items, mSelection, this)
        _itemsAtStart = selectedItemsAsString
        builder.setPositiveButton("Submit") { dialog, which ->
            mSelectionAtStart = mSelection!!.clone()
            listener?.selectedIndices(selectedIndices)
            listener?.selectedStrings(selectedStrings)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            simpleAdapter.clear()
            simpleAdapter.add(_itemsAtStart)
            mSelection = mSelectionAtStart!!.clone()
        }
        builder.show()
        return true
    }

    override fun setAdapter(adapter: SpinnerAdapter) {
        throw RuntimeException("setAdapter is not supported by MultiSelectSpinner.")
    }

    fun setListener(listener: OnMultipleItemsSelectedListener) {
        this.listener = listener
    }

    fun setItems(items: List<String>) {
        _items = items.toTypedArray()
        mSelection = BooleanArray(_items!!.size)
        mSelectionAtStart = BooleanArray(_items!!.size)
        simpleAdapter.clear()
        simpleAdapter.add(_items!![0])
        mSelection!!.fill(false)
        mSelection!![0] = true
    }

    fun setSelection(selection: Array<String>) {
        for (i in mSelection!!.indices) {
            mSelection!![i] = false
            mSelectionAtStart!![i] = false
        }
        for (cell in selection) {
            for (j in _items!!.indices) {
                if (_items!![j] == cell) {
                    mSelection!![j] = true
                    mSelectionAtStart!![j] = true
                }
            }
        }
        simpleAdapter.clear()
        simpleAdapter.add(buildSelectedItemString())
    }

    fun setSelection(selection: List<String>) {
        for (i in mSelection!!.indices) {
            mSelection!![i] = false
            mSelectionAtStart!![i] = false
        }
        for (sel in selection) {
            for (j in _items!!.indices) {
                if (_items!![j] == sel) {
                    mSelection!![j] = true
                    mSelectionAtStart!![j] = true
                }
            }
        }
        simpleAdapter.clear()
        simpleAdapter.add(buildSelectedItemString())
    }

    override fun setSelection(index: Int) {
        for (i in mSelection!!.indices) {
            mSelection!![i] = false
            mSelectionAtStart!![i] = false
        }
        if (index in mSelection!!.indices) {
            mSelection!![index] = true
            mSelectionAtStart!![index] = true
        } else {
            throw IllegalArgumentException("Index $index is out of bounds.")
        }
        simpleAdapter.clear()
        simpleAdapter.add(buildSelectedItemString())
    }

    fun setSelection(selectedIndices: IntArray) {
        for (i in mSelection!!.indices) {
            mSelection!![i] = false
            mSelectionAtStart!![i] = false
        }
        for (index in selectedIndices) {
            if (index in mSelection!!.indices) {
                mSelection!![index] = true
                mSelectionAtStart!![index] = true
            } else {
                throw IllegalArgumentException("Index $index is out of bounds.")
            }
        }
        simpleAdapter.clear()
        simpleAdapter.add(buildSelectedItemString())
    }

    val selectedStrings: List<String>
        get() {
            val selection: MutableList<String> = LinkedList()
            for (i in _items!!.indices) {
                if (mSelection!![i]) {
                    selection.add(_items!![i])
                }
            }
            return selection
        }

    val selectedIndices: List<Int>
        get() {
            val selection: MutableList<Int> = LinkedList()
            for (i in _items!!.indices) {
                if (mSelection!![i]) {
                    selection.add(i)
                }
            }
            return selection
        }

    private fun buildSelectedItemString(): String {
        val sb = StringBuilder()
        var foundOne = false

        for (i in _items!!.indices) {
            if (mSelection!![i]) {
                if (foundOne) {
                    sb.append(", ")
                }
                foundOne = true
                sb.append(_items!![i])
            }
        }
        return sb.toString()
    }

    private val selectedItemsAsString: String
        get() {
            val sb = StringBuilder()
            var foundOne = false

            for (i in _items!!.indices) {
                if (mSelection!![i]) {
                    if (foundOne) {
                        sb.append(", ")
                    }
                    foundOne = true
                    sb.append(_items!![i])
                }
            }
            return sb.toString()
        }

    fun hasNoneOption(value: Boolean) {
        hasNone = value
    }

}
