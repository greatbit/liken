package com.greatbit.liken.external.quack;

import com.greatbit.liken.beans.Launch;
import com.greatbit.liken.beans.LaunchStatus;
import com.greatbit.liken.external.LikenExternalServicePlugin;

import javax.servlet.http.HttpServletRequest;

public class QuackExternalPlugin implements LikenExternalServicePlugin {
    @Override
    public void updateExternalTestcaseStatus(HttpServletRequest request, Launch launch, String testcaseUUID, LaunchStatus status) {

    }

    @Override
    public void deleteExternal(HttpServletRequest request, String launchId) {

    }
}
