/*
 * @brief SAMU - the potential ancestor of developmental robotics chatter bots
 *
 * @file nlp.hpp
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

class SPOTriplet {

    public String s;
    public String p;
    public String o;

    SPOTriplet() {
        s = new String();
        p = new String();
        o = new String();
    }

    SPOTriplet(String sn, String pn, String on) {
        s = sn;
        p = pn;
        o = on;
    }

    public void writeOut(SPOTriplet triplet) {

        System.out.print(triplet.s + " " + triplet.p + " " + triplet.o);

    }

    public Boolean equals(SPOTriplet other) {
        if (s == other.s
                && p == other.p
                && o == other.o) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isLessThan(SPOTriplet other) {
        String thisTriplet = s + p + o;
        String otherTriplet = other.s + other.p + other.o;
        return thisTriplet.compareTo(otherTriplet) < 0;
    }

    public double cmp(SPOTriplet other) {
        if (this == other || other == null) {
            return 1.0;
        } else if ((s == other.s
                && p == other.p)
                || (s == other.s
                && o == other.o)
                || (o == other.o
                && p == other.p)) {
            return 2.0 / 3.0;
        } else if (s == other.s
                || p == other.p
                || o == other.o) {
            return 1.0 / 3.0;
        } else {
            return 0.0;
        }
    }

    public void cut() {
//     cut ( s );
//     cut ( p );
//     cut ( o );
        try {
            s = s.substring(0, s.indexOf("."));
        } catch (Exception e) {
        }
        try {
            s = s.substring(0, s.indexOf("["));
        } catch (Exception e) {
        }
        try {
            p = p.substring(0, p.indexOf("."));
        } catch (Exception e) {
        }
        try {
            p = p.substring(0, p.indexOf("["));
        } catch (Exception e) {
        }
        try {
            o = o.substring(0, o.indexOf("."));
        } catch (Exception e) {
        }
        try {
            o = o.substring(0, o.indexOf("["));
        } catch (Exception e) {
        }

    }

    //we are using it as a key
    @Override
    public boolean equals(Object obj) {
        return equals((SPOTriplet) obj);
    }

    @Override
    public int hashCode() {
        return s.hashCode() + p.hashCode() + o.hashCode();

    }

};
