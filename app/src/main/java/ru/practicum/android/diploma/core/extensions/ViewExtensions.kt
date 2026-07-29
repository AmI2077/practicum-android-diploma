package ru.practicum.android.diploma.core.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R

fun ImageView.loadCompanyLogo(logoUrl: String?, cornerRadius: Int) {
    if (!logoUrl.isNullOrEmpty()) {
        Glide.with(context)
            .load(logoUrl)
            .placeholder(R.drawable.ic_placeholder_32)
            .error(R.drawable.ic_placeholder_32)
            .transform(
                CenterCrop(),
                RoundedCorners(cornerRadius)
            )
            .into(this)
    } else {
        this.setImageResource(R.drawable.ic_placeholder_32)
    }
}
