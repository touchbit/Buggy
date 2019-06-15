package org.touchbit.buggy.example.min.listeners;

import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.testrail.StatusMapper;
import org.touchbit.testrail4j.core.type.Statuses;

/**
 * Created by Oleg Shaburov on 12.01.2019
 * shaburov.o.a@gmail.com
 */
public class StatusMap implements StatusMapper<Status> {

    @Override
    public long getId(Status status) {
        switch (status) {
            case SUCCESS:       return Statuses.PASSED.getId();
            case BLOCKED:       return Statuses.BLOCKED.getId();
            case UNTESTED:      return Statuses.UNTESTED.getId();
            case FAILED:        return Statuses.FAILED.getId();
            case FIXED:         return Statuses.CUSTOM_STATUS1.getId();
            case IMPLEMENTED:   return Statuses.CUSTOM_STATUS2.getId();
            case CORRUPTED:     return Statuses.CUSTOM_STATUS3.getId();
            case EXP_FIX:       return Statuses.CUSTOM_STATUS4.getId();
            case SKIP:          return Statuses.CUSTOM_STATUS5.getId();
            case EXP_IMPL:      return Statuses.CUSTOM_STATUS6.getId();
            default:
                throw new BuggyConfigurationException("Unhandled status received: " + status);
        }
    }

}
