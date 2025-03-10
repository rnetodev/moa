/*
 *    AbruptChangeGenerator.java
 *    Copyright (C) 2012 University of Waikato, Hamilton, New Zealand
 *    @author Albert Bifet (abifet@cs.waikato.ac.nz)
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
 *    
 */
package moa.streams.generators.cd;

import com.github.javacliparser.FloatOption;

import java.util.Random;

public class AbruptChangeGenerator extends AbstractConceptDriftGenerator {

    public FloatOption minNoiseOption = new FloatOption("minNoiseOption", 'x',
            "The min value of noise to be added", 0.0, -2.0, 0.0);

    public FloatOption maxNoiseOption = new FloatOption("maxNoiseOption", 'y',
            "The max value of noise to be added", 0.0, 0.0, 2.0);

    private Integer counter = 0;
    private Double actualRes = 0.2;

    @Override
    protected double nextValue() {
        this.counter++;

        if (this.counter == this.period) {
            if (this.actualRes == 0.2) {
                this.actualRes = 0.8;
            }
            else if (this.actualRes == 0.8) {
                this.actualRes = 0.2;
            }
            this.counter = 0;
            this.change = true;
        } else {
            this.change = false;
        }

//        double res;
//        double t = this.numInstances % this.period;
//        this.change = (t == this.period / 2) ? true : false;
//        res = (t < this.period / 2) ? .2 : .8;

        Random r = new Random();
        double random = this.minNoiseOption.getValue() + r.nextDouble() * (this.maxNoiseOption.getValue() - this.minNoiseOption.getValue());


        return this.actualRes + random;
    }
}
