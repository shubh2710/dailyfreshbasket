package org.dailyfreshbasket.com.in.informations;

import android.util.Log;

/**
 * Created by shubham on 4/1/2017.
 */

public class filtersortInformation {
    MinMax list0=new MinMax(0,0,0,false);
    MinMax list1=new MinMax(0,0,0,false);
    MinMax list2=new MinMax(0,0,0,false);
    MinMax list3=new MinMax(0,0,0,false);
    private  String SortApply="0,";
    String queary;
    public filtersortInformation(MinMax list0, MinMax list1, MinMax list2, MinMax list3, String qury,int isSortapplyed){
        if(list0!=null)
            this.list0=list0;
        if(list0!=null)
            this.list1=list1;
        if(list0!=null)
            this.list2=list2;
        if(list0!=null)
            this.list3=list3;

            SortApply=isSortapplyed+",";
        setFilter();
        setMs(list0.getMin()+"",list2.getMin()+"",list2.getMax()+"",list1.getMin()+"",list3.getMin()+"");
        queary=qury;
    }
    public void setIsSortNumber(int num){
        SortApply=num+",";
        setFilter();
    }
    public String getFilterString() {
        return filterString;
    }

    String filterString;
    String m0,m1,m2,m3,m4;

    public void setMs(String Dicount,String MinPrice,String MaxPrice,String srate,String prate) {
        this.m0 = Dicount;
        this.m1 = MinPrice;
        this.m2 = MaxPrice;
        this.m3 = srate;
        this.m4 = prate;
    }
    public String getMinDiscount() {
        return m0;
    }

    public String getMinPrice() {
        return m1;
    }

    public String getMaxPrice() {
        return m2;
    }

    public String getMinSRate() {
        return m3;
    }
    public String getMinPRate() {
        return m4;
    }

    //0=discount 1=prating 2=price 3=srating
    private void setFilter(){
        filterString=null;
        if(list0.getisapply() && list1.getisapply() && list2.getisapply() && list3.getisapply()){
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");
            filterString=SortApply+"price_"+"discount_"+"prate_"+"srate";
        }
        else if(list0.getisapply() &&list1.getisapply()&& list2.getisapply()){

            filterString=SortApply+"prate_"+"discount_"+"price";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        }

        else if(list0.getisapply() &&list1.getisapply()&& list3.getisapply()){
            filterString=SortApply+"discount_"+"prate_"+"srate";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        }

        else if(list0.getisapply() &&list2.getisapply()&& list3.getisapply()){
            filterString=SortApply+"price_"+"discount_"+"srate";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        }
        else if(list1.getisapply() &&list2.getisapply()&& list3.getisapply()){
            filterString=SortApply+"price_"+"prate_"+"srate";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        }
        else if(list0.getisapply() &&list1.getisapply()){
            filterString=SortApply+"discount_"+"prate_";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        }
        else if(list0.getisapply() && list2.getisapply()){
            filterString=SortApply+"price_"+"discount";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        }
        else if(list0.getisapply() &&list3.getisapply()){
            filterString=SortApply+"discount_"+"srate";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        }else if(list1.getisapply() &&list2.getisapply()){
            filterString=SortApply+"price_"+"prate";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        }else if(list1.getisapply() &&list3.getisapply()){
            filterString=SortApply+"prate_"+"srate";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        }
        else if(list2.getisapply() && list3.getisapply()){

            filterString=SortApply+"price_"+"srate";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        }else if(list0.getisapply()){
            filterString=SortApply+"discount";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        } else if(list1.getisapply()){
            filterString=SortApply+"prate";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        }else if(list2.getisapply()){
            filterString=SortApply+"price";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        }else if(list3.getisapply()){
            filterString=SortApply+"srate";
            Log.d("FilterDataQuary","filter"+" "+queary+" "+"all"+" "+list0.getMin()+" "+list1.getMin()+" "+list2.getMin()+" "+list2.getMax()+" "+list3.getMin()+"");

        }
    }
    public void showData(){
        Log.d("FilterDataInfo",filterString);
        Log.d("FilterDataInfoMIN",getMaxPrice()+" "+getMinPrice()+" "+getMinDiscount()+" "+getMinPRate()+" "+getMinSRate());
    }
}
