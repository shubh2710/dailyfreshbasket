package org.dailyfreshbasket.com.in.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.adapters.RecycleViewAdapterRowDetail;
import org.dailyfreshbasket.com.in.models.MyOrderDetailProduct;
import org.dailyfreshbasket.com.in.myInterface.getResponce;
import org.dailyfreshbasket.com.in.networks.SendRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_DEFULT_PIC;
import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_GETORDER;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;

public class OrderDetailActivity extends AppCompatActivity implements getResponce {


    private RecycleViewAdapterRowDetail adapter;
    private TextView tv_o_no,tv_o_id,tv_o_date,tv_o_amt,tv_o_stats,tv_o_placeDate;
    private TextView tv_o_name,tv_o_land,tv_o_street,tv_o_city,tv_o_mobile,tv_o_state;
    private ProgressDialog pd=null;
    private Toolbar toolbar;
    private ArrayList<MyOrderDetailProduct> products=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("Order Detail");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setup();
        try{
            String oid=getIntent().getStringExtra("oid");
            pd = ProgressDialog.show(this, "Getting details", "wait...");
            pd.setCancelable(true);
            getOrderDetails(oid);

        }catch (Exception e){

        }
        adapter=new RecycleViewAdapterRowDetail(this,products);
        RecyclerView rv=(RecyclerView)findViewById(R.id.rc_orderList_details);
        rv.setFocusable(false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }
    private void setup() {
        tv_o_no =(TextView)findViewById(R.id.tv_details_p_orderNo);
        tv_o_id=(TextView)findViewById(R.id.tv_details_p_orderID);
        tv_o_date=(TextView)findViewById(R.id.tv_details_p_dDate);
        tv_o_amt=(TextView)findViewById(R.id.tv_detail_p_amount);
        tv_o_stats=(TextView)findViewById(R.id.tv_deatils_p_status);
        tv_o_placeDate=(TextView)findViewById(R.id.tv_details_p_placedate);

        tv_o_name=(TextView)findViewById(R.id.tv_details_p_name);
        tv_o_land=(TextView)findViewById(R.id.tv_details_p_land);
        tv_o_street=(TextView)findViewById(R.id.tv_details_p_street);
        tv_o_city=(TextView)findViewById(R.id.tv_details_p_city);
        tv_o_mobile=(TextView)findViewById(R.id.tv_details_p_mobile);
        tv_o_state=(TextView)findViewById(R.id.tv_details_p_state);
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    private void getOrderDetails(String oid) {
        SendRequest req=new SendRequest(URL_GETORDER,this,1);
        String uid=readPrefes(this,PREF_KEY_UID,"");
        Map<String,String> params = new HashMap<String, String>();
        params.put("oid",oid);
        params.put("key","ORDER");
        params.put("uid",uid);
        params.put("password","");
        req.setParameters(params);
        req.send();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }
    @Override
    public void responce(String res, int requestCode) {
        Log.d("order list", "responce: "+res);
        pd.dismiss();
        JSONParsOrderDetails(res);
    }
    public void JSONParsOrderDetails(String res){
        try {
            JSONObject responce=new JSONObject(res);
            if(responce.getBoolean("success")){
                if(!responce.isNull("data")){
                    JSONObject object=responce.getJSONObject("data");
                    JSONArray order=object.getJSONArray("order");
                    JSONObject bill=object.getJSONObject("bill");
                    JSONArray product=object.getJSONArray("product");
                    JSONObject address=null;
                    if(!object.isNull("address"))
                    address=object.getJSONObject("address");
                    parseAddress(address);
                    parseBill(bill);
                    parseOrder(order);
                    parseProduct(product);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void parseOrder(JSONArray order){
        try {
            JSONObject orderList=order.getJSONObject(0);
            String oid=orderList.getString("oid");
            int amount =orderList.getInt("amount");
            String aid=orderList.getString("aid");
            int amount_status=orderList.getInt("amount_status");
            String bid=orderList.getString("bid");
            String createDate=orderList.getString("create_date");
            String delivary=orderList.getString("delivery_date");
            int delivaryShift=orderList.getInt("delivery_shift");
            String order_no=orderList.getString("order_number");
            int status=orderList.getInt("status");
            tv_o_no.setText("Order No:"+order_no);
            tv_o_id.setText("Order ID:"+oid);
            tv_o_date.setText(createDate);
            tv_o_amt.setText(amount+"");
            if(status==0){
                tv_o_stats.setTextColor(Color.RED);
            }else{
                tv_o_stats.setTextColor(Color.GREEN);
            }
            tv_o_stats.setText(getStatus(status));
            tv_o_placeDate.setText("Deliver On:"+delivary);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void parseBill(JSONObject bill){
        try {
            String bid=bill.getString("bid");
            String create=bill.getString("createDate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void parseProduct(JSONArray product){
        try {
            for(int i=0;i<product.length();i++){
                JSONObject products=product.getJSONObject(i);
                String pid=products.getString("pid");
                String opl=products.getString("opl");
                int quanity=products.getInt("quantity");
                int amount=0;
                if(!products.isNull("amount"))
                 amount=products.getInt("amount");
                int discount=0;
                if(!products.isNull("dicount"))
                discount=products.getInt("dicount");
                JSONArray productDetail=products.getJSONArray("product");
                JSONObject pro=productDetail.getJSONObject(0);
                String name=pro.getString("name");
                String price=pro.getString("actualPrice");
                String url=URL_DEFULT_PIC;
                if(!pro.isNull("url"))
                url=pro.getJSONArray("url").getString(0);
                MyOrderDetailProduct pModel=new MyOrderDetailProduct();
                pModel.setAmount(amount);
                pModel.setDiscount(discount);
                pModel.setName(name);
                pModel.setOpl(opl);
                pModel.setPid(pid);
                pModel.setQuanity(quanity);
                pModel.setPrice(price);
                pModel.setUrl(url);
                Log.d("product", "parseProduct: "+pModel);
                this.products.add(pModel);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getStatus(int status) {
        switch (status){
            case 0:
                return "Cancelled";
            case 1:
                return "Placed successfully";
            case 2:
                return "dispatched";
            case 3:
                return "Shipped";
            case 4:
                return "Waiting";
            case 5:
                return "Delivered";
        }
        return "unavilable";
    }

    public void parseAddress(JSONObject address){
        if(address==null){
            tv_o_name.setText("not given");
            tv_o_land.setText("not given");
            tv_o_street.setText("not given");
            tv_o_city.setText("not given");
            tv_o_mobile.setText("not given");
            tv_o_state.setText("not given");
        }else
        try {
            String land=address.getString("landmark");
            String aid=address.getString("aid");
            String city=address.getString("city");
            String name=address.getString("name");
            String state=address.getString("state");
            String street=address.getString("address");
            String mobile=address.getString("mobile");
            tv_o_name.setText(name);
            tv_o_land.setText(land);
            tv_o_street.setText(street);
            tv_o_city.setText(city);
            tv_o_mobile.setText(mobile);
            tv_o_state.setText(state);
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

}
