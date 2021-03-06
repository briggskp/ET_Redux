/*
 * CorrectedIntensitiesDataView.java
 *
 * Created Jul 6, 2011
 *
 * Copyright 2006-2015 James F. Bowring and www.Earth-Time.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.earthtime.Tripoli.dataViews.simpleViews.usedByReflection;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.JLayeredPane;
import org.earthtime.Tripoli.dataModels.DataModelInterface;
import org.earthtime.Tripoli.dataModels.RawIntensityDataModel;
import org.earthtime.Tripoli.dataViews.AbstractRawDataView;
import org.earthtime.Tripoli.fractions.TripoliFraction;
import org.earthtime.UPb_Redux.ReduxConstants;
import org.earthtime.utilities.TicGeneratorForAxes;

/**
 *
 * @author James F. Bowring
 */
public class CorrectedIntensitiesDataView extends AbstractRawDataView {

    /**
     *
     */
    public static int DEFAULT_WIDTH_OF_PANE = 128;
    //
    //private DataModelInterface rawRatioDataModel;

    /**
     *
     * @param sampleSessionDataView
     * @param tripoliFraction
     * @param rawIsotopeDataModel
     * @param bounds
     * @param invokeMouseListener
     */
    public CorrectedIntensitiesDataView (//
            JLayeredPane sampleSessionDataView, //
            TripoliFraction tripoliFraction,//
            DataModelInterface rawIsotopeDataModel,//
            Rectangle bounds,//
            boolean invokeMouseListener ) {
        super( sampleSessionDataView, tripoliFraction, bounds, invokeMouseListener, true );

        this.rawRatioDataModel = rawIsotopeDataModel;

    }

    /**
     *
     * @param g2d
     */
    @Override
    public void paint ( Graphics2D g2d ) {
        super.paint( g2d );

        if ( ((RawIntensityDataModel) rawRatioDataModel).isBelowDetection() ) {
            setBackground(ReduxConstants.palePinkBelowDetection );
            g2d.drawString("BELOW DETECTION", 25, 25);
        }

        // draw corrected onPeak intensities 
        for (int i = 0; i < myOnPeakData.length; i ++) {
            Shape intensity = new java.awt.geom.Ellipse2D.Double( //
                    mapX( myOnPeakNormalizedAquireTimes[i] ), mapY( myOnPeakData[i] ), 1, 1 );
            g2d.setPaint( determineDataColor( i, Color.black ) );

            // dec 2012 for visualization only
            if ( myOnPeakData[i] <= Double.MIN_VALUE ) {
                g2d.setPaint( Color.red );
            }

            g2d.draw( intensity );
        }
    }

    /**
     *
     */
    @Override
    public void preparePanel () {

        this.removeAll();

        setDisplayOffsetY( 0.0 );
        setDisplayOffsetX( 0.0 );

        // walk intensities and get min and max for axes
        myOnPeakData = ((RawIntensityDataModel) rawRatioDataModel).getOnPeakCorrectedCountsPerSecondAsRawIntensities();// .getOnPeakVirtualCollector().getCorrectedIntensities();

        // normalize aquireTimes
        myOnPeakNormalizedAquireTimes = ((RawIntensityDataModel) rawRatioDataModel).getNormalizedOnPeakAquireTimes();

        // X-axis lays out time evenly spaced
        minX = myOnPeakNormalizedAquireTimes[0];
        maxX = myOnPeakNormalizedAquireTimes[myOnPeakNormalizedAquireTimes.length - 1];

        // Y-axis is intensities as voltages plus or minus
        minY = Double.MAX_VALUE;
        maxY =  - Double.MAX_VALUE;

        // assume background is less than onPeak
        for (int i = 0; i < myOnPeakData.length; i ++) {
            minY = Math.min( minY, myOnPeakData[i] );
        }

        for (int i = 0; i < myOnPeakData.length; i ++) {
            maxY = Math.max( maxY, myOnPeakData[i] );
        }

        // adjust margins for unknowns
        if (  ! tripoliFraction.isStandard() ) {
            double yMarginStretch = TicGeneratorForAxes.generateMarginAdjustment( minY, maxY, 0.05 );
            minY -= yMarginStretch;
            maxY += yMarginStretch;
        }

    }

    /**
     * @return the rawRatioDataModel
     */
    @Override
    public DataModelInterface getDataModel () {
        return rawRatioDataModel;
    }
//    /**
//     * 
//     * @param included
//     */
//    @Override
//    public void toggleFractionInclusion ( boolean included ) {
//        tripoliFraction.toggleAllData( included );
//        tripoliFraction.updateCorrectedRatioStatistics();
//        ((TripoliFractionViewInterface) tripoliFraction).setShowVerticalLineAtThisIndex( -1 );
//        repaintFraction();
//    }
}
