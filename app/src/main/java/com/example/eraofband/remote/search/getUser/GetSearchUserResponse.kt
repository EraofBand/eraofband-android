package com.example.eraofband.remote.search.getUser

import android.os.Parcel
import android.os.Parcelable


data class GetSearchUserResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: List<GetSearchUserResult>
)

data class GetSearchUserResult(
    var nickName : String,
    var profileImgUrl : String,
    var userIdx : Int,
    var userSession : Int
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nickName)
        parcel.writeString(profileImgUrl)
        parcel.writeInt(userIdx)
        parcel.writeInt(userSession)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetSearchUserResult> {
        override fun createFromParcel(parcel: Parcel): GetSearchUserResult {
            return GetSearchUserResult(parcel)
        }

        override fun newArray(size: Int): Array<GetSearchUserResult?> {
            return arrayOfNulls(size)
        }
    }
}