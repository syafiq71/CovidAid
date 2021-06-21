package com.android.covidaid;

import java.util.List;

public interface IAllStateLoadListener {
    void onAllSalonLoadSuccess(List<String> areaNameList);
    void onAllSalonLoadFailed(String message);
}
