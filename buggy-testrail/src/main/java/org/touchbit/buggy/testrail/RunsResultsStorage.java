package org.touchbit.buggy.testrail;

import org.touchbit.testrail4j.jackson2.model.TRResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Test Run Result Store with Run ID split
 * <p>
 * Created by Oleg Shaburov on 13.11.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("WeakerAccess")
public class RunsResultsStorage {

    /**
     * key   - TestRail run id
     * value - list of test result objects {@link TRResult}
     */
    private final Map<Long, List<TRResult>> runsResults;

    public RunsResultsStorage() {
        this(new HashMap<>());
    }

    public RunsResultsStorage(Map<Long, List<TRResult>> map) {
        runsResults = Collections.synchronizedMap(map);
    }

    public void add(final Long runID, final TRResult result) {
        List<TRResult> list = runsResults.get(runID);
        if (list == null) {
            list = new ArrayList<>();
            list.add(result);
            runsResults.put(runID, list);
        } else {
            if (!list.contains(result)) {
                runsResults.get(runID).add(result);
            }
        }
    }

    public List<TRResult> getResultsForRun(final Long runID) {
        return runsResults.get(runID);
    }

    public List<Long> getRunIDs() {
        return new ArrayList<>(runsResults.keySet());
    }

    public List<Long> getCaseIDsForRun(final Long runID) {
        return getResultsForRun(runID).stream()
                .map(TRResult::getCaseId)
                .collect(Collectors.toList());
    }

}
