package com.pava.chatapplication.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
//import kotlinx.parcelize.Parcelize

@Parcelize
class User(val uid: String, val username: String, val profileImageUrl: String): Parcelable {
    constructor() : this("", "", "")
}