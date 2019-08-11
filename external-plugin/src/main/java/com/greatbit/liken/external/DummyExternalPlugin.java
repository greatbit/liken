package com.greatbit.liken.external;

import com.greatbit.liken.beans.Launch;
import com.greatbit.liken.beans.LaunchStatus;

import javax.servlet.http.HttpServletRequest;

public class DummyExternalPlugin implements LikenExternalServicePlugin {
    @Override
    public void updateExternalTestcaseStatus(HttpServletRequest request, Launch launch, String testcaseUUID, LaunchStatus status) {

    }

    @Override
    public void deleteExternal(HttpServletRequest request, String launchId) {

    }
}
