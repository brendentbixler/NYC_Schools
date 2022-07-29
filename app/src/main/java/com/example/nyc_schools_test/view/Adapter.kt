package com.example.nyc_schools_test.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nyc_schools_test.common.OnSchoolClicked
import com.example.nyc_schools_test.databinding.SchoolItemLayoutBinding
import com.example.nyc_schools_test.model.remote.SchoolListResponse

/**
 * Adapter Class
 * Holds and stores the data to then be sent to the UI
 * @param onSchoolClicked
 * @param MutableList<SchoolListResponse>
 */
class Adapter(
    private val onSchoolClicked: OnSchoolClicked,
    private val items: MutableList<SchoolListResponse> = mutableListOf()
) : RecyclerView.Adapter<Adapter.NYCViewHolder>() {

    class NYCViewHolder(val binding: SchoolItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    /**
     * Sets data for the newly created list
     */
    fun updateData(newSchools: List<SchoolListResponse>) {
        items.addAll(newSchools)
        notifyDataSetChanged()
    }

    /**
     * Inflate
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NYCViewHolder {
        return NYCViewHolder(
            SchoolItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Bind data to correct items
     */
    override fun onBindViewHolder(holder: NYCViewHolder, position: Int) {
        holder.binding.tvSchoolName.text = items[position].school_name
        holder.binding.tvSchoolLocation.text = items[position].neighborhood
        holder.binding.tvSchoolPhone.text = items[position].phone_number

        // Bind click listener
        holder.binding.cardView.setOnClickListener {
            onSchoolClicked.schoolClicked(items[position])
        }
    }

    /** Total item count */
    override fun getItemCount(): Int = items.size
}