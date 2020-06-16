package com.example.mopapov2.DeviceConfig.CompanyModel.CompanyInterface;

import com.example.mopapov2.DeviceConfig.CompanyModel.Company;

import java.util.List;

public interface IFFirebaseLoadDoneCompny {
    void onFirebaseLoadSuccessCompany(List<Company> companyList);
    void onFirebaseLoadfailCompany(String message);
}
