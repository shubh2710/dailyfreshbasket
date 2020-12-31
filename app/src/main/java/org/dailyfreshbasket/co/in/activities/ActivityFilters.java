package org.dailyfreshbasket.co.in.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.informations.MinMax;

import java.util.ArrayList;


public class ActivityFilters extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView catagory,options;
    private String[] catagoryList={"Discount","Rating","Price","Shopes rateings"};
    private int currentSelectedList=0;
    private Button apply;
    ArrayList<Integer> checkpos1=new ArrayList<>();
    ArrayList<Integer> checkpos2=new ArrayList<>();
    ArrayList<Integer> checkpos3=new ArrayList<>();
    ArrayList<Integer> checkpos4=new ArrayList<>();

    private ArrayList<MinMax> minmaxlist=new ArrayList<>();

    private String[] optionList_1={"none","more then 10%","more then 50%","more then 70%","above 80%"};
    private String[] optionList_2={"none","more then 1","more then 2","more then 3","more then 4","5"};
    private String[] optionList_3={"none","100-200","200-300","300-500","500-700","700-1000","1000-1300","1300-2000","above 2000"};
    private String[] optionList_4={"none","more then 1","more then 2","more then 3","more then 4","5"};
    private ArrayList<String[]> optionsList=new ArrayList<>();
    private ArrayList<ArrayList<Integer>> gotselected=new ArrayList<>();
    private ArrayList<ArrayList<String>> allSecection=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allSecection.clear();
        optionsList.clear();
        currentSelectedList=0;
        setContentView(R.layout.activity_filters);
        catagory=(ListView)findViewById(R.id.lv_activityfilter_catagory);
        options=(ListView)findViewById(R.id.lv_activityfilter_options);
        for(int i=0;i<optionsList.size();i++)
        gotselected.add(new ArrayList<Integer>());
        optionsList.add(optionList_1);
        optionsList.add(optionList_2);
        optionsList.add(optionList_3);
        optionsList.add(optionList_4);
        for(int i=0;i<optionsList.size();i++)
        allSecection.add(new ArrayList<String>());
        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, catagoryList);

        apply=(Button)findViewById(R.id.b_activityFilter_apply);
        apply.setOnClickListener(this);
        catagory.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        catagory.setSelector(android.R.color.holo_blue_light);
        catagory.setAdapter(myarrayAdapter);
        catagory.setOnItemClickListener(this);
        ArrayAdapter<String> myarrayAdapteroptions1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, optionsList.get(0));
        options.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        options.setAdapter(myarrayAdapteroptions1);
    }
    @Override
    public void onClick(View v) {
        SparseBooleanArray checked = options.getCheckedItemPositions();
        ArrayList<Integer> checkpos=new ArrayList<>();
        for(int i=0;i<options.getCount();i++)
            if(checked.get(i))
                checkpos.add(i);
        saveToPos(checkpos);
        Intent returnIntent = new Intent();
        applyFilter();
        returnIntent.putExtra("result",minmaxlist);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SparseBooleanArray checked = options.getCheckedItemPositions();
        ArrayList<Integer> checkpos=new ArrayList<>();
        for(int i=0;i<options.getCount();i++)
            if(checked.get(i))
                checkpos.add(i);
        saveToPos(checkpos);
        switch(position){
            case 0:
                ArrayAdapter<String> myarrayAdapteroptions1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, optionsList.get(position));
                options.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                options.setAdapter(myarrayAdapteroptions1);
                currentSelectedList=0;
                showSave(options,currentSelectedList);
                break;
            case 1:
                ArrayAdapter<String> myarrayAdapteroptions2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, optionsList.get(position));
                options.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                options.setAdapter(myarrayAdapteroptions2);
                currentSelectedList=1;
                showSave(options,currentSelectedList);
                break;
            case 2:
                ArrayAdapter<String> myarrayAdapteroptions3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, optionsList.get(position));
                options.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                options.setAdapter(myarrayAdapteroptions3);
                currentSelectedList=2;
                showSave(options,currentSelectedList);
                break;
            case 3:
                ArrayAdapter<String> myarrayAdapteroptions4 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, optionsList.get(position));
                options.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                options.setAdapter(myarrayAdapteroptions4);
                currentSelectedList=3;
                showSave(options,currentSelectedList);
                break;
        }
    }
    private void showSave(ListView options, int currentSelectedList) {
        int len=options.getCount();
        for(int i=0;i<len;i++){
            if(gotselected.get(currentSelectedList).contains(i)){
                options.setItemChecked(i,true);
            }
        }
    }

    private void saveToPos(ArrayList<Integer> checkpos) {
        gotselected.clear();
        switch (currentSelectedList){
            case 0:
                 checkpos1=checkpos;
                break;
            case 1:
                checkpos2=checkpos;
                break;
            case 2:
               checkpos3=checkpos;
                break;
            case 3:
               checkpos4=checkpos;
                break;
        }
            gotselected.add(checkpos1);
            gotselected.add(checkpos2);
            gotselected.add(checkpos3);
            gotselected.add(checkpos4);


        //Log.d("check save",gotselected.toString());
    }
    public void convertFilterData(){
    }
   public void setfilterdata(int listnumber,int position){
       switch (listnumber){
           case 0:
               switch (position){
                   case 0:
                       minmaxlist.add(new MinMax(0,100,listnumber,false));
                       break;
                   case 1:
                       minmaxlist.add(new MinMax(10,100,listnumber,true));
                        break;
                   case 2:
                       minmaxlist.add(new MinMax(50,100,listnumber,true));
                       break;
                   case 3:
                       minmaxlist.add(new MinMax(70,100,listnumber,true));
                       break;
                   case 4: minmaxlist.add(new MinMax(80,100,listnumber,true));
                       break;
               }
               break;
           case 1:
               switch (position){
                   case 0:
                       minmaxlist.add(new MinMax(0,0,listnumber,false));
                       break;
                   case 1: minmaxlist.add(new MinMax(1,5,listnumber,true));
                       break;
                   case 2: minmaxlist.add(new MinMax(2,5,listnumber,true));
                       break;
                   case 3: minmaxlist.add(new MinMax(3,5,listnumber,true));
                       break;
                   case 4: minmaxlist.add(new MinMax(4,5,listnumber,true));
                       break;
                   case 5: minmaxlist.add(new MinMax(5,5,listnumber,true));
                       break;
               }
               break;
           case 2:
               switch (position){
                   case 0:
                       minmaxlist.add(new MinMax(0,0,listnumber,false));
                       break;
                   case 1: minmaxlist.add(new MinMax(100,200,listnumber,true));
                       break;
                   case 2: minmaxlist.add(new MinMax(200,300,listnumber,true));
                       break;
                   case 3: minmaxlist.add(new MinMax(300,500,listnumber,true));
                       break;
                   case 4: minmaxlist.add(new MinMax(500,700,listnumber,true));
                       break;
                   case 5: minmaxlist.add(new MinMax(700,1000,listnumber,true));
                       break;
                   case 6: minmaxlist.add(new MinMax(1000,1300,listnumber,true));
                       break;
                   case 7: minmaxlist.add(new MinMax(1300,2000,listnumber,true));
                       break;
                   case 8: minmaxlist.add(new MinMax(2000,5000,listnumber,true));
                       break;
               }
               break;
           case 3:
               switch (position){
                   case 0:
                       minmaxlist.add(new MinMax(0,5,listnumber,false));
                       break;
                   case 1: minmaxlist.add(new MinMax(1,5,listnumber,true));
                       break;
                   case 2: minmaxlist.add(new MinMax(2,5,listnumber,true));
                       break;
                   case 3: minmaxlist.add(new MinMax(3,5,listnumber,true));
                       break;
                   case 4: minmaxlist.add(new MinMax(4,5,listnumber,true));
                       break;
                   case 5: minmaxlist.add(new MinMax(5,5,listnumber,true));
                       break;
               }
               break;
       }

   }
    private void applyFilter() {
        for(int j=0;j<4;j++){
            int len=optionsList.get(j).length;
            boolean font=false;
        for(int i=0;i<len;i++){
            if(gotselected.get(j).contains(i)){
                font=true;
                setfilterdata(j,i);
            }

        }
            if(!font)
            minmaxlist.add(new MinMax(0,0,j,false));
    }
       /* for(MinMax min:minmaxlist)
        if(min.getisapply())
            Log.d("filtered",min.getListNumber()+" "+min.getMin()+" "+min.getMax()+""+min.getisapply());
            */
    }
}