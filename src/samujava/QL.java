/*
 * @brief SAMU - the potential ancestor of developmental robotics chatter bots
 *
 * @file ql.hpp
 * @author  Norbert Bátfai <nbatfai@gmail.com>
 * @version 0.0.1
 *
 * @section LICENSE
 *
 * Copyright (C) 2015 Norbert Bátfai, batfai.norbert@inf.unideb.hu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @section DESCRIPTION
 * SAMU
 * 
 * The main purpose of this project is to allow the evaluation and 
 * verification of the results of the paper entitled "A disembodied 
 * developmental robotic agent called Samu Bátfai". It is our hope 
 * that Samu will be the ancestor of developmental robotics chatter 
 * bots that will be able to chat in natural language like humans do.
 *
 */
package samujava;

import java.util.*;
import java.util.Map.Entry;

public class QL {

    public class Perceptron {

        public Perceptron(int nof, int... va_args) {

            n_layers = nof;
            units = new double[n_layers][];

            n_units = new int[n_layers];

            for (int i = 0; i < n_layers; ++i) {

                n_units[i] = va_args[i];

                units[i] = new double[n_units[i]];
            }
            weights = new double[n_layers - 1][][];
            Random random = new Random();

            for (int i = 1; i < n_layers; ++i) {

                weights[i - 1] = new double[n_units[i]][];

                for (int j = 0; j < n_units[i]; ++j) {
                    weights[i - 1][j] = new double[n_units[i - 1]];

                    for (int k = 0; k < n_units[i - 1]; ++k) {

                        weights[i - 1][j][k] = randDoubleGen(random);

                    }
                }
            }
        }

        public double randDoubleGen(Random rand) {
            return -1 + (rand.nextDouble() * 2);
        }

        public double sigmoid(double x) {
            return 1.0 / (1.0 + Math.exp(-x));
        }

        public double ThisWasAnOperator(double img[]) {
            units[0] = img;

            for (int i = 1; i < n_layers; ++i) {
                for (int j = 0; j < n_units[i]; ++j) {
                    units[i][j] = 0.0;

                    for (int k = 0; k < n_units[i - 1]; ++k) {
                        units[i][j] += weights[i - 1][j][k] * units[i - 1][k];
                    }

                    units[i][j] = sigmoid(units[i][j]);

                }
            }

            return sigmoid(units[n_layers - 1][0]);

        }

        public void learning(double img[], double q, double prev_q) {
            double y[] = {q};

            learning(img, y);
        }

        public void learning(double img[], double y[]) {
            ThisWasAnOperator(img);

            units[0] = img;

            double backs[][] = new double[n_layers - 1][];

            for (int i = 0; i < n_layers - 1; ++i) {
                backs[i] = new double[n_units[i + 1]];
            }

            int i = n_layers - 1;

            for (int j = 0; j < n_units[i]; ++j) {
                backs[i - 1][j] = sigmoid(units[i][j]) * (1.0 - sigmoid(units[i][j])) * (y[j] - units[i][j]);

                for (int k = 0; k < n_units[i - 1]; ++k) {
                    weights[i - 1][j][k] += (0.2 * backs[i - 1][j] * units[i - 1][k]);
                }

            }

            for (int h = n_layers - 2; h > 0; --h) {
                for (int j = 0; j < n_units[h]; ++j) {

                    double sum = 0.0;

                    for (int l = 0; l < n_units[h + 1]; ++l) {
                        sum += 0.19 * weights[h][l][j] * backs[h][l];
                    }

                    backs[h - 1][j] = sigmoid(units[h][j]) * (1.0 - sigmoid(units[h][j])) * sum;

                    for (int k = 0; k < n_units[h - 1]; ++k) {
                        weights[h - 1][j][k] += (0.19 * backs[h - 1][j] * units[h - 1][k]);
                    }
                }
            }

        }

        private int n_layers;
        private int n_units[];
        private double units[][];
        private double weights[][][];
    }

    public QL(SPOTriplet triplet) {
        this();
    }

    public QL() {

        prev_image = new double[65536];

        prcps = new HashMap<SPOTriplet, Perceptron>();
        frqs = new HashMap<SPOTriplet, Map<String, Integer>>();
    }

    public double f(double u, int n) {

        if (n < N_e) {
            return 1.0;
        } else {
            return u;
        }
    }

    double max_ap_Q_sp_ap(double image[]) {
        double q_spap;
        double min_q_spap = -java.lang.Double.MAX_VALUE;

        for (Iterator<Entry<SPOTriplet, Perceptron>> it = prcps.entrySet().iterator(); it.hasNext();) {

            Entry<SPOTriplet, Perceptron> thisEntry = (Entry<SPOTriplet, Perceptron>) it.next();
            q_spap = ((Perceptron) thisEntry.getValue()).ThisWasAnOperator(image);
            ;
            if (q_spap > min_q_spap) {
                min_q_spap = q_spap;
            }
        }

        return min_q_spap;
    }

    SPOTriplet argmax_ap_f(String prg, double image[]) {
        double min_f = -java.lang.Double.MAX_VALUE;
        SPOTriplet ap = null;

        for (Iterator<Entry<SPOTriplet, Perceptron>> it = prcps.entrySet().iterator(); it.hasNext();) {
            Entry<SPOTriplet, Perceptron> thisEntry = (Entry<SPOTriplet, Perceptron>) it.next();
            double q_spap = ((Perceptron) thisEntry.getValue()).ThisWasAnOperator(image);
            double explor = f(q_spap, frqs.get(thisEntry.getKey()).get(prg));

            if (explor >= min_f) {
                min_f = explor;
                ap = (SPOTriplet) thisEntry.getKey();
            }
        }

        return ap;
    }

    SPOTriplet ThisWasAnOperator(SPOTriplet triplet, String prg, double image[]) {

        double reward = 3.0 * triplet.cmp(prev_action) - 1.5;

        if (prcps.get(triplet) == null) {
            prcps.put(triplet, new Perceptron(3, 256 * 256, 80, 1));
        }

        SPOTriplet action = triplet;

        if (prev_reward > -java.lang.Double.MAX_VALUE && frqs.get(prev_action) != null) {
            frqs.get(prev_action).put(prev_state, frqs.get(prev_action).get(prev_state) + 1);

            double max_ap_q_sp_ap = max_ap_Q_sp_ap(image);

            for (int z = 0; z < 10; ++z) {
                double nn_q_s_a = (prcps.get(prev_action)).ThisWasAnOperator(prev_image);

                double q_q_s_a = nn_q_s_a
                        + alpha(frqs.get(prev_action).get(prev_state)) * (reward + gamma * max_ap_q_sp_ap - nn_q_s_a);

                prcps.get(prev_action).learning(prev_image, q_q_s_a, nn_q_s_a);

                System.out.println("### " + (q_q_s_a - nn_q_s_a) + " " + q_q_s_a + " " + nn_q_s_a);
            }

            action = argmax_ap_f(prg, image);
        }

        prev_state = prg; // s <- s'
        prev_reward = reward; // r <- r'
        prev_action = action; // a <- a'
        prev_image = Arrays.copyOf(image, image.length);

        return action;
    }

    public double reward() {
        return prev_reward;
    }

    public double alpha(int n) {
        return 1.0 / (((double) n) + 1.0);
    }

    private QL(QL oth) {

    }

    private double gamma = .2;

    private Map<SPOTriplet, Perceptron> prcps;

    private Map<SPOTriplet, Map<String, Integer>> frqs;

    private SPOTriplet prev_action;

    private String prev_state;

    private double prev_reward = -java.lang.Double.MAX_VALUE;

    private double prev_image[];

    private static final int N_e = 10;

}
