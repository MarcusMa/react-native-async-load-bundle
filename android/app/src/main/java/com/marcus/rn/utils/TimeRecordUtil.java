package com.marcus.rn.utils;

import android.util.Log;
import android.util.Pair;

import java.util.concurrent.ConcurrentHashMap;

public class TimeRecordUtil {

    private static final String TAG = "TimeRecordUtils";

    private static ConcurrentHashMap<String, Pair<Long, Long>> recordMap = new ConcurrentHashMap<>();

    public static synchronized void setStartTime(String tag) {
        Pair<Long, Long> timeRecord = new Pair<>(System.currentTimeMillis(), 0L);
        recordMap.put(tag, timeRecord);
    }

    public static synchronized void setEndTime(String tag) {
        if (recordMap.containsKey(tag)) {
            Pair<Long, Long> timeRecord = new Pair<>(recordMap.get(tag).first, System.currentTimeMillis());
            recordMap.put(tag, timeRecord);
        }
    }

    public static long getTimeCost(String tag) {
        if (recordMap.containsKey(tag)) {
            Pair<Long, Long> timeRecord = recordMap.get(tag);
            return timeRecord.second - timeRecord.first;
        } else {
            return -1L;
        }
    }

    public static String getReadableTimeCostWithUnit(String tag) {
        if (isFinished(tag)) {
            return "" + getTimeCost(tag) + " ms";
        } else {
            return "Unfinished.";
        }
    }

    public static boolean isFinished(String tag) {
        if (recordMap.containsKey(tag)) {
            Pair<Long, Long> timeRecord = recordMap.get(tag);
            return timeRecord.second > 0 && timeRecord.first > 0;
        } else {
            return false;
        }
    }


    public static void printTimeInfo(String tag) {
        if (recordMap.containsKey(tag)) {
            Pair<Long, Long> timeRecord = recordMap.get(tag);
            Log.d(TAG, "----------------------------------");
            Log.d(TAG, "| Start Time (ms): " + timeRecord.first);
            Log.d(TAG, "| End Time (ms): " + timeRecord.second);
            Log.d(TAG, "| Time cost (ms): " + getTimeCost(tag));
            Log.d(TAG, "----------------------------------");
        } else {
            Log.d(TAG, "----------------------------------");
            Log.d(TAG, "| No TimeRecord For " + tag);
            Log.d(TAG, "----------------------------------");
        }
    }

}
