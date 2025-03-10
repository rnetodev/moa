/*
 *    DDM.java
 *    Copyright (C) 2008 University of Waikato, Hamilton, New Zealand
 *    @author Manuel Baena (mbaena@lcc.uma.es)
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package moa.classifiers.core.driftdetection;

import com.github.javacliparser.FloatOption;
import com.github.javacliparser.IntOption;
import moa.core.ObjectRepository;
import moa.tasks.TaskMonitor;

/**
 * Drift detection method based in Geometric Moving Average Test
 *
 *
 * @author Manuel Baena (mbaena@lcc.uma.es)
 * @version $Revision: 7 $
 */
public class GeometricMovingAverageDM extends AbstractChangeDetector {

    private static final long serialVersionUID = -3518369648142099719L;

    public IntOption minNumInstancesOption = new IntOption(
            "minNumInstances",
            'n',
            "The minimum number of instances before permitting detecting change.",
            30, 0, Integer.MAX_VALUE);

    public FloatOption lambdaOption = new FloatOption("lambda", 'l',
            "Threshold parameter of the Geometric Moving Average Test", 1, 0.0, Float.MAX_VALUE);

    public FloatOption alphaOption = new FloatOption("alpha", 'a',
            "Alpha parameter of the Geometric Moving Average Test", .99, 0.0, 1.0);

    private double m_n;

    private double sum;

    private double x_mean;
                                                                                            
    private double alpha;

    private double delta;

    private double lambda;

    public GeometricMovingAverageDM() {
        resetLearning();
    }

    @Override
    public void resetLearning() {
        m_n = 1.0;
        x_mean = 0.0;
        sum = 0.0;
        alpha = this.alphaOption.getValue();
        lambda = this.lambdaOption.getValue();
    }

    @Override
    public void input(double x) {
        System.out.print(x + ", ");

        // It monitors the error rate
        if (this.isChangeDetected == true || this.isInitialized == false) {
            resetLearning();
            this.isInitialized = true;
        }

        x_mean = x_mean + (x - x_mean) / m_n;
        sum = alpha * sum + ( 1.0- alpha) * (x - x_mean);


        m_n++;

        // System.out.print(prediction + " " + m_n + " " + (m_p+m_s) + " ");
        this.estimation = x_mean;
        this.isChangeDetected = false;
        this.isWarningZone = false;
        this.delay = 0;

        if (m_n < this.minNumInstancesOption.getValue()) {
            return;
        }

        if (sum > this.lambda) {
            this.isChangeDetected = true;
        } 
    }

    @Override
    public void getDescription(StringBuilder sb, int indent) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor,
            ObjectRepository repository) {
        // TODO Auto-generated method stub
    }
}