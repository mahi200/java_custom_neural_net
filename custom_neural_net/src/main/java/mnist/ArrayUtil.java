package mnist;

/*
 * Artificial Intelligence for Humans
 * Volume 3: Deep Learning and Neural Networks
 * Java Version
 * http://www.aifh.org
 * http://www.jeffheaton.com
 *
 * Code repository:
 * https://github.com/jeffheaton/aifh
 *
 * Copyright 2014-2015 by Jeff Heaton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For more information on Heaton Research copyrights, licenses
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */

import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * Array utilities.
 */
public class ArrayUtil {

	/**
     * Return the index of the largest value in a vector.
     * @param data The vector.
     * @return The index of the largest value.
     */
    public static int indexOfLargest(float[] data) {
        int result = 0;

        for (int i = 0; i < data.length; i++) {
            if (data[i] > data[result])
                result = i;
        }

        return result;
    }
	
    /**
     * Return the index of the largest value in a vector.
     * @param data The vector.
     * @return The index of the largest value.
     */
    public static int indexOfLargest(double[] data) {
        int result = 0;

        for (int i = 0; i < data.length; i++) {
            if (data[i] > data[result])
                result = i;
        }

        return result;
    }

    /**
     * Return the index of the largest value in a vector.
     * @param data The vector.
     * @return The index of the largest value.
     */
    public static int indexOfLargest(INDArray data) {
        int result = -1;
        double m = 0;
        int idx = 0;

        for(int row=0;row<data.size(0);row++) {
            for (int col = 0; col < data.size(1); col++) {
                if ( m < data.getDouble(row,col)) {
                    result = idx;
                    m = data.getDouble( row, col);
                }
                idx++;
            }
        }

        return result;
    }
}
