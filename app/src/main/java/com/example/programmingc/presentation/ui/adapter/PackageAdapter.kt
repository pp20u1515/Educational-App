package com.example.programmingc.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.programmingc.R
import com.example.programmingc.domain.model.Package

class PackageAdapter(
    private var myPackage: List<Package>
) : RecyclerView.Adapter<PackageAdapter.PackageViewHolder>() {

    inner class PackageViewHolder(packageView: View): RecyclerView.ViewHolder(packageView){
        val typeOfPackage: TextView = packageView.findViewById(R.id.packageName)
        val diamondIcon: ImageView = packageView.findViewById(R.id.diamondIcon)
        val amountOfDiamonds: TextView = packageView.findViewById(R.id.amountOfDiamonds)
        val priceOfPackage: TextView = packageView.findViewById(R.id.priceOfPackage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_package, parent, false)
        return PackageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        val packages = myPackage[position]
        holder.typeOfPackage.text = packages.typeOfPackage
        holder.amountOfDiamonds.text = packages.amountOfDiamonds
        holder.priceOfPackage.text = packages.priceOfPackage
        holder.diamondIcon.setImageResource(packages.diamondIcon)
    }

    //override fun getItemCount(): Int = myPackage.size
    override fun getItemCount(): Int {
        return myPackage.size
    }

    fun updatePackages(_package: List<Package>){
        myPackage = _package
        notifyDataSetChanged()
    }
}