
package com.github.mikephil.charting.data;

import android.util.Log;

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Data object that represents all data for the BarChart.
 *
 * @author Philipp Jahoda
 */
public class BarData extends BarLineScatterCandleBubbleData<IBarDataSet> {

    /**
     * the width of the bars on the x-axis, in values (not pixels)
     */
    private float mBarWidth = 0.85f;

    public BarData() {
        super();
    }

    public BarData(IBarDataSet... dataSets) {
        super(dataSets);
    }

    public BarData(List<IBarDataSet> dataSets) {
        super(dataSets);
    }

    /**
     * Sets the width each bar should have on the x-axis (in values, not pixels).
     * Default 0.85f
     *
     * @param mBarWidth
     */
    public void setBarWidth(float mBarWidth) {
        this.mBarWidth = mBarWidth;
    }

    public float getBarWidth() {
        return mBarWidth;
    }

    /**
     * Groups all BarDataSet objects this data object holds together by modifying the x-value of their entries.
     * Previously set x-values of entries will be overwritten. Leaves space between bars and groups as specified
     * by the parameters.
     * Do not forget to call notifyDataSetChanged() on your BarChart object after calling this method.
     *
     * @param barSpace   the space between individual bars in values (not pixels) e.g. 0.1f for bar width 1f
     */
    public void groupBars(float barSpace) {

        int setCount = mDataSets.size();
        if (setCount <= 1) {
            throw new RuntimeException("BarData needs to hold at least 2 BarDataSets to allow grouping.");
        }

        List<Float> xPositions = new ArrayList<>();

        for(IBarDataSet set : mDataSets){
            for(int i = 0; i < set.getEntryCount(); i++){
                if(!xPositions.contains(set.getEntryForIndex(i).getX())){
                    xPositions.add(set.getEntryForIndex(i).getX());
                }
            }
        }

        Float groupWidth = getGroupWidth(barSpace);

        for(Float pos : xPositions){
            for(IBarDataSet set : mDataSets){
                int setIndex = mDataSets.indexOf(set);

                for(BarEntry entry : set.getEntriesForXValue(pos)){
                    entry.setDrawAtX(entry.getX()-(groupWidth/2)+(setIndex*(mBarWidth + barSpace)) + mBarWidth/2);
                }
            }
        }

        notifyDataChanged();
    }

    /**
     * In case of grouped bars, this method returns the space an individual group of bar needs on the x-axis.
     *
     * @param barSpace
     * @return
     */
    public float getGroupWidth(float barSpace) {
        return mDataSets.size() * (mBarWidth + barSpace);
    }
}
