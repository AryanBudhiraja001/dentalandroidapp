package com.arumdental.web

import com.arumdental.authentication.login.model.LoginResponseModel
import com.arumdental.authentication.login.model.ValidateTokenResponse
import com.arumdental.authentication.signUp.model.paramModel.SignUpParamModel
import com.arumdental.authentication.signUp.model.response.SignUpResponse
import com.arumdental.coupons.model.CouponsResponseModel
import com.arumdental.home.address.model.AddMultipleAddressModel
import com.arumdental.home.brands.model.CategoriesResponse
import com.arumdental.home.cart.model.AddToCartResponse
import com.arumdental.home.cart.model.UpdateCartResponse
import com.arumdental.home.changePassword.ChangePasswordResponse
import com.arumdental.home.filter.model.FilterResponseModel
import com.arumdental.home.order.createOrder.model.CreateOrderModel
import com.arumdental.home.order.createOrder.model.CreateOrderResponseModel
import com.arumdental.home.order.model.OrdersModel
import com.arumdental.home.products.model.ProductResponseModel
import com.arumdental.home.shipping.model.ShippingResponseModel
import com.arumdental.home.wishlist.addProduct.model.AddProductResponse
import com.arumdental.home.wishlist.getProducts.model.WishlistProductsResponseModel
import com.arumdental.profile.model.ContactParamModel
import com.arumdental.profile.model.ContactResponseModel
import com.arumdental.profile.model.ProfileResponseModel
import com.arumdental.profile.updateProfile.model.UpdateProfileModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {



    @POST("wp-json/wc/v3/customers")
    fun signUp(@Body signUpParamModel: SignUpParamModel): Call<SignUpResponse>

    @FormUrlEncoded
    @POST("wp-json/jwt-auth/v1/token")
    fun login(@Field("username")username:String,
              @Field("password") password:String):Call<LoginResponseModel>

    @POST("wp-json/jwt-auth/v1/token/validate")
    fun validateToken():Call<ValidateTokenResponse>

  @GET("wp-json/wc/v3/customers/{id}")
   fun getUserInfo(@Path("id") id:Int): Call<ResponseBody>


    @FormUrlEncoded
    @POST("api/userDetails.php")
    fun getUserInfo(@Field("email") email:String): Call<ProfileResponseModel>


   /* @GET("/wp-json/wc/v3/products/attributes/1/terms")
    fun getBrands(@Query("per_page") per_page:Int
   ):Call<ArrayList<CategoriesResponse>>*/
   @GET
   fun getBrands(@Url string:String):Call<ArrayList<CategoriesResponse>>

    @GET("/wp-json/wc/v3/products/attributes/12/terms")
    fun searchBrands(@Query("search") search:String):Call<ArrayList<CategoriesResponse>>

    @GET("/wp-json/wc/v3/products/categories?exclude=116,101,152,154,102,127,113")
    fun getCategories(@Query("per_page") per_page:Int):Call<ArrayList<CategoriesResponse>>


    @GET("/wp-json/wc/v3/products?per_page=100&attribute=pa_brand")
    fun getProductsFromCategory(@Query("attribute_term")attribute_term:Int,
    @Query("search") search:String):Call<ArrayList<ProductResponseModel>>

    @GET("/wp-json/wc/v3/products/attributes/10/terms")
    fun gettags():Call<ArrayList<CategoriesResponse>>

    @GET("/wp-json/wc/v3/products/attributes/168/terms")
    fun getTypes():Call<ArrayList<CategoriesResponse>>

    @GET("/wp-json/wc/v3/products/attributes/11/terms")
    fun getVolume():Call<ArrayList<CategoriesResponse>>

    @GET
    fun applyFilter(@Url string:String):Call<ArrayList<ProductResponseModel>>

    @FormUrlEncoded
    @POST("/wp-json/cocart/v1/add-item")
    fun addToCart(@Field("product_id")product_id:Int,
    @Field("quantity")quantity:Int):Call<AddToCartResponse>

    @FormUrlEncoded
    @POST("/wp-json/cocart/v1/item")
    fun updateCart(@Field("cart_item_key")cart_item_key:String,
                   @Field("quantity")quantity:Int):Call<UpdateCartResponse>

    @GET("wp-json/cocart/v1/get-cart")
    fun getCart():Call<ResponseBody>

    @PUT("wp-json/wc/v3/customers/{id}")
    fun updateUserProfile(@Path("id") id:Int,
                          @Body updateProfileModel: AddMultipleAddressModel
    ):Call<ResponseBody>

    @POST("wp-json/wc/v3/orders")
    fun createOrder(@Body createOrderModel: CreateOrderModel):Call<CreateOrderResponseModel>

    @POST("wp-json/cocart/v1/clear")
    fun clearCart():Call<ResponseBody>

    @GET("wp-json/wc/v3/orders")
    fun getOrders(@Query("customer")customer:Int):Call<ArrayList<OrdersModel>>

    @GET("wp-json/wc/v3/coupons")
    fun getcoupons():Call<ArrayList<CouponsResponseModel>>

    @FormUrlEncoded
    @POST("api/createPayemnt.php")
    fun makePayment(
        @Field("token")token:String,
    @Field("amount")amount:String,
    @Field("orderId")orderId:String):Call<ResponseBody>

    @GET("api/shippingRates.php")
    fun getShipingRated():Call<ShippingResponseModel>

  @FormUrlEncoded
  @POST("api/addWishlist.php")
  fun addToWishlist(@Field("userID")userID:Int,
               @Field("productID") productID:Int,
                @Field("price")price:String):Call<AddProductResponse>

    @FormUrlEncoded
    @POST("api/wishlist.php")
    fun getProducts(@Field("userID")userID:Int):Call<WishlistProductsResponseModel>

    @FormUrlEncoded
    @POST("api/removeWishlist.php")
    fun removeProduct(@Field("productID")productID:Int,
    @Field("userID")userID:Int):Call<AddProductResponse>

    @GET("wp-json/wc/v3/products")
    fun getProductDetail(@Query("include")include:String):Call<ArrayList<ProductResponseModel>>

    @FormUrlEncoded
    @POST("api/forgotPassword.php")
    fun forgotPassword(@Field("login") login:String):Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/resetPassword.php")
    fun resetPassword(@Field("user_id")user_id:Int,
    @Field("password") password:String,
    @Field("new_password") new_password:String):Call<ChangePasswordResponse>

    @FormUrlEncoded
    @POST("wp-json/contact-form-7/v1/contact-forms/106516/feedback")
    fun contact(@Field("country") country:String,
                @Field("company") company:String ,
                @Field("options") options:String,
                @Field("your-name") your_name:String ,
                @Field("email") email:String ,
                @Field("subject") subject:String ,
                @Field("telephone") telephone:String ,
                @Field("message") message:String
    ):Call<ContactResponseModel>








}