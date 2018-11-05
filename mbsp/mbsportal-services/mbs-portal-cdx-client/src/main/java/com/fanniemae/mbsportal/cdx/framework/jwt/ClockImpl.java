package com.fanniemae.mbsportal.cdx.framework.jwt;

import java.util.Date;

import com.fanniemae.mbsportal.cdx.framework.jwt.interfaces.Clock;

final class ClockImpl implements Clock {

    ClockImpl() {
    }

    @Override
    public Date getToday() {
        return new Date();
    }
}
