/*
 * RatiosDataViewEditable.java
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
package org.earthtime.ratioDataViews;

import java.awt.Dimension;
import org.earthtime.UPb_Redux.valueModelPanelViews.ValueModelsPanelViewEditable;
import org.earthtime.exceptions.ETException;
import org.earthtime.matrices.matrixViews.MatrixGridViewEditable;
import org.earthtime.matrices.matrixViews.MatrixGridViewNotEditable;
import org.earthtime.ratioDataModels.AbstractRatiosDataModel;
import org.earthtime.ratioDataModels.pbBlankICModels.PbBlankICModel;

/**
 *
 * @author James F. Bowring
 */
public class RatiosDataViewEditable extends AbstractRatiosDataView {

    /**
     *
     *
     * @param ratiosDataModel
     * @param parentDimension the value of parentDimension
     * @param showTableOnly the value of showTableOnly
     */
    public RatiosDataViewEditable ( AbstractRatiosDataModel ratiosDataModel, Dimension parentDimension, boolean showTableOnly) {
        super( ratiosDataModel, parentDimension );

        this.valueModelsPanelView = new ValueModelsPanelViewEditable(//
                ratiosDataModel.getData(), this );

        this.covarianceVarUnctMatrixView = new MatrixGridViewNotEditable( ratiosDataModel.getDataCovariancesVarUnct(), showTableOnly );
        this.correlationVarUnctMatrixView = new MatrixGridViewEditable( ratiosDataModel.getDataCorrelationsVarUnct(), this, showTableOnly, true);

        initView(true);

    }

    /**
     *
     *
     * @param checkCovarianceValidity the value of checkCovarianceValidity
     * @throws org.earthtime.exceptions.ETException
     */
    
    @Override
    protected void saveEdits (boolean checkCovarianceValidity)
        throws ETException {
        super.saveEdits(checkCovarianceValidity);
        valueModelsPanelView.saveEdits();
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main ( String[] args ) throws Exception {
        AbstractRatiosDataView testView = new RatiosDataViewEditable( PbBlankICModel.createNewInstance(), null, false );

        testView.displayModelInFrame();

    }
}
