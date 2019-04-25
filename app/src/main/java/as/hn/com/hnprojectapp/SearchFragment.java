package as.hn.com.hnprojectapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment {

    @BindView(R.id.editview)
    protected EditText searchText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this,view);

        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //当点击搜索框的时候，触发的事件

                //根据不同的Activity Search框导致不同的事件发生
                String mActivity = getActivity().getClass().toString();
                switch(mActivity){
                    case "AddChatActivity":
                        //在AddChatActivity的frame2中填充通讯录列表

                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.beginTransaction().add(R.id.add_chat_frame2, new WorkmatesFragment()).commit();

                        //刷新activity
                        Intent intent = new Intent(getActivity(), AddChatActivity.class);
                        startActivity(intent);

                        break;
                    default:
                        break;




                }

            }
        });
        return view;

    }
}
