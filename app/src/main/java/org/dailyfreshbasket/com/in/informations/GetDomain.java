package org.dailyfreshbasket.com.in.informations;

/**
 * Created by shubham on 2/22/2017.
 */

public class GetDomain {

    public interface KEYS{
       //public String KEY_DOAMIN="http://192.168.0.4/";
        public String KEY_DOAMIN="http://dailyfreshbasket.com/dailyfreshbasket";
        public String TAG="dailyfreshbasket.androidbaba.tk";
        public String KEY_FRONT_PAGE_URL=KEY_DOAMIN+"/android/home-product-loader.php";
        public String KEY_CART_MANAGER_URL=KEY_DOAMIN+"/android/user_product/cartmanager.php";
        public String KEY_WISHLIST_URL=KEY_DOAMIN+"/android/user_product/wishlistmanager.php";
        public String KEY_SEARCH_URL=KEY_DOAMIN+"/android/product_shop/searchmanager.php";
        public String KEY_SEARCHEXP_URL=KEY_DOAMIN+"/android/product_shop/searchexp.php";
        public String KEY_FRONT_PAGE_SHOPES_URL=KEY_DOAMIN+"/android/product_shop/shopmanager.php";


       public String URL_GET_PRODUCT=KEY_DOAMIN+"/products/getProducts.php";
        public String URL_RATE_PRODUCT=KEY_DOAMIN+"/products/addRating.php";
        public String URL_BANNER=KEY_DOAMIN+"/banner/getBannerList.php";
        public String URL_LOGIN=KEY_DOAMIN+"/user/profile/getUser.php";
        public String URL_COMPLAIN=KEY_DOAMIN+"/user/complains/complain.php";
        public String URL_SIGNUP=KEY_DOAMIN+"/user/profile/addUser.php";
        public String URL_UPDATE=KEY_DOAMIN+"/user/profile/updateUser.php";
     public String URL_ADDWISHLIST=KEY_DOAMIN+"/user/mywishlist/addToWishList.php";
        public String URL_WISHLIST=KEY_DOAMIN+"/user/mywishlist/getMyWishList.php";
        public String URL_MOVEWISHLIST=KEY_DOAMIN+"/user/mywishlist/moveMyWishList.php";
        public String URL_MYCART=KEY_DOAMIN+"/user/mycart/getMyCart.php";
        public String URL_GUESTCART=KEY_DOAMIN+"/user/guest/getMyCart.php";
        public String URL_DEFULT_PIC=KEY_DOAMIN+"/images/defult.png";
        public String URL_SEARCH=KEY_DOAMIN+"/search/searchProduct.php";
        public String URL_ADDRESS=KEY_DOAMIN+"/user/address/getAddress.php";
        public String URL_PLACEORDER=KEY_DOAMIN+"/order/addOrder.php";
     public String URL_UPDATEORDER=KEY_DOAMIN+"/order/updateOrder.php";
        public String URL_GETORDER=KEY_DOAMIN+"/order/getOrderList.php";
     public String URL_RESETPASSWORD=KEY_DOAMIN+"/user/profile/resetPassword.php";

        public String URL_LANDREQUEST=KEY_DOAMIN+"/extra/AddRequest.php";
        public String URL_varifyOtp=KEY_DOAMIN+"/user/profile/varifyOtp.php";

    }
}