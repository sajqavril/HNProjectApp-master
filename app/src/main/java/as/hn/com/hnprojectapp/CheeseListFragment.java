package as.hn.com.hnprojectapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Region;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.business.Data.ProjectPlanEntity;
import com.hn.business.Data.ServiceHelper;
import com.hn.business.Data.UserInfoEntity;
import com.hn.gc.views.CircleProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CheeseListFragment extends Fragment {

    //默认的位置
    private String ListTypeValue="Current";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListTypeValue = getArguments().getString("listType");

        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_cheese_list, container, false);
        setupRecyclerView(rv);

        return rv;
    }

    public static Fragment newInstance(String strListType) {
        CheeseListFragment fragment = new CheeseListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("listType", strListType);
        fragment.setArguments(bundle);
        return fragment;
    }


    private void setupRecyclerView(RecyclerView recyclerView) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        List<ProjectPlanEntity> mDataList = new ArrayList<>();
        //region 通过参数获取不同的数据对象
        //先获取用户对象
        ServiceHelper serviceHelper = new ServiceHelper();
        UserInfoEntity myUser = serviceHelper.LocalToUser(recyclerView.getContext());
//        String strType = getArguments().getString("listType");
        switch (ListTypeValue){
            case "Current":
                mDataList = serviceHelper.GetCurrentProjectPlanList(myUser.getUserCode(), myUser.getUserToken());
                break;
            case "Overtime":
                mDataList = serviceHelper.GetOvertimeProjectPlanList(myUser.getUserCode(), myUser.getUserToken());
                break;
            case "End":
                mDataList = serviceHelper.GetEndProjectPlanList(myUser.getUserCode(), myUser.getUserToken());
                break;
        }
        //endregion

        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                mDataList));
    }


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;

        private Context context;
        private List<ProjectPlanEntity> mDataList;
        private LayoutInflater mLayoutInflater;

        public SimpleStringRecyclerViewAdapter(Context mContext, List<ProjectPlanEntity> mDataList) {
            this.context = mContext;
            this.mDataList = mDataList;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView dateText;
            TextView planTitle;
            android.support.v7.widget.CardView cardView;
            ImageView newsIcon;
            CircleProgressView mCircleBar;
            CircleProgressView mPercentageBar;
            Button btnAdd;
            Button btnView;

            public ViewHolder(final View itemView) {
                super(itemView);
                cardView = (android.support.v7.widget.CardView) itemView.findViewById(R.id.card_view);
                dateText = (TextView) itemView.findViewById(R.id.base_swipe_item_Date);
                planTitle = (TextView) itemView.findViewById(R.id.base_swipe_item_title);
                mCircleBar = (CircleProgressView) itemView.findViewById(R.id.circleProgressbar);
                mPercentageBar = (CircleProgressView) itemView.findViewById(R.id.endPercentage);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + planTitle.getText();
            }
        }

//        public SimpleStringRecyclerViewAdapter(Context context, List<String> items) {
//            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
//            mBackground = mTypedValue.resourceId;
//            mValues = items;
//        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_base_swipe_list, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SimpleStringRecyclerViewAdapter.ViewHolder viewHolder, int position) {
            final int j = position;
            ProjectPlanEntity entity = mDataList.get(position);

            if (null == entity)
                return;

            ViewHolder holder = (ViewHolder) viewHolder;
            bindNormalItem(entity, holder.planTitle, holder.dateText, holder.mCircleBar, holder.mPercentageBar);

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ProjectPlanEntity entity = mDataList.get(j);
                    Intent intent = new Intent(context, PlanVIewActivity.class);
                    intent.putExtra("plan", entity);
                    context.startActivity(intent);
                }
            });
        }


        void bindNormalItem(ProjectPlanEntity entity, TextView planTitle, TextView planDate, CircleProgressView mCircleBar, CircleProgressView mPercentageBar) {

            planDate.setText(entity.GetPublishDate());
            planTitle.setText(entity.getPlanTitle());

            mCircleBar.setProgress(entity.getTimeUsePercentage());
            mCircleBar.setmTxtHint1("使用了");
            mCircleBar.setmTxtHint2("的时间");

            mPercentageBar.setProgress(entity.getEndPercentage());
            mPercentageBar.setmTxtHint1("已完成");
            mPercentageBar.setmTxtHint2("工作量");
        }


        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }
}
