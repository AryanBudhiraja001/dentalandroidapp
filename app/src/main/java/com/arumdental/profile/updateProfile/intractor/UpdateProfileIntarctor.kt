package com.arumdental.profile.updateProfile.intractor

import com.arumdental.authentication.signUp.model.response.SignUpResponse
import com.arumdental.base.BaseApplication
import com.arumdental.home.address.model.AddMultipleAddressModel
import com.arumdental.profile.updateProfile.UpdateProfileContract
import com.arumdental.profile.updateProfile.model.UpdateProfileModel
import com.arumdental.utils.UserSharedPrefences
import com.arumdental.utils.showSnackBar
import com.arumdental.web.RetrofitClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class UpdateProfileIntarctor:UpdateProfileContract.UpdateProfileIntractor {


    override fun updateProfile(
        updateProfileModel: AddMultipleAddressModel,
        updatePrpfileCallBack: UpdateProfileContract.UpdatePrpfileCallBack
    ) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().updateUserProfile(UserSharedPrefences.getInstance().id,
            updateProfileModel).enqueue(object : Callback<ResponseBody>
            {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    updatePrpfileCallBack.onError(t.message!!)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful && response.code()==200)
                    {
                        updatePrpfileCallBack.onSucess(response.body()!!)
                    }
                    else{
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            var data=jObjError.getJSONObject("data")
                            var code=data.getInt("status")
                            if(code==400)
                            {
                                updatePrpfileCallBack.onError("User not found")
                            }
                            else
                            {
                                updatePrpfileCallBack.onError("User not found")
                            }



                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

            })
        }
        else
        {
          updatePrpfileCallBack.onError("No Internet Connection")
        }
    }
}